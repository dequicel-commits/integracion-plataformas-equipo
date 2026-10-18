# Primera entrega — Análisis de Peluditos Pet Chile y actas del equipo

**Asignatura:** Integración de Plataformas  
**Profesor:** Santiago Neira  
**Fecha de actualización del análisis:** 18 de octubre de 2026  
**Estado:** Propuesta inicial para revisar con el equipo.

## 1. Integrantes y roles

Actualizamos la distribución a partir del 17 de octubre mediante whatsapp. Elvis confirmó que se encargará del modelado BPMN y Geraldine se ofreció para implementar el proceso en Flowable. Falta dejar registrada la confirmación del equipo sobre esta última propuesta. Los roles de Felipe y Lautaro se mantienen como estaban informados.

| Integrante | Rol |
| --- | --- |
| Felipe Octavio Quicel Soto | Analista de procesos |
| Geraldine Andrea González Acuña | Implementadora Flowable |
| Elvis Brayan Gutiérrez Bernal | Modelador BPMN |
| Lautaro Nelson Salinas Alvear | Tester y encargado del repositorio |

## 2. Qué problema vamos a trabajar

Peluditos Pet Chile es un emprendimiento que elabora y vende pastelería para mascotas, principalmente perros y gatos. Recibe consultas por redes sociales y mensajería, pero no cuenta con un mecanismo formal para registrar a las personas interesadas y hacer seguimiento de cada consulta.

El caso plantea dos necesidades:

**Gestionar potenciales clientes o leads:** guardar sus datos, identificar el canal por el que llegaron, registrar los seguimientos y saber si finalmente compraron.
**Generar oportunidades de recompra:** aprovechar fechas como el cumpleaños de una mascota, Navidad u otras campañas para volver a contactar a los clientes.

Para empezar, proponemos trabajar con el registro y seguimiento de leads. Así podemos probar un proceso pequeño en Flowable y mejorarlo en las siguientes entregas. La gestión de fechas y oportunidades de recompra seguirá formando parte del proyecto.

Esta prioridad debe acordarse con el equipo. No significa que el profesor haya pedido trabajar solamente con leads.

### Qué buscamos mejorar

Queremos que cada consulta quede registrada y que la persona encargada pueda revisar qué información tiene, qué seguimiento se realizó y cuál fue el resultado. Esto ayudaría a que las consultas no dependan solamente de revisar conversaciones en distintos canales.

### Participantes del proceso

- **Persona interesada:** consulta por un producto y puede convertirse en cliente si realiza una compra.
- **Persona encargada de la atención:** recibe la consulta, responde y realiza el seguimiento. Falta confirmar cómo se distribuyen estas funciones en el emprendimiento.

Flowable será la herramienta para ejecutar el proceso propuesto. No asumimos que existe un supervisor ni una etapa de aprobación, porque el caso no los menciona.

## 3. Cómo se trabaja actualmente — AS-IS

El AS-IS representa el proceso actual. El caso confirma lo siguiente:

Las consultas llegan desde distintos canales digitales.
Algunas personas compran y otras solamente piden información.
No existe un mecanismo formal para registrar y seguir a los interesados.
No hay una forma sistemática de identificar oportunidades de recompra.

El documento no explica todos los pasos de atención. Por eso, este recorrido es una base para conversar con la emprendedora y preparar el primer diagrama; todavía debe validarse.

| Paso | Participante | Descripción inicial |
| --- | --- | --- |
| 1 | Persona interesada | Envía una consulta por un canal digital. |
| 2 | Persona encargada | Atiende la consulta. El detalle de la atención está pendiente de confirmar. |
| 3 | Persona interesada y encargada | La consulta puede terminar en una compra o quedar sin concretarse. Falta conocer cómo se identifica y registra ese resultado. |

**Recorrido de referencia:** consulta recibida → atender consulta → finalizar con compra o sin compra.

No sabemos todavía si utilizan planillas, una agenda u otro registro de apoyo. Tampoco conocemos los plazos de respuesta ni la cantidad de intentos de contacto. Estos puntos quedan como preguntas pendientes.

**Para el diagrama BPMN:** la persona interesada puede representarse como un participante externo en un pool separado. Las tareas de atención pertenecen al proceso del emprendimiento. Si se conectan ambos pools, se usan flujos de mensaje; dentro de cada proceso se usan flujos de secuencia.

### Revisión del borrador compartido

El esquema compartido muestra una consulta, su respuesta y una decisión sobre si la persona quiere comprar. Si la respuesta es sí, se coordina el pedido; si es no, se finaliza la consulta. Como Elvis comentó que después haría el TO-BE con las mejoras, entendemos que esta imagen sería el borrador del AS-IS. Falta confirmar esa identificación con él.

Como analista, propongo revisar estos puntos con Elvis:

Cambiar “Ingresa consulta de cliente” por “Recibir consulta del cliente”, si la actividad representa la recepción y no un registro que ya exista en la empresa.
Aclarar la tarea “Finalizar consulta seguimiento”. Si representa el cierre de la atención, puede llamarse “Finalizar consulta”. Todavía debemos confirmar si hoy realizan algún seguimiento informal.
Mantener “Pedido coordinado” como resultado de esa rama. Coordinar un pedido no demuestra por sí solo que la compra se haya concretado; falta definir qué confirma la compra.
En el TO-BE, incorporar el registro del interesado, las acciones de seguimiento y el resultado. Si alguien no compra en el primer contacto, debemos definir si sigue pendiente o corresponde cerrar su consulta.

La imagen es evidencia de un avance de modelado. También se necesita guardar el archivo editable `.bpmn` y, cuando corresponda, la evidencia de ejecución en Flowable.

## 4. Cómo proponemos trabajar — TO-BE preliminar

En el TO-BE proponemos que la consulta se registre manualmente en Flowable. Después, la persona encargada podrá dejar anotado el seguimiento y su resultado.

| Paso | Actividad propuesta | Qué debería quedar registrado |
| --- | --- | --- |
| 1 | Registrar consulta | Datos del interesado, contacto, canal y motivo de la consulta. |
| 2 | Realizar seguimiento | Fecha, acción realizada y una observación breve. |
| 3 | Registrar resultado | Si compró, no concretó o todavía sigue pendiente. |
| 4 | Continuar o cerrar | Si sigue pendiente, volver al seguimiento. Si tiene un resultado definitivo, cerrar el proceso. |

**Recorrido propuesto:** inicio → registrar consulta → realizar seguimiento → registrar resultado → decidir si continúa o finaliza.

Para el diagrama preliminar, la decisión puede representarse con una compuerta exclusiva. La opción “Pendiente” vuelve al seguimiento; “Compró” y “No concretó” llevan al cierre. Los criterios para cerrar una consulta por falta de respuesta deben validarse.

Este recorrido muestra la mejora que queremos desarrollar. La primera versión ejecutable será más pequeña, como se indica a continuación.

## 5. Proceso mínimo para la primera entrega

La planificación muestra como mínimo un proceso con inicio, una tarea humana y fin. Proponemos comenzar con lo siguiente:

**Nombre del proceso:** Registro de consultas de Peluditos.
**Clave propuesta:** `registro_consultas_peluditos`.
**Flujo:** inicio → tarea humana “Registrar consulta” → fin.
**Plataforma del curso:** [Flowable](https://iplacex.cloud.flowable.com/).

La persona encargada ingresará los datos manualmente. Elegir Instagram como canal de origen no significa que el sistema esté conectado con esa red social.

### Campos propuestos para la tarea humana

| Campo | Variable propuesta | Tipo y validación propuesta |
| --- | --- | --- |
| Nombre del interesado | `nombreInteresado` | Texto obligatorio. |
| Medio de contacto | `medioContacto` | Lista o texto obligatorio: usuario de red social, teléfono o correo. |
| Dato de contacto | `datoContacto` | Texto obligatorio según el medio elegido. |
| Canal de origen | `canalOrigen` | Lista obligatoria: Instagram, Facebook, TikTok, WhatsApp u Otro. |
| Motivo de la consulta | `motivoConsulta` | Texto obligatorio. |
| Observaciones | `observaciones` | Texto opcional. |

Estos nombres y validaciones son una propuesta para coordinar el formulario con el implementador. No son campos exigidos literalmente por la pauta. No proponemos pedir RUT, porque no se ha identificado una necesidad de usarlo.

La fecha de registro puede obtenerse de la instancia del proceso. Si se necesita una variable aparte para mostrarla o utilizarla después, el equipo deberá configurarla.

### Cómo revisaremos esta primera versión

Cuando esté implementada, comprobaremos que se pueda:

1. Iniciar una instancia del proceso.
2. Acceder a la tarea “Registrar consulta”.
3. Ingresar los datos de una persona ficticia y aplicar las validaciones acordadas.
4. Completar la tarea y llegar al evento de fin.
5. Revisar los datos guardados según la configuración realizada.

Guardaremos capturas o un video de esa prueba. Por ahora, estos puntos son criterios de revisión; no representan pruebas ya ejecutadas.

### Qué queda para ampliar después

Agregar las tareas de seguimiento y registro del resultado.
Definir cuándo una consulta se cierra sin compra.
Relacionar a quienes compraron con las futuras oportunidades comerciales.
Incorporar servicios web en Java, según la planificación del segundo entregable.
Desarrollar la gestión de fechas y oportunidades de recompra.

## 6. Preguntas que debemos resolver

1. ¿Quién recibe las consultas y quién realiza el seguimiento?
2. ¿Dónde guardan actualmente los datos de las personas interesadas?
3. ¿Qué información necesitan para volver a contactar a alguien?
4. ¿Cuánto esperan antes de realizar un nuevo contacto?
5. ¿Cuándo consideran que una consulta terminó en compra?
6. ¿Cuándo cierran una consulta que no tuvo respuesta?
7. ¿Cómo reconocen a una persona que consulta varias veces o por distintos canales?
8. ¿Qué fechas desean administrar y con cuánta anticipación quieren preparar el contacto?

Las respuestas se agregarán cuando se obtengan. Mientras tanto, los detalles no confirmados se mantendrán como propuestas o dudas.

## 7. Aportes y pendientes del analista

Mi trabajo como analista consiste en ordenar las necesidades del caso, describir el proceso y revisar que lo modelado corresponda a lo que queremos resolver.

Con esta actualización, el documento queda enfocado en Peluditos Pet Chile e incluye una propuesta de alcance, la descripción inicial del AS-IS y TO-BE, los campos del proceso mínimo y las preguntas pendientes.

Todavía corresponde:

Revisar y acordar el alcance con el equipo.
Validar los detalles del proceso actual.
Revisar con Elvis los diagramas AS-IS y TO-BE y registrar las observaciones del análisis.
Coordinar con Geraldine los campos del formulario y el proceso mínimo, si el equipo confirma su propuesta de asumir Flowable.
Participar en la revisión del prototipo y registrar las observaciones.
Mantener las actas con lo que realmente se converse y acuerde.

Los diagramas BPMN y la ejecución en Flowable son entregables aparte. Este documento sirve de base, pero no los reemplaza.

Cada integrante debe registrar sus avances desde su cuenta de GitHub. En el caso del analista, pueden ser cambios en el análisis, respuestas a preguntas, ajustes de requisitos y observaciones de revisión. Las actas también deben indicar quién se compromete con cada tarea.

## 8. Actas de reuniones

Las actas se completan con las reuniones que efectivamente se realicen. La planificación pide al menos dos reuniones semanales y revisar los compromisos de la reunión anterior.

### Acta 1 — 14 de octubre de 2026

Este registro conserva lo informado en el documento original. La referencia a la empresa pendiente corresponde a esa fecha; el análisis de las secciones anteriores se actualizó el 18 de octubre con el caso de Peluditos Pet Chile.

**Modalidad registrada:** online / coordinación por grupo.

**Participantes registrados:**

- Felipe Octavio Quicel Soto.
- Geraldine Andrea González Acuña.
- Elvis Brayan Gutiérrez Bernal.
- Lautaro Nelson Salinas Alvear.

**Avances registrados:**

- Revisamos la planificación del curso y las fechas de las entregas.
- Repartimos los primeros roles para las semanas 3 y 4.
- Se informó la creación del repositorio del grupo para guardar avances y actas.
- Felipe avanzó en una estructura inicial del análisis, con el proceso actual, la propuesta de mejora y los campos de una tarea.

**Compromisos registrados en esa reunión:**

| Tarea | Responsable | Fecha estimada | Estado registrado entonces |
| --- | --- | --- | --- |
| Enviar los integrantes al profesor y consultar por el caso. | Grupo | 15-10-2026 | Completo. |
| Completar el análisis con los datos de la empresa. | Felipe Quicel | 19-10-2026 | Estructura preparada, pendiente. |
| Preparar borradores de los diagramas AS-IS y TO-BE. | Geraldine González | 20-10-2026 | Pendiente. |
| Probar el flujo básico en Flowable. | Elvis Gutiérrez | 20-10-2026 | Pendiente. |
| Ordenar las carpetas y revisar los archivos del repositorio. | Lautaro Salinas | 21-10-2026 | Pendiente. |

**Bloqueo registrado:** faltaba contar con el caso de la empresa para ajustar el análisis. Mientras tanto, se había preparado una estructura general de trabajo.

Los estados anteriores describen lo registrado en el acta original. Su avance se revisará.

### Coordinación por WhatsApp — 17 de octubre de 2026

Se registra el intercambio compartido del grupo.

**19:55, Elvis:** indicó que después realizaría el modelo TO-BE con las mejoras.
**20:11, Geraldine:** se ofreció para asumir la implementación en Flowable y hacer funcionar y probar el proceso cuando estuviera listo el TO-BE. Consultó al grupo si estaba de acuerdo.
**20:27, Elvis:** confirmó que se encargaría del modelado BPMN.

**Seguimiento pendiente:** confirmar en el grupo la propuesta de Geraldine y actualizar los responsables de las tareas futuras. La distribución del acta del 14 de octubre se conserva como antecedente de esa fecha.

### Acta 2 — Plantilla para completar después de la reunión

**Fecha y hora:** por completar.  
**Modalidad:** por completar.  
**Participantes que asistieron:** por completar.

**Revisión de compromisos anteriores:**

Envío de integrantes al profesor: por revisar.
Actualización del análisis para Peluditos: por revisar con el equipo.
Borradores BPMN: por revisar.
Prueba del proceso mínimo en Flowable: por revisar.
Organización y acceso al repositorio: por revisar.

**Avances conversados:**

Por completar con lo revisado durante la reunión.

**Nuevos acuerdos:**

| Tarea acordada | Responsable | Fecha comprometida | Estado |
| --- | --- | --- | --- |
| Por completar. | Por completar. | Por completar. | Por completar. |

**Bloqueos y dudas:**

Por completar con las dificultades identificadas o indicar que no se reportaron.

## 9. Documentos de referencia

**PeluditosPetChile.pdf:** contexto del emprendimiento, gestión de leads y oportunidades comerciales.
**planificacion-final.pdf:** requisitos de la primera entrega, proceso mínimo, reuniones y evidencias del equipo.
**Anuncio Semana 1 de Integración de Plataformas:** uso de GitHub, roles y calendario del curso.

El anuncio sitúa la evaluación de la Unidad 1 en la semana del 19 al 25 de octubre de 2026. El equipo debe revisar la actividad de entrega para conocer la hora de cierre y la forma de presentación.
