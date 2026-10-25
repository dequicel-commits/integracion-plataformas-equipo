# Informe de Pruebas de Integración (QA / Tester) — Peluditos Pet Chile

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Evaluación:** Entrega 1  
**Responsable de Pruebas (QA / Tester):** Lautaro Salinas  
**Fecha de Ejecución:** 25 de octubre de 2026  

---

## 1. Objetivo de las Pruebas

El presente documento registra la ejecución y validación de las pruebas funcionales y de integración para el prototipo de **Peluditos Pet Chile**, descargado directamente desde el repositorio del equipo (`https://github.com/dequicel-commits/integracion-plataformas-equipo/tree/main/Entrega-1`).

El propósito de las pruebas fue comprobar de extremo a extremo (*End-to-End*):
1. La compilación y disponibilidad del **Servicio REST Java** (`servicio-peluditos`).
2. La generación de trabajos externos en **Flowable OSS** cuando el proceso BPMN (`peluditosPetChileTOBEIntegracionJava`) alcanza la tarea de servicio **Consultar servicio REST Java** (`ExternalWorkerTask_21`, tópico `seguimiento-peluditos`).
3. La correcta ejecución del **External Worker** (`external-worker-peluditos`), verificando que adquiera el trabajo pendiente, consulte el servicio REST y reporte la finalización a Flowable.
4. La continuación automática del flujo del proceso BPMN tras la compuerta exclusiva (`compraRealizada == true`) hacia la siguiente actividad de usuario (**Registro cliente y compra realizada**, `FormTask_13`).

---

## 2. Entorno de Pruebas y Observación Técnica (Puerto 7071)

| Componente | Versión / Configuración Utilizada | Rol en la Prueba |
|---|---|---|
| **Sistema Operativo** | Windows 11 (x64) | Ambiente local de ejecución |
| **Java JDK** | OpenJDK 21.0.11 (`ms-21.0.11`) | Compilación y ejecución de proyectos Java |
| **Apache Maven** | Maven 3.9.x | Gestión de dependencias y ejecución (`exec:java`) |
| **Apache Tomcat** | 11.0.27 (Puerto `8080`) | Contenedor de la API `flowable-rest` |
| **Servicio REST Java** | Javalin 6.7.0 (Puerto `7071`) | Endpoint `GET /api/seguimiento` |
| **External Worker** | `flowable-external-worker-client-java` 2.0.0 | Consumidor del tópico `seguimiento-peluditos` |

### Hallazgo Técnico durante la Puesta en Marcha (Puerto 7070 → 7071)
Al clonar el repositorio y levantar `servicio-peluditos` en el puerto original (`7070`), se detectó el error `io.javalin.util.JavalinBindException: Port already in use` debido a que el servicio local de **AnyDesk** en Windows mantiene ocupado el puerto TCP `7070`.

* **Solución aplicada para la prueba:** Se actualizó el puerto a **`7071`** tanto en `servicio-peluditos` (`Javalin.create().start(7071)`) como en `external-worker-peluditos` (`http://localhost:7071/api/seguimiento`), logrando levantar el servicio y ejecutar la integración sin conflictos de red.

---

## 3. Matriz de Casos de Prueba

| ID | Caso de Prueba | Entrada / Acción | Resultado Esperado | Resultado Obtenido | Estado |
|---|---|---|---|---|---|
| **CP-01** | Disponibilidad del Servicio REST Java | Petición `GET http://localhost:7071/api/seguimiento` | Código HTTP `200 OK` y JSON con `"estado": "OK"` | Responde `200 OK` con el JSON esperado | ✅ **Aprobado** |
| **CP-02** | Generación de trabajo en cola External Worker | Avanzar instancia BPMN hasta `ExternalWorkerTask_21` y consultar `/external-job-api/jobs` | Se visualiza 1 trabajo activo con `elementName: "Consultar servicio REST Java"` | Trabajo generado correctamente (`"total": 1`) | ✅ **Aprobado** |
| **CP-03** | Consumo y completado mediante External Worker | Ejecutar `mvn exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"` en `external-worker-peluditos` | Consola muestra `Trabajos recibidos: 1`, respuesta JSON del servicio y `Trabajo completado en Flowable.` | Ejecución exitosa (`BUILD SUCCESS`) en 1.091 s | ✅ **Aprobado** |
| **CP-04** | Continuación del proceso BPMN en Flowable | Consultar `/external-job-api/jobs` y `/service/runtime/tasks` tras ejecutar el Worker | Cola de External Jobs queda en `0` y se crea la tarea `FormTask_13` (*"Registro cliente y compra realizada"*) | Cola en `0` (`"total": 0`) y nueva tarea activa en `/runtime/tasks` (`"total": 3`) | ✅ **Aprobado** |

---

## 4. Desarrollo de las Pruebas y Evidencias

### 4.1. Verificación del Servicio REST Java (`CP-01`)
Con el proyecto `servicio-peluditos` en ejecución, se consultó el endpoint desde el navegador en `http://localhost:7071/api/seguimiento`, obteniendo la respuesta en formato JSON:

```json
{
  "estado": "OK",
  "mensaje": "Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"
}
```

![Verificación del Servicio REST en puerto 7071](./API%20seguimiento.png)

---

### 4.2. Estado Inicial en Flowable antes de Ejecutar el External Worker (`CP-02`)
Antes de ejecutar el External Worker, se verificó el listado de tareas humanas activas en `http://localhost:8080/flowable-rest/service/runtime/tasks`, donde existían únicamente 2 tareas previas (`"total": 2`):

![Tareas de usuario antes de ejecutar el External Worker](./prueba%20servicio%20rest%201%20inicio.png)

Simultáneamente, se consultó el endpoint `http://localhost:8080/flowable-rest/external-job-api/jobs`, confirmando que la instancia de proceso `b62af02a-d07e-11f1-ab20-0a0027000009` (`executionId: b62b1740-d07e-11f1-ab20-0a0027000009`) se encontraba detenida en la actividad **`ExternalWorkerTask_21` ("Consultar servicio REST Java")** con 1 trabajo pendiente en cola (`"total": 1`):

```json
{
  "data": [
    {
      "correlationId": "b63354b3-d07e-11f1-ab20-0a0027000009",
      "createTime": "2026-10-25T14:16:46.647Z",
      "elementId": "ExternalWorkerTask_21",
      "elementName": "Consultar servicio REST Java",
      "executionId": "b62b1740-d07e-11f1-ab20-0a0027000009",
      "id": "b63354b4-d07e-11f1-ab20-0a0027000009",
      "processDefinitionId": "04ecdaf6-ce76-11f1-ab20-0a0027000009",
      "processInstanceId": "b62af02a-d07e-11f1-ab20-0a0027000009",
      "retries": 3,
      "url": "http://localhost:8080/flowable-rest/external-job-api/jobs/b63354b4-d07e-11f1-ab20-0a0027000009"
    }
  ],
  "order": "asc",
  "size": 1,
  "sort": "id",
  "start": 0,
  "total": 1
}
```

![Trabajo pendiente en External Job API antes de ejecutar el Worker](./prueba%20external%20job1%20inicio.png)

---

### 4.3. Ejecución del External Worker desde PowerShell (`CP-03`)
Se configuraron las variables de entorno de conexión a Flowable OSS (`FLOWABLE_API_URL`, `FLOWABLE_USER`, `FLOWABLE_PASSWORD`) y se ejecutó el proyecto `external-worker-peluditos` mediante Maven:

```powershell
$env:JAVA_HOME = "C:\Users\Lautaro\.jdks\ms-21.0.11"
$env:PATH = "$env:JAVA_HOME\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.3\plugins\maven\lib\maven3\bin;$env:PATH"
$env:FLOWABLE_API_URL = "http://localhost:8080/flowable-rest/external-job-api"
$env:FLOWABLE_USER = "rest-admin"
$env:FLOWABLE_PASSWORD = "test"
cd "C:\Users\Lautaro\Desktop\Iplacex\2026-4B-INTEGRACIÓN DE PLATAFORMAS400-CED-QM\Peluditos chile test\external-worker-peluditos"
mvn exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

El programa adquirió el trabajo pendiente (`Trabajos recibidos: 1`), invocó al servicio REST (`Respuesta del servicio: {"estado":"OK","mensaje":"Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"}`) y notificó el término a Flowable (`Trabajo completado en Flowable.`, `BUILD SUCCESS`):

![Ejecución exitosa del External Worker en PowerShell](./externalworker%20test.png)

---

### 4.4. Verificación Posterior en Flowable: Cola Vacía y Avance del Proceso (`CP-04`)
Inmediatamente después de la ejecución en PowerShell, se volvió a consultar `http://localhost:8080/flowable-rest/external-job-api/jobs`, comprobando que el trabajo ya fue procesado y la cola quedó vacía (`"total": 0`):

```json
{"data":[],"order":"asc","size":0,"sort":"id","start":0,"total":0}
```

![Cola de External Jobs vacía después de ejecutar PowerShell](./despues%20de%20ejectar%20PS.png)

Finalmente, al consultar `http://localhost:8080/flowable-rest/service/runtime/tasks`, se verificó que el total de tareas subió a 3 (`"total": 3`) y que la misma instancia de proceso (`processInstanceId: b62af02a-d07e-11f1-ab20-0a0027000009`, `executionId: b62b1740-d07e-11f1-ab20-0a0027000009`) avanzó exitosamente por la compuerta exclusiva hasta la tarea de usuario **`FormTask_13` ("Registro cliente y compra realizada")** con ID `330c9599-d080-11f1-ab20-0a0027000009`:

```json
{
  "id": "330c9599-d080-11f1-ab20-0a0027000009",
  "name": "Registro cliente y compra realizada",
  "taskDefinitionKey": "FormTask_13",
  "executionId": "b62b1740-d07e-11f1-ab20-0a0027000009",
  "processInstanceId": "b62af02a-d07e-11f1-ab20-0a0027000009",
  "createTime": "2026-10-25T14:27:25.605Z"
}
```

![Avance del proceso hacia la tarea Registro cliente y compra realizada](./servidio%20rest%20despues%20de%20ejecutar%20PS.png)

---

## 5. Conclusión de QA / Tester

Las pruebas realizadas confirman que la integración desarrollada para la **Entrega 1 de Peluditos Pet Chile** cumple satisfactoriamente con todos los criterios funcionales y técnicos esperados:
* El **Servicio REST Java** responde de forma estable y con la estructura JSON acordada.
* El **External Worker** se autentica contra la API de Flowable OSS, bloquea y consume los trabajos del tópico `seguimiento-peluditos`, valida el código HTTP `200` del servicio REST y completa la tarea sin errores.
* El motor **Flowable OSS** continúa el flujo BPMN de manera transparente tras la intervención del Worker.
