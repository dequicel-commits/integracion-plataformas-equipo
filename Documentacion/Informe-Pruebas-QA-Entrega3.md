# Informe de Pruebas QA - Entrega 3

**Proyecto:** Peluditos Pet Chile  
**Asignatura:** Integración de Plataformas - IPLACEX  
**Grupo:** 3  
**Responsable:** Geraldine González  
**Rol:** Implementadora y QA/Tester  

## 1. Objetivo de las pruebas

Durante la Entrega 3 realicé distintas pruebas para comprobar que los componentes del sistema Peluditos Pet Chile funcionaran correctamente y pudieran comunicarse entre sí.

Revisé el funcionamiento de la interfaz web, los servicios desarrollados con Jakarta REST, el motor de procesos Flowable OSS y los dos External Workers encargados de ejecutar las tareas del proceso.

También comprobé el registro de clientes y mascotas, el seguimiento comercial, las respuestas de los servicios y el almacenamiento de la información.

Durante el desarrollo se presentaron algunos errores que fueron revisados mediante nuevas pruebas. Además, identifiqué aspectos que todavía deben mejorarse.

Todas estas pruebas se realizaron en mi computador, utilizando el entorno local del proyecto.

## 2. Pruebas realizadas

### 2.1. Compilación de los componentes

Primero revisé que los proyectos `servicio-peluditos` y `external-worker-peluditos` compilaran correctamente utilizando Maven.

En ambos casos obtuve el mensaje `BUILD SUCCESS`, lo que permitió continuar con las pruebas de funcionamiento.

Aunque se ejecutaron las pruebas automáticas disponibles, también fue necesario realizar pruebas manuales para comprobar la integración de los componentes.

**Evidencias:**
- `QA_Compilacion_Jakarta_REST.png`
- `QA_Compilacion_External_Worker.png`

### 2.2. Pruebas de los servicios Jakarta REST

Realicé consultas a los servicios REST para revisar las respuestas entregadas por el sistema.

Al consultar el listado de clientes mediante una solicitud GET, obtuve HTTP 200, confirmando que el servicio respondió correctamente.

También consulté la ruta `/api/seguimiento`, la cual respondió HTTP 200 y entregó un mensaje indicando que el servicio Jakarta REST se encontraba funcionando.

Durante las pruebas de registro, observé respuestas HTTP 201 correspondientes a la creación de registros.

En una prueba inicial detecté que el servicio aceptaba una solicitud con datos vacíos. Posteriormente repetí la prueba enviando `{}` y esta vez obtuve HTTP 400 con el mensaje "El nombre del cliente es obligatorio".

De esta manera, pude comprobar que el servicio rechazó correctamente esa solicitud incompleta.

También se consultó un cliente inexistente, obteniendo HTTP 404. Esta respuesta fue la esperada, ya que el registro solicitado no existía.

**Evidencias:**
- `QA_API_Clientes_HTTP200.png`
- `QA_API_Clientes_HTTP201.png`
- `QA_API_Seguimiento_HTTP200.png`
- `QA_Validacion_API_HTTP400.png`

### 2.3. Pruebas de la interfaz web

Probé los formularios de registro de clientes y mascotas para revisar su funcionamiento.

Primero comprobé que los campos obligatorios solicitaran información antes de permitir el envío.

Después ingresé datos de prueba y confirmé que la aplicación mostrara un mensaje indicando que la consulta había sido recibida correctamente.

También revisé el panel de la emprendedora y las pantallas utilizadas para consultar clientes y realizar seguimiento comercial.

Durante la implementación se separaron las interfaces según las funciones de cada usuario. El cliente cuenta con un formulario para registrar sus datos y consultas, mientras que la emprendedora dispone de pantallas para consultar la información y realizar el seguimiento.

Además, se reemplazó un mensaje técnico relacionado con Flowable por una confirmación más sencilla para el usuario.

**Evidencias:**
- `QA_Validacion_Campos_Obligatorios.png`
- `QA_Consulta_Enviada_OK.png`
- `QA_Seguimiento_Cliente_Seleccionado.png`
- `QA_Seguimiento_Enviado_OK.png`

Las capturas que muestran las modificaciones realizadas en las pantallas también se encuentran en `Evidencias/Implementacion/Entrega-3/`.

### 2.4. Pruebas de Flowable OSS

Para revisar el funcionamiento del motor de procesos utilicé Flowable OSS 7.2.0.

Primero comprobé que el servicio estuviera funcionando y que el proceso TO-BE de Peluditos Pet Chile se encontrara desplegado.

Posteriormente realicé pruebas de ejecución para verificar que el proceso avanzara y que los External Workers recibieran las tareas correspondientes.

Estas pruebas permitieron comprobar la participación de Flowable en la integración del sistema.

**Evidencias:**
- `QA_Flowable_OSS_Motor_Activo.png`
- `QA_Flowable_Procesos_Desplegados.png`
- `QA_Flowable_Proceso_Ejecutado.png`

### 2.5. Pruebas de los External Workers

El sistema utiliza dos External Workers que se comunican con Flowable OSS y el servicio Jakarta REST.

**Worker 1: registro de clientes**

Durante las pruebas observé que el Worker recibía trabajos desde Flowable y enviaba solicitudes al servicio REST.

En la consola se mostraron respuestas HTTP 201 y mensajes confirmando que los trabajos se habían completado en Flowable.

**Worker 2: actualización del seguimiento**

También probé el Worker encargado de actualizar el seguimiento comercial.

En la consola observé una solicitud PUT al servicio REST, una respuesta HTTP 200 y la confirmación de que el trabajo se completó en Flowable.

Posteriormente revisé que los clientes utilizados durante las pruebas mostraran el estado correspondiente a una compra realizada.

**Evidencias:**
- `QA_Worker1_Registro_Exitoso.png`
- `QA_Worker2_Compra_Realizada.png`
- `QA_Cliente7_Compra_Realizada.png`
- `QA_Cliente8_Compra_Realizada.png`

### 2.6. Pruebas de persistencia de datos

También revisé el almacenamiento de la información utilizada por el sistema.

Para los datos comerciales se utilizó SQLite, mientras que Flowable OSS se configuró con una base de datos H2 persistente.

Comprobé la existencia de los archivos de H2 y revisé su configuración.

Además, mediante las consultas REST y las pantallas del sistema, pude verificar que los clientes registrados y sus estados de seguimiento estaban disponibles para su consulta.

Estas pruebas permitieron revisar el almacenamiento de información en el entorno local, aunque todavía falta comprobar su recuperación en otro computador.

**Evidencias:**
- `QA_Flowable_Configuracion_H2.png`
- `QA_Flowable_Archivos_BD_H2.png`
- `QA_API_Clientes_HTTP200.png`
- `QA_Cliente8_Compra_Realizada.png`

## 3. Errores encontrados y pruebas posteriores

Durante el desarrollo aparecieron algunos errores que dificultaron temporalmente la ejecución de determinados componentes.

### 3.1. Error durante la ejecución de Jakarta REST

En una de las pruebas apareció un error al ejecutar el servicio Jakarta REST.

Posteriormente, el servicio pudo iniciarse y respondió correctamente a las solicitudes realizadas, obteniendo HTTP 200.

Esto permitió continuar con las pruebas, aunque no se identificó de manera definitiva la causa del error inicial.

**Evidencias:**
- `QA_Error_Jakarta_REST.png`
- `QA_API_Seguimiento_HTTP200.png`

### 3.2. Error al confirmar registros en Flowable

Durante una prueba, la interfaz mostró un mensaje indicando que no se había podido confirmar el registro en Flowable.

Después se realizaron nuevas pruebas y fue posible enviar consultas, visualizar la confirmación y comprobar la ejecución de tareas relacionadas con el proceso.

Por lo tanto, el error inicial no volvió a impedir las operaciones realizadas durante esas pruebas.

**Evidencias:**
- `QA_Error_Registro_Flowable.png`
- `QA_Consulta_Enviada_OK.png`

### 3.3. Error durante la ejecución del Worker 1

Durante una de las ejecuciones apareció una excepción en el primer External Worker.

En las pruebas posteriores, el Worker recibió trabajos, obtuvo respuestas HTTP 201 y confirmó que las tareas habían sido completadas en Flowable.

De esta manera, pude comprobar que volvió a funcionar en los casos probados.

**Evidencias:**
- `QA_Error_Worker1.png`
- `QA_Worker1_Registro_Exitoso.png`

### 3.4. Validación de datos vacíos en la API

En una prueba inicial observé que el servicio REST aceptaba una solicitud de registro con datos vacíos y respondía HTTP 201.

Para comprobar si este comportamiento continuaba, repetí la prueba enviando un objeto JSON vacío (`{}`).

En esta última ejecución obtuve HTTP 400 con el mensaje "El nombre del cliente es obligatorio".

Con este resultado confirmé que el servicio actualmente rechaza esa solicitud incompleta en el entorno probado.

**Evidencias:**
- `QA_API_Clientes_HTTP201.png`
- `QA_Validacion_API_HTTP400.png`

## 4. Limitaciones conocidas y aspectos pendientes

Aunque las pruebas realizadas permitieron comprobar el funcionamiento de los principales componentes, todavía quedan algunos aspectos por revisar.

**Validaciones adicionales:** se comprobó que la API rechaza una solicitud con datos completamente vacíos. Sin embargo, sería conveniente ampliar las pruebas utilizando otros datos incorrectos o incompletos.

**Manejo de errores:** los errores iniciales no impidieron completar las pruebas posteriores, pero todavía es necesario comprobar el comportamiento del sistema frente a fallas de conexión o interrupciones de los servicios.

**Pruebas automáticas:** los componentes compilaron correctamente, pero faltan pruebas automáticas más completas que permitan verificar las principales funciones del sistema.

**Instalación en otro computador:** las pruebas se realizaron en un entorno local. Todavía falta comprobar el funcionamiento completo del sistema en otro equipo.

**Respaldo y recuperación:** se verificó el almacenamiento de información, pero todavía no se ha realizado una prueba completa de respaldo y recuperación de las bases de datos.

Estos aspectos se consideran pendientes para continuar mejorando el sistema en la siguiente etapa.

## 5. Conclusión

Las pruebas realizadas durante la Entrega 3 permitieron comprobar el funcionamiento de los principales componentes de Peluditos Pet Chile en el entorno local.

Pude verificar que los servicios Jakarta REST respondían a las solicitudes, que la interfaz permitía registrar consultas y realizar seguimiento comercial, y que Flowable OSS se comunicaba con los dos External Workers durante las ejecuciones realizadas.

También revisé el almacenamiento de información y comprobé que los registros y sus estados podían consultarse posteriormente.

Durante el desarrollo se presentaron algunos errores, pero las pruebas posteriores permitieron comprobar que las funciones involucradas podían ejecutarse correctamente.

Además, se verificó que el servicio REST rechazara una solicitud con datos vacíos mediante una respuesta HTTP 400, lo que permitió confirmar el funcionamiento de esa validación.

Considero que estas pruebas fueron importantes porque permitieron revisar la comunicación entre los distintos componentes, identificar dificultades y comprobar los avances realizados.

De todas maneras, todavía quedan aspectos pendientes, principalmente ampliar las pruebas de validación, comprobar el manejo de fallas y verificar la instalación del sistema en otro computador.

## 6. Ubicación de las evidencias

Las **22 capturas QA** utilizadas para documentar las pruebas de esta entrega se encuentran en:

`Evidencias/QA/Entrega-3/`

Las evidencias relacionadas con la implementación y las mejoras de la interfaz web se encuentran en:

`Evidencias/Implementacion/Entrega-3/`
