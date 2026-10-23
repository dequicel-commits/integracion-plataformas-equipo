# Documentación técnica — Peluditos Pet Chile

**Asignatura:** Integración de Plataformas — IPLACEX\
**Grupo:** 3\
**Evaluación:** Entrega 1

## 1. ¿Qué realizamos?

Para esta primera entrega trabajamos con el proceso de seguimiento comercial de Peluditos Pet Chile.

Creamos un servicio REST sencillo en Java y lo conectamos con Flowable mediante un External Worker.

La idea es que Flowable llegue a una actividad, nuestro programa Java consulte el servicio REST y, si todo funciona correctamente, el proceso pueda continuar.

Este trabajo es una primera prueba de integración. Todavía no corresponde a un sistema completo para gestionar clientes o ventas.

## 2. Programas y tecnologías utilizadas

| Programa o tecnología | Para qué lo utilizamos                          |
| --------------------- | ----------------------------------------------- |
| Visual Studio Code    | Escribir y revisar el código Java               |
| Java 21               | Ejecutar los programas                          |
| Maven                 | Compilar y ejecutar los proyectos Java          |
| Javalin 6.7.0         | Crear el servicio REST y la ruta de seguimiento |
| Flowable Enterprise   | Probar el proceso TO-BE y sus formularios       |
| Flowable OSS 7.2      | Probar la conexión con el External Worker       |
| Apache Tomcat 10      | Ejecutar Flowable REST en nuestro computador    |
| GitHub                | Guardar y compartir los archivos del proyecto   |

## 3. Servicio REST Java implementado

Creamos un servicio de prueba para el seguimiento comercial.

**Nombre:** Seguimiento comercial\
**Método:** GET\
**Ruta:** `/api/seguimiento`\
**Dirección local:** `http://localhost:7070/api/seguimiento`

**Propósito:** comprobar que el servicio Java pueda ser consultado durante la integración con el External Worker.

**Datos de entrada:** para esta prueba no necesita recibir información del cliente.

**Datos de salida:** devuelve un mensaje en formato JSON indicando que el servicio está funcionando.

Cuando se consulta, responde:
```
{
  "estado": "OK",
  "mensaje": "Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"
}
```

Este mensaje nos permite comprobar que el servicio responde correctamente.

Por ahora no guarda datos reales de clientes ni registra compras. Su función en esta primera entrega es demostrar la conexión entre un servicio Java, el External Worker y el proceso BPMN.

## 4. Conexión con Flowable

En el diagrama BPMN agregamos una actividad llamada **Consultar servicio REST Java**.

Esta actividad utiliza un External Worker con el tema:

`seguimiento-peluditos`

El funcionamiento es el siguiente:

1. Flowable llega a la actividad de integración.
2. El programa Java recibe el trabajo pendiente.
3. El programa consulta el servicio REST.
4. El servicio responde indicando que está funcionando.
5. Si la respuesta es correcta, Java completa el trabajo.
6. Flowable continúa con el proceso.

De esta manera pudimos comprobar la comunicación entre el proceso BPMN y un servicio desarrollado fuera de Flowable.

## 5. Cómo ejecutar el proyecto

Para realizar la prueba se necesita Java 21, Maven y Flowable OSS con su API REST funcionando.

### Servicio REST

Desde la carpeta:

`servicio-peluditos`

Ejecutar:
```
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

Después se puede comprobar el servicio desde el navegador:

`http://localhost:7070/api/seguimiento`

### External Worker

Desde la carpeta:

`external-worker-peluditos`

Configurar la conexión con Flowable OSS:
```
$env:FLOWABLE_API_URL = "http://localhost:8080/flowable-rest/external-job-api"
$env:FLOWABLE_USER = "rest-admin"
$env:FLOWABLE_PASSWORD = [System.Net.NetworkCredential]::new("", (Read-Host "Contraseña local de Flowable" -AsSecureString)).Password
```

Cada persona debe utilizar las credenciales autorizadas de su propia instalación.

Con el BPMN desplegado y una instancia esperando en la actividad External Worker, ejecutar:
```
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

En esta versión, el Worker consulta los trabajos una vez por ejecución.

## 6. Resultado de nuestra prueba

Durante la prueba obtuvimos el siguiente resultado:
```
Trabajos recibidos: 1
Respuesta del servicio: {"estado":"OK","mensaje":"Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"}
Trabajo completado en Flowable.
BUILD SUCCESS
```

Esto nos permitió comprobar que el External Worker recibió un trabajo, consultó el servicio REST y lo completó correctamente.

Después revisamos Flowable OSS y comprobamos que el proceso continuó y que la instancia terminó.

Para esta prueba utilizamos la ruta de compra realizada, con la variable:

`compraRealizada = true`

También probamos el formulario de seguimiento comercial en Flowable Enterprise.

## 7. Problema encontrado

Al comienzo intentamos utilizar el External Worker en Flowable Cloud Sandbox de IPLACEX.

El proceso llegaba hasta la actividad correspondiente, pero los trabajos del External Worker no se generaban.

Consultamos al profesor y nos indicó que era un problema conocido del entorno Sandbox.

Por esta razón realizamos la prueba de integración con Flowable OSS de forma local.

El proceso TO-BE y sus formularios también se mantuvieron y probaron en Flowable Enterprise.

## 8. Archivos de la implementación

Dentro del repositorio GitHub del equipo, en la carpeta `Entrega-1`, se encuentran:

- `servicio-peluditos`: código Java del servicio REST.
- `external-worker-peluditos`: código Java que conecta Flowable con el servicio.
- `bpmn-oss`: archivo BPMN adaptado para realizar las pruebas locales.
- `Evidencias/Implementacion`: capturas de las pruebas realizadas.
- `GUIA-PARA-TESTER.md`: información para el compañero encargado de QA.

El BPMN utilizado para la integración es una versión adaptada del modelo TO-BE y se guardó por separado para conservar el trabajo original del equipo.

## 9. Pruebas del Tester

La implementación queda disponible para que el encargado de QA / Tester realice las pruebas correspondientes sobre el servicio REST, el External Worker y su integración con Flowable.

Durante el desarrollo comprobamos que el servicio respondía correctamente, que el External Worker recibía el trabajo y que Flowable podía continuar hasta finalizar el proceso.

Los resultados de nuestras pruebas se encuentran en:

`Evidencias/Implementacion`

Si durante las pruebas se encuentra algún problema con el servicio REST, el External Worker o la conexión con Flowable, revisaremos la implementación y realizaremos las correcciones necesarias.

## 10. Servicios considerados para próximas entregas

La pauta solicita identificar algunos servicios que podrían ser necesarios para continuar desarrollando la solución.

A partir del proceso trabajado en esta primera entrega, se proponen inicialmente los siguientes servicios.

Estas propuestas todavía deben ser revisadas por el equipo y pueden cambiar en las próximas entregas.

### 10.1. Registro de un potencial cliente

**Nombre propuesto:** Registro de cliente\
**Método:** POST\
**Ruta propuesta:** `/api/clientes`

**Propósito:** permitir registrar los datos básicos de una persona interesada en los productos de Peluditos Pet Chile.

**Datos que podría recibir:**

- Nombre del cliente.
- Correo electrónico.
- Teléfono de contacto.

**Lo que podría devolver:**

- Identificador del cliente registrado.
- Mensaje indicando si el registro se realizó correctamente.

**Relación con el proceso:** podría utilizarse en la actividad **Registro de datos potencial cliente**.

### 10.2. Registro del seguimiento comercial

**Nombre propuesto:** Registro de seguimiento\
**Método:** POST\
**Ruta propuesta:** `/api/seguimientos`

**Propósito:** permitir guardar el resultado del seguimiento realizado a un potencial cliente.

**Datos que podría recibir:**

- Identificador del cliente.
- Fecha del seguimiento.
- Resultado del contacto.
- Observaciones.

**Lo que podría devolver:**

- Identificador del seguimiento.
- Mensaje de confirmación.

**Relación con el proceso:** podría relacionarse con la actividad **Realizar seguimiento comercial** del BPMN.

### Observación sobre estos servicios

Estos dos servicios son solamente una propuesta inicial basada en el proceso trabajado hasta ahora.

El equipo deberá revisarlos y decidir si serán necesarios en las próximas entregas.

En esta primera entrega únicamente se implementó y probó el servicio:

`GET /api/seguimiento`

Los otros servicios todavía no están desarrollados y sus métodos, rutas y datos pueden cambiar según las decisiones del equipo.

## 11. Próximas mejoras

Este trabajo corresponde a un prototipo inicial.

En las siguientes entregas se podrán incorporar nuevos servicios, mejorar el manejo de la información de clientes y desarrollar una interfaz propia.

La idea es continuar utilizando lo aprendido sobre Java, servicios REST, BPMN y Flowable para seguir avanzando en la solución de Peluditos Pet Chile.
