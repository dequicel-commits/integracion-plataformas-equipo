# Peluditos Pet Chile — Integración de Plataformas

**Proyecto académico — IPLACEX**

Sistema de gestión comercial desarrollado para **Peluditos Pet Chile**, orientado al registro de clientes y mascotas, la atención de consultas y el seguimiento de compras mediante un proceso automatizado.

La solución integra las siguientes tecnologías:

- **BPMN y Flowable Engine OSS 7.2**, para la automatización del proceso comercial.
- **Java con Jakarta REST (JAX-RS)**, para los servicios web.
- **External Workers**, para ejecutar las tareas automáticas del proceso.
- **SQLite**, para almacenar los datos comerciales.
- **HTML, CSS y JavaScript**, para la interfaz web.
- **Apache Tomcat 10.1**, para ejecutar Flowable OSS.
- **Maven**, para compilar y ejecutar los componentes Java.

**Profesor:** Santiago Neira  
**Grupo:** 3  
**Etapa actual:** Entrega 4 — Examen Final  
**Fecha de entrega:** 30 de noviembre de 2026  
**Versiones anteriores:** `entrega-1`, `entrega-2` y Entrega 3.

---

## 1. Integrantes del equipo

| Integrante | Rol en Entrega 4 |
|---|---|
| Elvis Brayan Gutiérrez Bernal | Implementador |
| Felipe Octavio Quicel Soto | Por confirmar |
| Geraldine Andrea González Acuña | Por confirmar |
| Lautaro Nelson Salinas Alvear | Por confirmar |

**Nota:** Los roles pendientes deben actualizarse según la distribución definitiva acordada por el equipo.

## 2. Objetivo del proyecto

Implementar una solución que permita automatizar el registro y seguimiento comercial de los clientes de Peluditos Pet Chile.

La aplicación permite:

1. Registrar consultas realizadas por potenciales clientes.
2. Registrar información de sus mascotas.
3. Iniciar instancias de procesos BPMN mediante Flowable OSS.
4. Guardar los datos comerciales en SQLite.
5. Gestionar el seguimiento de cada cliente.
6. Registrar si el cliente concretó o no la compra.
7. Actualizar automáticamente los resultados mediante External Workers.
8. Consultar los datos desde una interfaz web propia.

La Entrega 4 se enfoca en comprobar la estabilidad de la integración, la persistencia de los datos, la configuración externa y la posibilidad de instalar el sistema en otro equipo.

## 3. Arquitectura de la solución

El sistema utiliza una arquitectura de componentes integrados mediante servicios REST.

**Flujo general:**

```text
Cliente / Emprendedora
         |
         v
Interfaz Web (HTML, CSS y JavaScript)
         |
         v
Servicios Jakarta REST (Puerto 7072)
         |
         v
Flowable Engine OSS (Puerto 8080)
         |
         v
External Workers (Java)
         |
         v
Servicios Jakarta REST
         |
         v
Base de datos SQLite (peluditos.db)
```

### Componentes principales

| Componente | Función |
|---|---|
| Interfaz web | Registro y seguimiento de clientes |
| Jakarta REST | Comunicación entre interfaz, Flowable y base de datos |
| Flowable OSS | Orquestación del proceso BPMN |
| External Worker 1 | Registro automático del cliente y mascota |
| External Worker 2 | Actualización automática del seguimiento comercial |
| SQLite | Persistencia de datos de clientes y mascotas |

## 4. Estructura del repositorio

```text
integracion-plataformas-equipo/
|
|-- README.md
|-- Actas/
|-- Arquitectura/
|-- BPMN/
|-- interfaz-peluditos/
|-- servicio-peluditos/
|-- external-worker-peluditos/
|-- Documentacion/
|   |-- Analisis/
|   |-- GUIA-EJECUCION.md
|   |-- IMPLEMENTACION-ENTREGA3.md
|   |-- Informe-Pruebas-QA-Entrega3.md
|
|-- Evidencias/
|   |-- BPMN/
|   |-- Implementacion/
|   |-- QA/
|
|-- Videos/
```

Las carpetas de evidencias y videos de la Entrega 4 deben organizarse antes de realizar la entrega definitiva.

## 5. Requisitos para ejecutar el proyecto

Se recomienda disponer de:

- Windows 11 o un sistema compatible con Java.
- JDK 21.
- Apache Maven 3.9 o compatible.
- Apache Tomcat 10.1.
- Flowable OSS 7.2.
- Visual Studio Code u otro editor.
- Un navegador web.
- Acceso al repositorio del proyecto.

**Importante:** Flowable OSS debe estar correctamente desplegado y conectado a una base de datos relacional persistente. SQLite se utiliza para almacenar los datos comerciales del sistema.

## 6. Configuración mediante variables de entorno

La aplicación utiliza variables de entorno para evitar que direcciones, puertos, credenciales y rutas dependan de un computador específico.

| Variable | Descripción | Valor de ejemplo |
|---|---|---|
| `PELU_HOST` | Dirección del servidor Jakarta REST | `localhost` |
| `PELU_PORT` | Puerto de Jakarta REST | `7072` |
| `PELU_DB_PATH` | Ruta del archivo SQLite | `peluditos.db` |
| `PELU_INTERFAZ_PATH` | Ruta de la interfaz web | `../interfaz-peluditos` |
| `FLOWABLE_REST_URL` | API REST general de Flowable | `http://localhost:8080/flowable-rest/service` |
| `FLOWABLE_API_URL` | API para tareas externas de Flowable | `http://localhost:8080/flowable-rest/external-job-api` |
| `FLOWABLE_USER` | Usuario de autenticación de Flowable | `rest-admin` |
| `FLOWABLE_PASSWORD` | Contraseña de Flowable | Configurar de forma segura |
| `SERVICIO_PELUDITOS_URL` | Dirección del servicio comercial | `http://localhost:7072` |

### Ejemplo de configuración en PowerShell

Desde la carpeta `servicio-peluditos`:

```powershell
$env:PELU_HOST = "localhost"
$env:PELU_PORT = "7072"
$env:PELU_DB_PATH = (Join-Path (Get-Location) "peluditos.db")
$env:PELU_INTERFAZ_PATH = (Resolve-Path "..\interfaz-peluditos").Path
$env:FLOWABLE_REST_URL = "http://localhost:8080/flowable-rest/service"
$env:FLOWABLE_USER = "rest-admin"

$credencial = Get-Credential -UserName "rest-admin"
$env:FLOWABLE_PASSWORD = $credencial.GetNetworkCredential().Password
```

La contraseña debe ingresarse de forma privada y no debe guardarse en el repositorio.

**Nota:** Las variables de PowerShell configuradas de esta manera se aplican a la terminal actual. Cada componente ejecutado en otra terminal debe recibir las variables que necesita.

## 7. Despliegue del modelo BPMN

Primero se debe iniciar Apache Tomcat y comprobar que Flowable OSS esté disponible.

**API general de Flowable:**

`http://localhost:8080/flowable-rest/service`

El proyecto utiliza el archivo BPMN:

```text
BPMN/peluditos-integracion-oss-entrega2.bpmn20.xml
```

El identificador del proceso utilizado en la integración es:

```text
peluditosPetChileTOBE12
```

Desde la carpeta raíz del proyecto, en PowerShell, se puede desplegar el archivo mediante:

```powershell
curl.exe -i -u rest-admin -F "file=@BPMN/peluditos-integracion-oss-entrega2.bpmn20.xml" "http://localhost:8080/flowable-rest/service/repository/deployments"
```

Se debe ingresar la contraseña cuando se solicite.

Una respuesta **HTTP 201 Created** indica que Flowable recibió correctamente el despliegue.

**Importante:** No es necesario desplegar nuevamente el proceso si la versión correcta ya se encuentra instalada y activa.

## 8. Ejecutar Jakarta REST y SQLite

Abrir una terminal de PowerShell.

Ubicarse en la carpeta:

```powershell
cd servicio-peluditos
```

Configurar las variables necesarias, según la sección 6, y ejecutar:

```powershell
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.ServidorJakarta"
```

El servicio debe permanecer funcionando en:

`http://localhost:7072`

### Direcciones de la interfaz web

**Registro de consultas:**

`http://localhost:7072/interfaz/index.html`

**Panel de la emprendedora:**

`http://localhost:7072/interfaz/emprendedora.html`

**Listado de clientes:**

`http://localhost:7072/interfaz/clientes.html`

**Seguimiento comercial:**

`http://localhost:7072/interfaz/seguimiento.html`

**API de clientes:**

`http://localhost:7072/api/clientes`

## 9. Ejecutar los External Workers

Abrir una segunda terminal de PowerShell.

Ingresar a:

```powershell
cd external-worker-peluditos
```

Configurar las variables de conexión:

```powershell
$env:FLOWABLE_API_URL = "http://localhost:8080/flowable-rest/external-job-api"
$env:FLOWABLE_USER = "rest-admin"
$env:SERVICIO_PELUDITOS_URL = "http://localhost:7072"

$credencial = Get-Credential -UserName "rest-admin"
$env:FLOWABLE_PASSWORD = $credencial.GetNetworkCredential().Password
```

Ejecutar:

```powershell
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App" "-Dexec.args=--loop"
```

El argumento `--loop` permite que los External Workers permanezcan consultando tareas pendientes.

### External Worker 1

**Topic:** `guardar-cliente-peluditos`

Funciones:

- Recibir la tarea desde Flowable.
- Enviar la información comercial a Jakarta REST.
- Registrar el cliente y su mascota en SQLite.
- Devolver los identificadores generados.
- Completar la tarea automática.

### External Worker 2

**Topic:** `actualizar-seguimiento-peluditos`

Funciones:

- Recibir el resultado del seguimiento desde Flowable.
- Consultar el identificador del cliente.
- Enviar una petición HTTP PUT al servicio Jakarta REST.
- Actualizar el estado comercial en SQLite.
- Completar la tarea automática.

**Importante:** El servidor Jakarta REST y los External Workers deben permanecer ejecutándose en terminales separadas.

## 10. Catálogo de servicios web

| Método y ruta | Propósito |
|---|---|
| `POST /api/consultas/{referencia}` | Iniciar una instancia del proceso BPMN |
| `POST /api/consultas/{casoId}/seguimiento` | Registrar el resultado del seguimiento en Flowable |
| `POST /api/clientes` | Guardar cliente y mascota en SQLite |
| `PUT /api/clientes/{id}/seguimiento` | Actualizar estado comercial y observaciones |
| `GET /api/clientes` | Consultar clientes y mascotas registrados |
| `GET /api/seguimiento` | Comprobar disponibilidad del servicio |

### Estados comerciales

Los resultados principales del seguimiento son:

- **En seguimiento:** cliente registrado, pendiente de decisión.
- **Compra realizada:** cliente que concretó la compra.
- **Sin compra:** cliente que no concretó la compra.

## 11. Pruebas de integración realizadas — Entrega 4

El 26 de noviembre de 2026 se realizaron pruebas funcionales del flujo integrado.

### Prueba 1: Compra realizada

**Cliente:** Cliente Prueba Entrega 4  
**Cliente ID:** 1  
**Mascota:** Max  
**Especie:** Perro  
**Resultado:** Compra realizada

Se verificó lo siguiente:

1. Registro del cliente y mascota desde la interfaz web.
2. Creación de una instancia BPMN.
3. Ejecución del External Worker 1.
4. Registro del cliente en SQLite.
5. Actualización del seguimiento desde la interfaz.
6. Ejecución del External Worker 2.
7. Respuesta HTTP 200 de Jakarta REST.
8. Consulta del estado final en SQLite.
9. Finalización de la instancia BPMN en Flowable OSS.

**Resultado:** Prueba exitosa.

### Prueba 2: Sin compra

**Cliente:** Cliente Prueba Sin Compra  
**Cliente ID:** 2  
**Mascota:** Luna  
**Especie:** Gato  
**Resultado:** Sin compra

Se verificó lo siguiente:

1. Registro del cliente y mascota desde la interfaz web.
2. Creación de una instancia BPMN.
3. Ejecución del External Worker 1.
4. Registro inicial con estado «En seguimiento».
5. Actualización del seguimiento con la opción «Sin compra».
6. Ejecución del External Worker 2.
7. Actualización mediante `PUT /api/clientes/2/seguimiento`.
8. Respuesta HTTP 200.
9. Recuperación del estado final desde SQLite.
10. Finalización de la instancia BPMN en Flowable OSS.

**Resultado:** Prueba exitosa.

### Resumen de pruebas

| Prueba | Resultado |
|---|---|
| Registro de cliente y mascota | Correcto |
| Inicio de instancia BPMN | Correcto |
| External Worker 1 | Correcto |
| External Worker 2 | Correcto |
| Ruta «Compra realizada» | Correcto |
| Ruta «Sin compra» | Correcto |
| Actualización en SQLite | Correcto |
| Recuperación de datos desde SQLite | Correcto |
| Persistencia de SQLite después de reiniciar Jakarta REST | Correcto |
| Finalización de ambas instancias en Flowable | Correcto |

## 12. Verificación de instancias en Flowable

Los dos procesos de prueba finalizaron correctamente.

| Cliente | ID de instancia Flowable | Resultado |
|---|---|---|
| Max | `2a8ecd0b-e9b5-11f1-8b8b-28f52b9c2cb1` | Finalizado |
| Luna | `b984889a-e9bc-11f1-8b8b-28f52b9c2cb1` | Finalizado |

Se utilizó el endpoint:

```text
GET /flowable-rest/service/history/historic-process-instances/{id}
```

Ambas respuestas incluyeron el campo `endTime`, confirmando la finalización de las instancias.

## 13. Persistencia de datos

La solución utiliza SQLite para almacenar datos de clientes y mascotas.

Se comprobó que la información comercial permaneció disponible después de detener y reiniciar el servidor Jakarta REST.

La ubicación del archivo SQLite puede configurarse mediante:

```text
PELU_DB_PATH
```

La persistencia del motor Flowable debe comprobarse de manera independiente, revisando la configuración de su base de datos relacional y su comportamiento después de reiniciar Tomcat.

## 14. Evidencias de implementación

Durante las pruebas de la Entrega 4 se generaron evidencias con los siguientes nombres:

```text
IMP_Entrega4_Compilacion_Exitosa.png

IMP_Entrega4_Seguimiento_Compra_Realizada.png

IMP_Entrega4_Persistencia_SQLite.png

IMP_Entrega4_Seguimiento_Sin_Compra.png

IMP_Entrega4_Flowable_Procesos_Finalizados.png
```

Estas capturas permiten respaldar la compilación, la ejecución de ambas rutas comerciales, la recuperación de datos desde SQLite y la finalización de los procesos BPMN.

**Pendiente:** Incorporar las evidencias definitivas en las carpetas correspondientes del repositorio.

## 15. Documentación complementaria

El proyecto dispone de documentos de análisis, modelado, implementación y pruebas.

- [Arquitectura de la solución](Arquitectura/README.md)
- [Modelos BPMN](BPMN/README.md)
- [Análisis de requisitos — Entrega 3](Documentacion/Analisis/ANALISIS-REQUISITOS-ENTREGA3.md)
- [Matriz de trazabilidad](Documentacion/Analisis/MATRIZ-TRAZABILIDAD-ENTREGA3.md)
- [Modelo Entidad-Relación](Documentacion/Analisis/DIAGRAMA-ENTIDAD-RELACION.MD)
- [Implementación — Entrega 3](Documentacion/IMPLEMENTACION-ENTREGA3.md)
- [Informe QA — Entrega 3](Documentacion/Informe-Pruebas-QA-Entrega3.md)
- [Guía de ejecución](Documentacion/GUIA-EJECUCION.md)
- [Actas del equipo](Actas/)

## 16. Videos de la Entrega 4

La entrega final debe incluir los materiales audiovisuales solicitados para el examen:

1. **Video tutorial para la emprendedora:** explicación del uso de la interfaz y del seguimiento comercial.
2. **Video técnico para el profesor:** demostración de la arquitectura, la integración y el funcionamiento del proceso BPMN.
3. **Video tutorial de instalación:** explicación de los requisitos, la configuración y la ejecución de los componentes.

Los videos deben organizarse en la carpeta:

```text
Videos/Entrega-4/
```

Los enlaces definitivos se incorporarán cuando el equipo complete las grabaciones.

## 17. Consideraciones de seguridad y portabilidad

Para instalar el sistema en otro computador:

- Instalar los requisitos técnicos.
- Configurar Flowable OSS y su base de datos.
- Desplegar el BPMN.
- Configurar las variables de entorno.
- Iniciar Jakarta REST.
- Iniciar los External Workers.
- Abrir la interfaz web.
- Ejecutar las pruebas de registro y seguimiento.

**Recomendaciones:**

- No publicar contraseñas en GitHub.
- No incluir credenciales reales en archivos de configuración.
- Mantener las rutas y puertos configurables.
- No publicar datos personales reales utilizados en pruebas.
- Validar los servicios antes de iniciar la interfaz.
- Verificar la persistencia después de reiniciar los componentes.

## 18. Estado de la Entrega 4

**Comprobado:**

- Compilación de los componentes Java.
- Funcionamiento de Jakarta REST.
- Ejecución de External Workers.
- Integración con Flowable OSS 7.2.
- Registro de clientes y mascotas en SQLite.
- Rutas BPMN de compra y no compra.
- Finalización de las dos instancias de prueba.
- Persistencia de SQLite después de reiniciar Jakarta REST.

**Pendiente de completar o verificar:**

- Persistencia de la base de datos relacional de Flowable después de reiniciar Tomcat.
- Revisión completa de la instalación en otro computador.
- Actualización definitiva de la guía de ejecución.
- Organización de todas las evidencias de la Entrega 4.
- Incorporación de los tres videos.
- Confirmación de roles definitivos del equipo.
- Publicación de cambios en el repositorio.
- Creación del tag final `entrega-final`.

---

**Proyecto desarrollado por el Grupo 3 de IPLACEX para la asignatura Integración de Plataformas.**

**Caso de estudio:** Peluditos Pet Chile.  
**Entrega:** Examen Final — Entrega 4, noviembre de 2026.
