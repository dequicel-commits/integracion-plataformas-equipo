# Documentación técnica — Peluditos Pet Chile

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Evaluación:** Entrega 1

## 1. ¿Qué realizamos?

Para esta primera entrega trabajamos con un proceso de seguimiento comercial de Peluditos Pet Chile.

Creamos un servicio REST sencillo en Java y lo conectamos con Flowable mediante un External Worker.

La idea es que Flowable pueda solicitar un trabajo, nuestro programa Java consulte el servicio y después el proceso continúe.

## 2. Programas utilizados

| Programa | Para qué lo utilizamos |
|---|---|
| Visual Studio Code | Escribir el código Java |
| Java 21 | Ejecutar los programas |
| Maven | Compilar y ejecutar los proyectos Java |
| Flowable Enterprise | Probar el proceso TO-BE y sus formularios |
| Flowable OSS 7.2 | Probar la conexión con el External Worker |
| Apache Tomcat 10 | Ejecutar Flowable REST en nuestro computador |
| GitHub | Compartir los archivos del proyecto |

## 3. Servicio REST Java

Creamos un servicio de prueba para el seguimiento comercial.

**Método:** GET  
**Dirección:** `http://localhost:7070/api/seguimiento`

No necesita recibir datos del usuario.

Cuando se consulta, responde:

```json
{
  "estado": "OK",
  "mensaje": "Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"
}
```

Este mensaje permite comprobar que el servicio funciona. Todavía no guarda información real de clientes.

## 4. Conexión con Flowable

Agregamos una actividad llamada **Consultar servicio REST Java** al BPMN.

Esta actividad utiliza un External Worker con el tema `seguimiento-peluditos`.

El funcionamiento es sencillo:

1. Flowable llega a la actividad de integración.
2. Nuestro programa Java recibe el trabajo.
3. Java consulta el servicio REST.
4. Si el servicio responde correctamente, Java completa el trabajo.
5. Flowable continúa hacia la decisión sobre la compra.

## 5. Cómo ejecutar el proyecto

Para probarlo se necesita Java 21, Maven y Flowable OSS con su API REST funcionando.

**Servicio REST**

Desde la carpeta `servicio-peluditos`, ejecutar:

```powershell
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

Después se puede comprobar en el navegador:

`http://localhost:7070/api/seguimiento`

**External Worker**

Desde la carpeta `external-worker-peluditos`, configurar la conexión con Flowable OSS:

```powershell
$env:FLOWABLE_API_URL = "http://localhost:8080/flowable-rest/external-job-api"
$env:FLOWABLE_USER = "rest-admin"
$env:FLOWABLE_PASSWORD = [System.Net.NetworkCredential]::new("", (Read-Host "Contraseña local de Flowable" -AsSecureString)).Password
```

Cada persona debe utilizar las credenciales autorizadas de su propia instalación.

Con el BPMN desplegado y una instancia esperando en el External Worker, ejecutar:

```powershell
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

El Worker consulta los trabajos una vez cada vez que se ejecuta.

## 6. Resultado de nuestra prueba

Durante la prueba obtuvimos:

```text
Trabajos recibidos: 1
Respuesta del servicio: {"estado":"OK","mensaje":"Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"}
Trabajo completado en Flowable.
BUILD SUCCESS
```

Después comprobamos que Flowable continuó por la ruta **SÍ, compra realizada**, y que la instancia terminó.

También probamos el formulario de seguimiento comercial en Flowable Enterprise.

## 7. Problema encontrado

Al principio intentamos utilizar el External Worker en Flowable Cloud Sandbox, pero no se generaban los trabajos.

Consultamos al profesor, quien confirmó que era un problema conocido de la plataforma.

Por eso realizamos la integración en Flowable OSS local. El proceso original con formularios se mantuvo en Flowable Enterprise.

## 8. Pruebas del Tester

El integrante encargado de las pruebas debe comprobar que el servicio REST responda, que el External Worker reciba un trabajo y que Flowable pueda continuar.

También debe registrar los resultados y los errores encontrados.

La ejecución completa que demostramos utilizó `compraRealizada = true`.

## 9. Próximas mejoras

Este trabajo corresponde a un prototipo inicial. En las siguientes entregas se podrán incorporar más funcionalidades, formularios propios y nuevos servicios según las necesidades de Peluditos Pet Chile.
