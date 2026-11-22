# Implementación del sistema - Entrega 3

**Proyecto:** Peluditos Pet Chile  
**Asignatura:** Integración de Plataformas - IPLACEX  
**Grupo:** 3  
**Responsable:** Geraldine González  
**Rol:** Implementadora

## 1. Objetivo

Durante la Entrega 3 trabajé en la implementación y las pruebas del sistema Peluditos Pet Chile, con el propósito de mejorar el registro de clientes, las consultas y el seguimiento comercial.

Mi trabajo se enfocó principalmente en organizar las interfaces web para facilitar su uso, mejorar los mensajes que reciben los usuarios y comprobar el funcionamiento de la integración entre los distintos componentes del sistema.

Para esto se utilizaron servicios desarrollados con Jakarta REST, el motor de procesos Flowable OSS, dos External Workers y bases de datos para almacenar la información.

## 2. Organización de las interfaces web

Una de las principales mejoras que realicé fue separar las pantallas según las funciones de cada usuario, ya que consideré importante que tanto los clientes como la emprendedora pudieran utilizar el sistema de una manera sencilla.

### 2.1. Interfaz de clientes

Se organizó un formulario para que los clientes puedan ingresar sus datos personales, registrar información de sus mascotas y enviar sus consultas.

El formulario permite revisar la información antes de enviarla, de manera que el cliente pueda comprobar que los datos ingresados sean correctos.

También se revisaron los campos obligatorios para evitar que el formulario se enviara con información incompleta.

Una vez enviada la consulta correctamente, el sistema muestra un mensaje de confirmación.

### 2.2. Interfaz de la emprendedora

Por otra parte, se organizó un panel destinado a la emprendedora, desde el cual puede acceder a las funciones de gestión de clientes.

Este panel permite consultar los clientes registrados y acceder al seguimiento comercial, separando estas actividades del formulario utilizado por los clientes.

Con esta modificación busqué que la emprendedora pudiera trabajar directamente con la información recibida, sin tener que utilizar las pantallas destinadas al registro de nuevas consultas.

### 2.3. Mejora en el seguimiento comercial

Otra de las mejoras que realicé fue modificar la forma en que la emprendedora registra el seguimiento de los clientes.

Anteriormente, el formulario solicitaba ingresar manualmente un identificador relacionado con Flowable. Consideré que esto podía complicar su uso, ya que la emprendedora no necesariamente conoce cómo funciona esta plataforma ni sus identificadores técnicos.

Para solucionar este problema, cambié ese campo por una lista desplegable que carga automáticamente los clientes registrados que se encuentran en seguimiento.

En esta lista se muestra el nombre del cliente, su mascota y su número de registro, facilitando su identificación.

Cuando la emprendedora selecciona un cliente, puede indicar si se realizó la compra y agregar las observaciones correspondientes.

El sistema utiliza internamente el identificador de Flowable asociado a ese cliente para enviar el resultado al servicio REST, sin necesidad de que la emprendedora lo escriba.

De esta manera, el seguimiento comercial resulta más sencillo para la persona que utiliza el sistema, mientras la comunicación con Flowable continúa funcionando internamente.

También se consideraron mensajes para informar cuando no existen clientes pendientes o cuando ocurre un problema al cargar la información.

## 3. Mejoras en los mensajes de la aplicación

Durante las primeras pruebas observé que, después de registrar una consulta, aparecía un mensaje que incluía información técnica relacionada con Flowable.

Consideré que este mensaje podía resultar confuso para los clientes, ya que no necesitan conocer los procesos internos del sistema.

Por este motivo, modifiqué el mensaje para que fuera más claro y fácil de entender.

Actualmente, cuando una consulta se envía correctamente, se muestra:

"Tu consulta fue recibida correctamente. Gracias por comunicarte con Peluditos Pet Chile."

Con esta mejora, el cliente recibe una confirmación sencilla, mientras la aplicación continúa realizando las operaciones internas necesarias.

También se utilizaron mensajes de estado en el seguimiento comercial para informar cuando se está guardando la información y cuando el resultado fue registrado correctamente.

## 4. Implementación del servicio Jakarta REST

El sistema utiliza un servicio desarrollado en Java con Jakarta REST para atender las solicitudes relacionadas con el registro de clientes, las consultas y el seguimiento comercial.

Durante las pruebas realizadas en mi computador, el servicio se ejecutó utilizando el puerto 7072.

Se comprobó que las solicitudes de consulta respondieran con HTTP 200 y que los registros realizados correctamente pudieran entregar una respuesta HTTP 201.

Además, se realizaron pruebas para revisar cómo respondía el servicio frente a solicitudes incorrectas.

En una prueba inicial se observó que el servicio aceptaba una solicitud con datos vacíos. Posteriormente repetí la prueba enviando un objeto JSON vacío (`{}`), obteniendo esta vez una respuesta HTTP 400 con el mensaje:

"El nombre del cliente es obligatorio".

Este resultado permitió comprobar que el servicio rechazaba correctamente esa solicitud incompleta en la versión ejecutada durante la última prueba.

También se comprobó que, al consultar un cliente inexistente, el servicio respondiera con HTTP 404.

## 5. Integración con Flowable OSS

Para la ejecución del proceso de negocio se utilizó Flowable OSS 7.2.0 y el modelo TO-BE de Peluditos Pet Chile.

Flowable permite administrar el recorrido del proceso y coordinar las tareas necesarias para realizar el registro y seguimiento comercial.

La integración permite que la aplicación web envíe solicitudes al servicio correspondiente y que Flowable gestione las tareas del proceso mediante los componentes externos.

Durante las pruebas se revisó que el motor estuviera funcionando, que el proceso se encontrara desplegado y que las tareas pudieran avanzar durante las ejecuciones realizadas.

## 6. Implementación de los External Workers

El proyecto utiliza dos External Workers encargados de comunicarse con Flowable y los servicios REST.

### 6.1. Worker 1: registro de clientes

El primer Worker se encarga de recibir las tareas relacionadas con el registro de clientes.

Cuando Flowable entrega un trabajo, el Worker envía la información correspondiente al servicio Jakarta REST.

Durante las pruebas pude observar que recibía trabajos, obtenía respuestas HTTP 201 y posteriormente informaba que las tareas habían sido completadas en Flowable.

### 6.2. Worker 2: seguimiento comercial

El segundo Worker se encarga de procesar las tareas relacionadas con la actualización del seguimiento comercial.

Cuando la emprendedora registra el resultado de la atención, el sistema envía esa información mediante el servicio REST y el proceso continúa su ejecución.

Durante las pruebas se observó que el Worker realizaba una solicitud PUT al servicio, obtenía HTTP 200 y confirmaba que el trabajo se había completado.

También se comprobó en la interfaz que los clientes utilizados durante las pruebas mostraran el estado de compra correspondiente.

## 7. Almacenamiento de la información

Para almacenar la información comercial se utilizó una base de datos SQLite.

En ella se mantiene la información relacionada con los clientes, las mascotas y el seguimiento comercial.

Por otra parte, Flowable OSS se configuró con una base de datos H2 persistente, utilizada para almacenar información correspondiente al funcionamiento del motor de procesos.

Durante las pruebas se revisó la configuración de H2 y la existencia de sus archivos.

Además, mediante las consultas REST y las pantallas del sistema, pude comprobar que los clientes registrados y sus estados de seguimiento se encontraban disponibles.

## 8. Pruebas de funcionamiento

Después de realizar las modificaciones, se efectuaron pruebas para comprobar que las funciones principales del sistema continuaran funcionando.

Entre las verificaciones realizadas se encuentran la compilación de los componentes con Maven, las respuestas de los servicios REST, las validaciones de los formularios y el funcionamiento del seguimiento comercial.

También se realizaron pruebas con Flowable OSS y los dos External Workers para comprobar que pudieran recibir y completar las tareas correspondientes.

Los resultados de estas verificaciones, junto con los errores encontrados durante el desarrollo, quedaron documentados en el informe QA.

## 9. Evidencias de implementación

Las capturas correspondientes a los formularios, el panel de la emprendedora, el funcionamiento de Jakarta REST y las demás actividades de implementación se encuentran en:

`Evidencias/Implementacion/Entrega-3/`

Las evidencias de las pruebas realizadas como QA se encuentran en:

`Evidencias/QA/Entrega-3/`

En esta carpeta se encuentran las capturas correspondientes a las pruebas QA de la Entrega 3, incluyendo la validación HTTP 400, que permitió comprobar el rechazo de una solicitud con datos vacíos.

El informe completo de las pruebas se encuentra en:

`Documentacion/Informe-Pruebas-QA-Entrega3.md`

## 10. Aspectos pendientes

Durante las pruebas fue posible comprobar el funcionamiento de los principales componentes del sistema. Sin embargo, todavía quedan algunos aspectos por revisar.

Entre ellos se encuentra completar la guía de ejecución para otro computador, ampliar las pruebas frente a errores de conexión y verificar el procedimiento de respaldo y recuperación de las bases de datos.

También se revisaron los archivos antiguos del servicio para evitar confusiones con la implementación actual. Se retiraron `RegistroFlowable.java` y el antiguo `App.java` de `servicio-peluditos`, conservando `RegistroFlowableJakarta.java`, `ServidorJakarta.java` y el `App.java` correspondiente a los External Workers.

Después de esta limpieza, ambos proyectos Java se compilaron nuevamente con Maven, obteniendo `BUILD SUCCESS`.

Con estos cambios quedó más ordenada la implementación actual, mientras que las demás verificaciones mencionadas se mantienen pendientes para completar la documentación del proyecto.

## 11. Conclusión

Durante la Entrega 3 pude realizar mejoras en la organización de las interfaces y en la forma en que los usuarios interactúan con el sistema Peluditos Pet Chile.

Considero que uno de los cambios más importantes fue simplificar el seguimiento comercial, ya que la emprendedora ahora puede seleccionar directamente a un cliente registrado, sin tener que ingresar manualmente un identificador de Flowable.

También mejoré los mensajes de confirmación para que fueran más fáciles de entender y participé en las verificaciones del funcionamiento de Jakarta REST, Flowable OSS y los External Workers.

Las pruebas realizadas permitieron comprobar el funcionamiento de estas mejoras en el entorno local y dejaron identificadas las actividades pendientes para continuar desarrollando el proyecto.
