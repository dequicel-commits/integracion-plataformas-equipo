# Guía para el Tester — Peluditos Pet Chile (Entrega 2)

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Entrega:** Segunda evaluación

> Para reproducir el registro desde la interfaz web, sigue la [sección 5](#5-prueba-desde-el-formulario-web).

## 1. Objetivo

Este documento tiene como finalidad compartir la información de la implementación realizada con Java, SQLite y Flowable para la **Entrega 2**, de modo que cualquier integrante del equipo pueda levantar el proyecto en su Flowable local y realizar las pruebas de integración correspondientes.

En esta iteración se incorporó la persistencia en base de datos relacional (**SQLite**) con las 4 tablas acordadas (`TABLA_CLIENTE`, `TABLA_MASCOTA`, `TABLA_TIPO_MASCOTA` y `TABLA_PRODUCTO`) y la integración mediante el External Worker en el topic **`guardar-cliente-peluditos`**.

---

## 2. Programas utilizados

| Programa | Versión utilizada | Uso |
|---|---|---|
| Java JDK | 17 / 21 | Compilación y ejecución de los proyectos Java |
| Apache Maven | 3.9+ | Gestión de dependencias y ejecución de `servicio-peluditos` y `external-worker-peluditos` |
| SQLite (JDBC) | 3.49.1.0 | Motor de base de datos embebido (`peluditos.db`, se crea automáticamente al iniciar el servicio) |
| Apache Tomcat | 10.1 / 11.0 | Ejecución de Flowable UI y Flowable REST local |
| Flowable OSS | 6.8.1 / 7.2.0 / 8.0.0 | Ejecución del proceso BPMN, formularios y External Worker |
| GitHub | Plataforma web | Código fuente, modelo BPMN y documentación |

---

## 3. Archivos de la implementación en `Entrega-2`

Dentro de la carpeta `Entrega-2` del repositorio se encuentran:

- **`Analisis/`**:
  - `ANALISIS-REQUISITOS-ENTREGA2.md`: análisis de requisitos del emprendimiento.
  - `DIAGRAMA-ENTIDAD-RELACION.MD`: diseño de las 4 tablas de la base de datos (`TABLA_CLIENTE` incluyendo `CLIENTE_AUTORIZA`, `TABLA_MASCOTA`, `TABLA_TIPO_MASCOTA` y `TABLA_PRODUCTO`).
- **`servicio-peluditos/`**: proyecto Java con Javalin + SQLite (`BaseDatos.java` y `App.java`).
- **`external-worker-peluditos/`**: proyecto Java del External Worker configurado para el topic `guardar-cliente-peluditos`.
- **`bpmn-oss/`**:
  - `peluditos-integracion-oss-entrega2.bpmn20.xml`: modelo BPMN TO-BE de la Entrega 2 adaptado para Flowable OSS local.
  - `form_registro_cliente.form`: formulario para la tarea humana *Registrar datos del potencial cliente*.
  - `form_seguimiento_comercial.form`: formulario para la tarea humana *Realizar seguimiento comercial*.

---

## 4. Cómo levantar el proyecto en otro computador con Flowable local

### Paso 1: Clonar o actualizar el repositorio
Abrir una terminal (PowerShell o CMD) y ejecutar:

```powershell
git clone https://github.com/dequicel-commits/integracion-plataformas-equipo.git
cd integracion-plataformas-equipo
git pull origin main
```

### Paso 2: Levantar el Servicio REST y la Base de Datos SQLite (`servicio-peluditos`)
No es necesario instalar ningún motor de base de datos externo. Al iniciar el servicio REST por primera vez, SQLite crea automáticamente el archivo `peluditos.db`, construye las 4 tablas y precarga el catálogo base de tipos de mascota y productos.

Abrir una primera terminal y ejecutar:

```powershell
cd Entrega-2/servicio-peluditos
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

> **Nota sobre el puerto:** El servicio intenta iniciar en `http://localhost:7070`. Si el puerto `7070` está ocupado en el computador (por ejemplo, por AnyDesk), inicia automáticamente en `http://localhost:7071` e informa la URL en consola.

**Endpoints disponibles para verificar en el navegador:**
- `GET http://localhost:7070/api/clientes` (o `7071`): lista los potenciales clientes y sus mascotas guardados en SQLite.
- `GET http://localhost:7070/api/productos` (o `7071`): lista el catálogo de productos cruzado con `TABLA_TIPO_MASCOTA`.
- `POST http://localhost:7070/api/clientes` (o `7071`): recibe los datos del cliente y mascota desde el External Worker y los inserta en `TABLA_CLIENTE` y `TABLA_MASCOTA`.

---

### Paso 3: Cargar el BPMN en Flowable local
Con Tomcat y Flowable corriendo en `http://localhost:8080`:

1. **Si se utiliza Flowable UI (`http://localhost:8080/flowable-ui/`):**
   - Ingresar con usuario `admin` y contraseña `test`.
   - En **Modeler** (`http://localhost:8080/flowable-ui/modeler/`), importar el proceso `Entrega-2/bpmn-oss/peluditos-integracion-oss-entrega2.bpmn20.xml` y los formularios `form_registro_cliente.form` y `form_seguimiento_comercial.form`, o desplegarlos mediante la API de Flowable:
     ```powershell
     curl.exe -u admin:test -F "file=@Entrega-2/bpmn-oss/peluditos-integracion-oss-entrega2.bpmn20.xml" http://localhost:8080/flowable-ui/process-api/repository/deployments
     ```
2. **Si se utiliza Flowable REST (`http://localhost:8080/flowable-rest/`):**
   ```powershell
   curl.exe -u rest-admin:test -F "file=@Entrega-2/bpmn-oss/peluditos-integracion-oss-entrega2.bpmn20.xml" http://localhost:8080/flowable-rest/service/repository/deployments
   ```

---

### Paso 4: Iniciar una instancia y registrar los datos del potencial cliente
1. Iniciar una instancia del proceso **`peluditosPetChile-TOBE`** (`key: peluditosPetChileTOBE12`).
2. Completar la primera tarea de usuario (**`Registrar datos del potencial cliente`**) ingresando los datos del cliente y su mascota.
3. Al completarla, el flujo avanzará a la tarea automática **`Guardar datos del potencial cliente`** (External Worker con topic `guardar-cliente-peluditos`).

---

### Paso 5: Ejecutar el External Worker (`external-worker-peluditos`)
Abrir una segunda terminal y ejecutar:

```powershell
cd Entrega-2/external-worker-peluditos
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

*(Por defecto el worker se conecta a `http://localhost:8080/flowable-ui/external-job-api` con `admin:test`. Si se usa `flowable-rest`, basta con definir antes en la terminal:)*
```powershell
$env:FLOWABLE_API_URL="http://localhost:8080/flowable-rest/external-job-api"
$env:FLOWABLE_USER="rest-admin"
$env:FLOWABLE_PASSWORD="test"
```

**Resultado esperado en consola al ejecutar el External Worker:**

```text
Trabajos recibidos (topic: guardar-cliente-peluditos): 1
Enviando datos a /api/clientes: {"clienteNombre":"LAUTARO SALINAS","clienteTelefono":"56933955353","clienteCorreo":"YOMERO@GMAIL.COM","clienteCanalContacto":"WHATSAPP","clienteFechaConsulta":"2026-11-07","clienteConsultaRealizada":"COMIDA GATO","clienteEstadoCompra":"En seguimiento","clienteAutoriza":true,"clienteObservaciones":"Registrado desde Flowable","mascotaNombre":"MAOMAO","mascotaTipoId":4,"mascotaCumpleano":"17-11-2025","mascotaObservaciones":"GATA BEBE"}
Respuesta del servicio REST (/api/clientes): {"estado":"OK","mensaje":"Potencial cliente guardado correctamente en la base de datos","clienteId":3,"mascotaId":3}
Trabajo completado en Flowable (jobId: ca6b7409-da59-11f1-ac85-0a0027000009).
BUILD SUCCESS
```

Una vez ejecutado el worker:
1. Se puede abrir `http://localhost:7070/api/clientes` (o `7071`) en el navegador para verificar que el cliente y su mascota quedaron guardados en SQLite.
2. En el BPMN OSS, después del guardado se habilita la tarea **Atender y responder consulta del cliente**. El seguimiento comercial viene después.

---

## 5. Prueba desde el formulario web

Con esta prueba revisamos que una consulta ingresada en la página llegue a Flowable OSS y que el worker guarde al cliente y su mascota en SQLite. Este recorrido cubre el registro inicial.

### Antes de empezar

Descarga o actualiza el repositorio. Comprueba que `JAVA_HOME` apunta al JDK 21 y que `mvn -v` muestra Maven y Java 21. Descarga y descomprime Flowable OSS 7.2.0. Dentro de `wars` está `flowable-rest.war`. [Descarga de Flowable OSS 7.2.0](https://github.com/flowable/flowable-engine/releases/tag/flowable-7.2.0)

### 1. Levantar Flowable y cargar el BPMN

En una ventana de PowerShell, pega el comando. Cuando te pida la ruta, copia la ubicación completa de `flowable-rest.war`.

```powershell
$war = (Read-Host 'Ruta completa de flowable-rest.war').Trim('"')
& "$env:JAVA_HOME\bin\java.exe" -jar $war --server.address=127.0.0.1 --server.port=8080
```

Cuando Flowable arranque, deja esa ventana abierta. En otra ventana, pega este bloque. Ingresa la ruta completa de la carpeta `Entrega-2` del repositorio.

```powershell
$entrega = (Read-Host 'Ruta completa de la carpeta Entrega-2').Trim('"')
$bpmn = Join-Path $entrega 'bpmn-oss\peluditos-integracion-oss-entrega2.bpmn20.xml'
curl.exe -i -u rest-admin:test -F "file=@$bpmn" "http://127.0.0.1:8080/flowable-rest/service/repository/deployments"
```

Si devuelve **HTTP 201**, el modelo se cargó. Haz este despliegue una vez por ambiente o cuando necesites cargar una versión nueva del BPMN.

### 2. Levantar el servicio y abrir el formulario

En la ventana donde cargaste el BPMN, inicia el servicio con las credenciales de prueba del Flowable local.

```powershell
Set-Location -LiteralPath (Join-Path $entrega 'servicio-peluditos')
$env:FLOWABLE_REST_URL = 'http://127.0.0.1:8080/flowable-rest/service'
$env:FLOWABLE_USER = 'rest-admin'
$env:FLOWABLE_PASSWORD = 'test'
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

Deja el servicio abierto y entra a [Registrar una consulta](http://localhost:7070/interfaz/index.html). Usa datos ficticios, por ejemplo **Cliente Prueba Web**, teléfono `000000000`, correo `cliente.prueba@example.com`, canal **WhatsApp** y mascota **Nube**. En la consulta puedes escribir «Preguntó por el precio de una torta de cumpleaños para su perrita».

Pulsa **Revisar datos**, verifica la vista previa y luego pulsa **Enviar registro**. Debe aparecer el ID del caso y el mensaje de que el guardado queda pendiente del worker.

### 3. Ejecutar el worker y comprobar el guardado

Abre una tercera ventana. Ingresa nuevamente la ruta completa de `Entrega-2` y ejecuta el worker después de enviar el formulario.

```powershell
$entrega = (Read-Host 'Ruta completa de la carpeta Entrega-2').Trim('"')
Set-Location -LiteralPath (Join-Path $entrega 'external-worker-peluditos')
$env:FLOWABLE_API_URL = 'http://127.0.0.1:8080/flowable-rest/external-job-api'
$env:FLOWABLE_USER = 'rest-admin'
$env:FLOWABLE_PASSWORD = 'test'
$env:SERVICIO_PELUDITOS_URL = 'http://localhost:7070/api/clientes'
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

Si solo enviaste un registro y la cola estaba vacía, la consola debe mostrar **Trabajos recibidos: 1** y **Trabajo completado en Flowable**. El worker consulta una vez y termina. Si lo ejecutaste antes de enviar el formulario, vuelve a ejecutarlo después del envío.

Abre [la lista de clientes](http://localhost:7070/api/clientes) y busca al cliente de prueba junto con su mascota. Para comprobar la persistencia, reinicia solo el servicio Java desde la misma carpeta y vuelve a abrir la lista. El registro debe seguir ahí.

Si el servicio arrancó en el puerto `7071`, usa ese puerto en las direcciones del formulario y de clientes, y en `SERVICIO_PELUDITOS_URL`.

### Evidencia de la prueba

Guarda una captura de la confirmación del formulario, una de la salida del worker y una del registro recuperado en `/api/clientes`. Las evidencias del equipo se guardan en `Entrega-2/Evidencias/Implementacion/`.
