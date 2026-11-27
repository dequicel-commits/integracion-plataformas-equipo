# Guía de ejecución y configuración — Peluditos Pet Chile (Entrega 4)

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Entrega:** Examen final — Entrega 4 (noviembre de 2026)

## 1. Objetivo

Explicar cómo preparar, configurar, ejecutar y verificar la solución Peluditos Pet Chile. El proyecto integra una interfaz HTML/CSS/JavaScript, servicios Java Jakarta REST (Jersey/Grizzly), dos External Workers, Flowable OSS 7.2 y bases de datos relacionales con responsabilidades distintas: **SQLite** almacena la información comercial y **H2**, en la configuración local verificada, almacena información del motor Flowable.

Las rutas comerciales **«Compra realizada»** y **«Sin compra»** fueron probadas en un computador con Windows. También se comprobó que el historial de Flowable se conserva después de reiniciar Tomcat y que los registros comerciales persisten después de reiniciar Jakarta REST. **La instalación completa en otro computador aún no se ha verificado**.

## 2. Requisitos y herramientas

| Herramienta | Versión o configuración de referencia | Uso |
|---|---|---|
| Java JDK | 21 en las pruebas de Entrega 4; los proyectos Maven compilan con `release=17` | Ejecución y compilación de Java |
| Apache Maven | 3.9.16 en las pruebas de Entrega 4 | Compilación y ejecución |
| Jakarta REST | Jersey / Grizzly | API REST en el puerto 7072 |
| SQLite JDBC | Dependencia Maven del proyecto | Persistencia comercial |
| Flowable OSS | 7.2.0 | Motor BPMN y API de tareas externas |
| Apache Tomcat | 10.1 | Contenedor de `flowable-rest.war` |
| H2 | Base de datos configurada en Flowable REST | Persistencia del motor BPMN |
| Git y GitHub | Git for Windows / repositorio compartido | Código, evidencias e historial |
| PowerShell y VS Code | Windows 11 en el entorno probado | Terminal y edición |

Verificar desde PowerShell:

```powershell
java -version
mvn -version
git --version
```

**Compatibilidad:** los proyectos compilan para Java 17, aunque en las pruebas de Entrega 4 se utilizó JDK 21. Los comandos siguientes están escritos para PowerShell; en otros sistemas operativos deberán adaptarse.

## 3. Estructura del repositorio y descarga

```text
integracion-plataformas-equipo/
├── README.md
├── BPMN/
├── interfaz-peluditos/
├── servicio-peluditos/
├── external-worker-peluditos/
├── Arquitectura/
├── Documentacion/
│   └── GUIA-EJECUCION.md
├── Evidencias/
│   └── Implementacion/
│       └── Entrega-4/
├── Actas/
└── Videos/
```

Desde una carpeta elegida para el proyecto:

```powershell
git clone https://github.com/dequicel-commits/integracion-plataformas-equipo.git
cd integracion-plataformas-equipo
```

Si el proyecto ya está clonado y tiene modificaciones locales, comprobar `git status` antes de actualizar para no sobrescribir trabajo pendiente. Trabajar con una copia de respaldo de la base de datos cuando se realicen pruebas destructivas.

## 4. Componentes principales

### 4.1. Interfaz web

Pantallas de registro de consultas, panel de la emprendedora, listado de clientes y seguimiento comercial. El servidor Jakarta REST entrega los archivos HTML y expone los servicios a los que se conecta JavaScript.

### 4.2. Servicios Jakarta REST

La clase principal es `cl.iplacex.peluditos.ServidorJakarta`. Se utilizó el puerto `7072`. Los servicios registran clientes y mascotas, consultan registros comerciales y actualizan el seguimiento. La ruta de la interfaz puede configurarse con `PELU_INTERFAZ_PATH`.

### 4.3. Flowable OSS

Flowable REST se ejecutó mediante Tomcat, en `http://localhost:8080/flowable-rest`. La clave del proceso ejecutado es `peluditosPetChileTOBE12`.

### 4.4. External Workers

- **Worker 1 (`guardar-cliente-peluditos`):** consume tareas de Flowable y solicita a Jakarta REST el registro del cliente y su mascota en SQLite.
- **Worker 2 (`actualizar-seguimiento-peluditos`):** consume tareas de seguimiento y solicita a Jakarta REST la actualización del estado comercial.

### 4.5. Bases de datos

- **SQLite:** guarda clientes, mascotas, tipos de mascota, productos y seguimiento comercial.
- **H2:** según la configuración local observada, guarda el estado e historial del motor Flowable. La base H2 es independiente de `peluditos.db`.

## 5. Configuración externa y portabilidad

Las variables de entorno se establecen **en la terminal donde se ejecuta cada componente**. Una variable establecida en una ventana PowerShell no se comparte automáticamente con otra.

### 5.1. Servicio Jakarta REST

| Variable | Función | Ejemplo local |
|---|---|---|
| `PELU_HOST` | Dirección de escucha del servidor | `localhost` |
| `PELU_PORT` | Puerto del servidor | `7072` |
| `PELU_DB_PATH` | Ruta de SQLite | Ruta completa de `peluditos.db` |
| `PELU_INTERFAZ_PATH` | Ruta de los archivos HTML | `../interfaz-peluditos` |
| `FLOWABLE_REST_URL` | API general de Flowable | `http://127.0.0.1:8080/flowable-rest/service` |
| `FLOWABLE_USER` | Usuario REST de Flowable | `rest-admin` |
| `FLOWABLE_PASSWORD` | Contraseña REST | Ingresada de forma privada |

**Ejemplo:** abrir PowerShell desde la **raíz del repositorio** y ejecutar:

```powershell
cd servicio-peluditos
$env:PELU_HOST = "localhost"
$env:PELU_PORT = "7072"
$env:PELU_DB_PATH = (Join-Path (Get-Location) "peluditos.db")
$env:PELU_INTERFAZ_PATH = (Resolve-Path "..\interfaz-peluditos").Path
$env:FLOWABLE_REST_URL = "http://127.0.0.1:8080/flowable-rest/service"
$env:FLOWABLE_USER = "rest-admin"
$credencial = Get-Credential -UserName "rest-admin" -Message "Credenciales REST de Flowable"
$env:FLOWABLE_PASSWORD = $credencial.GetNetworkCredential().Password
```

La carpeta de la interfaz debe existir para que `Resolve-Path` funcione. Para mantener la misma base SQLite en sucesivos arranques, utilizar **siempre la misma ruta** `PELU_DB_PATH`.

### 5.2. External Workers

| Variable | Función | Ejemplo local |
|---|---|---|
| `FLOWABLE_API_URL` | API específica de trabajos externos | `http://127.0.0.1:8080/flowable-rest/external-job-api` |
| `FLOWABLE_USER` | Usuario de Flowable | `rest-admin` |
| `FLOWABLE_PASSWORD` | Contraseña REST | Ingresada de forma privada |
| `SERVICIO_PELUDITOS_URL` | URL del servicio Jakarta REST | `http://localhost:7072` |

En una **segunda ventana PowerShell**, desde la raíz del repositorio:

```powershell
cd external-worker-peluditos
$env:FLOWABLE_API_URL = "http://127.0.0.1:8080/flowable-rest/external-job-api"
$env:FLOWABLE_USER = "rest-admin"
$env:SERVICIO_PELUDITOS_URL = "http://localhost:7072"
$credencial = Get-Credential -UserName "rest-admin" -Message "Credenciales REST de Flowable"
$env:FLOWABLE_PASSWORD = $credencial.GetNetworkCredential().Password
```

**Importante:** para las pruebas de Entrega 4 se ejecutó el programa con `-Dexec.args=--loop` para que siga consultando tareas. No depender solamente de una variable `WORKER_LOOP` sin verificar en el código que se implemente.

### 5.3. Seguridad

No subir al repositorio contraseñas, tokens ni archivos de configuración que contengan secretos. Evitar credenciales incrustadas en comandos compartidos. Las credenciales predeterminadas de una instalación de prueba de Flowable deben reemplazarse antes de exponer el servicio fuera del entorno local.

## 6. Iniciar Flowable OSS y desplegar BPMN

1. Iniciar Apache Tomcat 10.1 con `flowable-rest.war` desplegado.
2. Confirmar que Flowable REST responde. Desde PowerShell:

```powershell
$credencial = Get-Credential -UserName "rest-admin" -Message "Credenciales REST de Flowable"
Invoke-WebRequest -Uri "http://localhost:8080/flowable-rest/service/management/engine" -Credential $credencial -UseBasicParsing | Select-Object StatusCode
```

Se espera `StatusCode : 200` cuando el servicio está operativo y el usuario tiene permisos.

3. **Solo si el proceso aún no está desplegado**, desplegar el archivo BPMN desde la raíz del repositorio:

```powershell
curl.exe -i -u rest-admin -F "file=@BPMN/peluditos-integracion-oss-entrega2.bpmn20.xml" "http://127.0.0.1:8080/flowable-rest/service/repository/deployments"
```

El cliente `curl.exe` solicitará la contraseña; no escribirla directamente en la guía. Se espera **HTTP 201 Created** al desplegar correctamente. Evitar despliegues repetidos si ya está instalada la versión requerida.

**Nota:** el archivo ejecutable conserva `entrega2` en su nombre, pero en las pruebas de Entrega 4 se utilizó con la clave `peluditosPetChileTOBE12` y los topics `guardar-cliente-peluditos` y `actualizar-seguimiento-peluditos`. No reemplazarlo automáticamente por otro diagrama BPMN sin verificar la compatibilidad.

### 6.1. Persistencia de Flowable: configuración comprobada

En la instalación local, `flowable-default.properties` contenía:

```properties
spring.datasource.url=jdbc:h2:~/flowable-db/ossdb;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=9091;DB_CLOSE_DELAY=-1
flowable.database-schema-update=true
flowable.history-level=full
```

La expresión `~` corresponde al directorio personal de la **cuenta que ejecuta Tomcat**, que puede no ser la cuenta del usuario que inicia sesión en Windows. No eliminar ni mover los archivos de H2 sin un respaldo. En las pruebas de Entrega 4 se reinició Tomcat y se recuperaron los historiales finalizados de Max y Luna.

## 7. Compilar y ejecutar Jakarta REST y SQLite

Desde la terminal configurada en la sección 5.1, dentro de `servicio-peluditos`:

```powershell
mvn clean package
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.ServidorJakarta"
```

Mantener la ventana abierta. Si `7072` ya está ocupado, identificar el proceso que lo utiliza antes de detenerlo; no cerrar procesos Java desconocidos.

Comprobar desde otra terminal:

```powershell
curl.exe -i http://localhost:7072/api/seguimiento
curl.exe -i http://localhost:7072/api/clientes
```

Una respuesta HTTP 200 comprueba que el servicio correspondiente responde. Si la base es nueva, la lista de clientes puede ser `[]`.

## 8. Compilar y ejecutar los External Workers

Desde la terminal configurada en la sección 5.2, dentro de `external-worker-peluditos`:

```powershell
mvn clean package
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App" "-Dexec.args=--loop"
```

Mantener la terminal abierta mientras se realizan registros y seguimientos. En las pruebas, el Worker 1 recibió una respuesta HTTP **201** al guardar clientes y el Worker 2 una respuesta HTTP **200** al actualizar seguimientos.

La URL de adquisición de trabajos **no** es `/flowable-rest/service`; corresponde a `/flowable-rest/external-job-api`. Una URL equivocada puede impedir que el Worker reciba tareas.

## 9. Abrir la interfaz web

Con el servidor Jakarta REST funcionando:

| Pantalla | URL |
|---|---|
| Registro de consultas | `http://localhost:7072/interfaz/index.html` |
| Panel de la emprendedora | `http://localhost:7072/interfaz/emprendedora.html` |
| Listado de clientes | `http://localhost:7072/interfaz/clientes.html` |
| Seguimiento comercial | `http://localhost:7072/interfaz/seguimiento.html` |

Si no se muestran las pantallas, comprobar `PELU_INTERFAZ_PATH`, el puerto de Jakarta REST y la existencia de los archivos HTML.

## 10. Servicios web principales

| Método y ruta | Uso |
|---|---|
| `POST /api/consultas/{referencia}` | Iniciar el caso en Flowable |
| `POST /api/consultas/{casoId}/seguimiento` | Comunicar el seguimiento al proceso BPMN |
| `POST /api/clientes` | Registrar cliente y mascota mediante Worker 1 |
| `PUT /api/clientes/{id}/seguimiento` | Actualizar compra y observaciones mediante Worker 2 |
| `GET /api/clientes` | Listar clientes y mascotas de SQLite |
| `GET /api/seguimiento` | Consultar estado del servicio |

## 11. Pruebas de los recorridos comerciales

### 11.1. Registro

1. Abrir `/interfaz/index.html`.
2. Ingresar un cliente ficticio y los datos de su mascota.
3. Enviar la consulta una sola vez y confirmar el mensaje de recepción.
4. Revisar el Worker 1 y la respuesta de registro HTTP 201.
5. Consultar `GET /api/clientes` y verificar el cliente y el `FLOWABLE_CASO_ID`.

### 11.2. Seguimiento — compra y no compra

1. Abrir `/interfaz/seguimiento.html` desde el panel de la emprendedora.
2. Seleccionar un cliente registrado con seguimiento pendiente.
3. Elegir **Sí — Compra realizada** o **No — Sin compra**.
4. Escribir observaciones y enviar una sola vez.
5. Revisar el Worker 2, la solicitud PUT y su respuesta HTTP 200.
6. Consultar nuevamente `GET /api/clientes` para confirmar el nuevo estado.
7. Consultar el historial Flowable para comprobar `endTime` de la instancia.

### 11.3. Resultados comprobados el 26 de noviembre de 2026

| Caso ficticio | Cliente ID | Mascota | Estado final | BPMN |
|---|---:|---|---|---|
| Cliente Prueba Entrega 4 | 1 | Max | Compra realizada | Finalizado |
| Cliente Prueba Sin Compra | 2 | Luna | Sin compra | Finalizado |

Historiales verificados:

- Max: `2a8ecd0b-e9b5-11f1-8b8b-28f52b9c2cb1`, `endTime=2026-11-26T16:32:45.400Z`.
- Luna: `b984889a-e9bc-11f1-8b8b-28f52b9c2cb1`, `endTime=2026-11-26T17:15:46.894Z`.

Ambas instancias se recuperaron como finalizadas tras reiniciar Tomcat. Los registros SQLite fueron consultados con los estados esperados; también se comprobó que los datos comerciales permanecen disponibles al reiniciar Jakarta REST.

### 11.4. Consulta del historial de Flowable

```powershell
$credencial = Get-Credential -UserName "rest-admin" -Message "Credenciales REST de Flowable"
$casos = @(
    @{ Nombre = "Max - Compra realizada"; Id = "2a8ecd0b-e9b5-11f1-8b8b-28f52b9c2cb1" },
    @{ Nombre = "Luna - Sin compra"; Id = "b984889a-e9bc-11f1-8b8b-28f52b9c2cb1" }
)
foreach ($caso in $casos) {
    $url = "http://localhost:8080/flowable-rest/service/history/historic-process-instances/$($caso.Id)"
    $resultado = Invoke-RestMethod -Uri $url -Credential $credencial -Method Get
    Write-Host "Proceso:" $caso.Nombre
    Write-Host "Finalizado:" ($null -ne $resultado.endTime)
    Write-Host "Fecha de finalizacion:" $resultado.endTime
    Write-Host "----------------------------------------"
}
```

Las instancias de prueba son evidencia del entorno local; los IDs serán distintos en otras instalaciones.

### 11.5. Pruebas de validación

En documentación QA de la Entrega 3 se registró una respuesta HTTP 400 para una solicitud de registro incompleta. Para repetirla en un entorno de pruebas:

```powershell
curl.exe -i -X POST "http://localhost:7072/api/clientes" -H "Content-Type: application/json" --data-raw "{}"
```

No usar clientes reales en las pruebas ni reejecutar acciones de registro innecesariamente.

## 12. Persistencia y respaldo

### SQLite

La clase `BaseDatos.java` inicializa las estructuras requeridas, incluidas `TABLA_CLIENTE`, `TABLA_MASCOTA`, `TABLA_TIPO_MASCOTA` y `TABLA_PRODUCTO`. La ruta de la base está controlada por `PELU_DB_PATH`. Si se inicia el servicio con otra ruta, podría crearse un archivo nuevo y parecer que los datos anteriores desaparecieron.

Para respaldar una instalación local de prueba, detener primero el servicio que escribe en SQLite y copiar el archivo `peluditos.db` a un lugar seguro. No publicarlo si contiene información personal.

### H2

Flowable utiliza H2 en la configuración local inspeccionada. El almacenamiento en disco y la conservación del historial después del reinicio de Tomcat se comprobaron con las dos instancias de prueba. Para realizar un respaldo consistente, detener Flowable y seguir el procedimiento correspondiente a su motor de base de datos; evitar copiar o eliminar archivos mientras estén en uso.

**Limitación:** se verificaron la lectura del historial y la persistencia tras reinicios locales, pero no una recuperación completa desde respaldo ni la instalación en un computador distinto.

## 13. Problemas frecuentes

| Síntoma | Verificación sugerida |
|---|---|
| Jakarta REST no inicia | Revisar `java -version`, `mvn -version`, puerto 7072 y rutas de interfaz / SQLite |
| Flowable responde 401 | Confirmar usuario, contraseña y privilegios REST; no compartir credenciales |
| Flowable no responde tras iniciar Tomcat | Comprobar servicio Tomcat, despliegue de `flowable-rest.war` y URL correcta |
| Worker obtiene error al adquirir trabajos | Verificar `FLOWABLE_API_URL` con `/flowable-rest/external-job-api` |
| Worker no recibe tareas | Confirmar `--loop`, BPMN desplegado, topics y conectividad con Flowable |
| La interfaz no aparece | Revisar `PELU_INTERFAZ_PATH` y `/interfaz/index.html` |
| No aparecen los clientes | Confirmar la ruta `PELU_DB_PATH`; podría haberse creado una base nueva |
| Seguimiento no avanza | Verificar cliente correcto, `FLOWABLE_CASO_ID`, tareas pendientes y logs del Worker 2 |
| Comandos fallan | Comprobar carpeta actual, PowerShell y dependencias instaladas |

## 14. Limitaciones y tareas pendientes de la Entrega 4

- Realizar una **instalación completa en otro computador** y registrar la evidencia.
- Verificar la coherencia de la documentación con los cambios finales del equipo.
- Revisar la robustez frente a errores y desconexiones.
- Documentar y probar el procedimiento completo de respaldo y recuperación.
- Completar y publicar las evidencias y los materiales audiovisuales acordados.
- Confirmar los roles finales, los cambios pendientes del monorepo y el tag `entrega-final`.

La gestión de campañas y fechas especiales puede considerarse una **ampliación futura**; no se declara implementada ni necesaria para demostrar las rutas principales verificadas de esta entrega.

## 15. Evidencias y documentación relacionada

- `Documentacion/IMPLEMENTACION-ENTREGA3.md`: implementación documentada en la etapa anterior.
- `Documentacion/Informe-Pruebas-QA-Entrega3.md`: resultados QA de la Entrega 3.
- `Documentacion/Analisis/`: análisis y trazabilidad.
- `Arquitectura/`: diagramas.
- `BPMN/`: modelos BPMN.
- `Evidencias/Implementacion/Entrega-3/`: pruebas anteriores.
- `Evidencias/Implementacion/Entrega-4/`: capturas locales de compilación, rutas comerciales y persistencia, pendientes de publicación en GitHub hasta su incorporación al repositorio.
- `Videos/Entrega-4/`: destino previsto de los enlaces a los tutoriales y al video técnico de la Entrega 4.

## 16. Conclusión

Las pruebas locales de la Entrega 4 verificaron el registro de clientes y mascotas, la integración de Jakarta REST con Flowable OSS, la ejecución de ambos External Workers, las rutas **«Compra realizada»** y **«Sin compra»**, y la persistencia de SQLite y del historial H2/Flowable tras reiniciar sus respectivos servicios. La portabilidad mediante variables de entorno está documentada; falta comprobar la instalación de extremo a extremo en otro computador y completar la publicación de los entregables finales.
