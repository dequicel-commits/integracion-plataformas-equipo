# Guía para el Tester — Peluditos Pet Chile (Entrega 2)

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Entrega:** Segunda evaluación

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
2. En Flowable, el proceso habrá avanzado automáticamente a **`Enviar información de productos y precios`** y luego a **`Realizar seguimiento comercial`** para finalizar el recorrido.
