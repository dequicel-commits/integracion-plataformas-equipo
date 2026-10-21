# Primera entrega - Análisis de Peluditos Pet Chile

**Asignatura:** Integración de Plataformas  
**Profesor:** Santiago Neira  
**Actualizado:** 21 de octubre de 2026  
**Entrega:** jueves 29 de octubre de 2026, según el plazo informado al actualizar este documento. La hora se revisará en la actividad.  
**Estado del trabajo:** análisis y modelos iniciales preparados; implementación, integración y evidencias en desarrollo.

## 1. Cómo nos organizamos

Nos estamos coordinando por WhatsApp. Cada uno tiene una parte principal, pero necesitamos revisar juntos que el análisis, los modelos y la implementación coincidan.

| Integrante | Trabajo principal en esta entrega |
| --- | --- |
| Felipe Octavio Quicel Soto | Análisis del proceso y organización del repositorio. |
| Elvis Brayan Gutiérrez Bernal | Modelos BPMN AS-IS y TO-BE. |
| Geraldine Andrea González Acuña | Implementación en Flowable. El 21 informó que también está avanzando en integración con Web Services. |
| Lautaro Nelson Salinas Alvear | Pruebas y registro de resultados. |

El 18 se conversó la distribución en el grupo. Elvis confirmó el modelado y Geraldine confirmó su rol de implementadora. Lautaro ha estado revisando el requerimiento y las pruebas.

Estamos usando Flowable de IPLACEX. Para el desarrollo se propuso Java 21, Maven y Visual Studio Code. Antes de ejecutar el código compartido debemos comprobar las versiones y anotar los pasos en el README.

Con la pauta nueva quedó claro que en esta primera entrega sí se necesita un servicio Java de prueba y su integración mediante un external worker. Ya no dejamos esa parte para la segunda entrega. Como Geraldine está trabajando en integración, falta revisar con ella qué tiene avanzado y cómo se repartirá lo que queda.

## 2. Qué problema queremos resolver

Peluditos Pet Chile hace y vende pastelería para mascotas, principalmente perros y gatos. Recibe consultas por redes sociales y mensajería, pero el caso indica que no existe un mecanismo formal para registrar a los interesados y dar seguimiento a sus consultas.

Hay dos necesidades principales:

- **Gestionar a los potenciales clientes o leads.** Saber quién consultó, por qué canal llegó, qué necesita, qué seguimiento se hizo y si finalmente compró.
- **Generar oportunidades de recompra.** Tener en cuenta fechas como el cumpleaños de una mascota o campañas de Navidad para volver a contactar a los clientes.

Para esta primera versión estamos trabajando con el registro y seguimiento de consultas. La parte de fechas sigue dentro del proyecto completo, pero no la presentamos como implementada.

Lautaro también propuso anotar el motivo de no compra cuando se conozca. Nos puede servir para entender mejor las consultas, pero no debemos inventar un motivo si la persona no lo indicó.

### Participantes

- **Persona interesada o cliente:** escribe para consultar por los productos.
- **Persona encargada de atención:** responde, coordina el pedido y realiza las acciones de seguimiento que correspondan.

Todavía necesitamos confirmar con el negocio cómo se reparten esas funciones. Para el BPMN, la persona interesada puede representarse como participante externo, separada del proceso interno del emprendimiento.

## 3. Proceso actual AS-IS

El caso confirma que llegan consultas por distintos canales, que algunas terminan en compra y que falta un registro formal de seguimiento. También indica que no existe una forma sistemática de identificar oportunidades comerciales por fechas.

No tenemos confirmado si utilizan un cuaderno, una planilla u otro apoyo. Por eso no afirmamos que no exista ningún registro. El detalle de cómo responden y qué hacen cuando una persona deja de contestar también queda pendiente de validar.

El borrador que compartió Elvis representa este recorrido:

| Paso | Participante | Qué representa el modelo |
| --- | --- | --- |
| 1 | Persona interesada | Realiza una consulta. |
| 2 | Persona encargada | Recibe y responde la consulta. |
| 3 | Persona interesada y encargada | Se determina si quiere hacer un pedido. |
| 4 | Persona encargada | Coordina el pedido o finaliza la atención. |

**Recorrido de referencia:** consulta recibida -> responder consulta -> ¿quiere comprar? -> coordinar pedido o finalizar consulta.

Este es nuestro modelo inicial del proceso. Los detalles que no están explicados en el caso se mantienen como supuestos por revisar. Además, un pedido coordinado no demuestra por sí solo una compra concretada; debemos aclarar qué condición la confirma.

### Avances del modelado

- El 17 Elvis compartió el primer AS-IS por WhatsApp.
- El 19 informó que los dos modelos estaban en Flowable.
- Ese mismo día avisó que tenía un error en el AS-IS y después indicó que estaba corregido.
- Geraldine revisó los dos caminos del TO-BE mediante simulación de tokens y señaló que ambos llegaban al final.

Falta comprobar que estén respaldados en el repositorio los archivos editables con los nombres que pide la pauta: `BPMN/AS-IS.bpmn` y `BPMN/TO-BE.bpmn`. Las capturas sirven de apoyo, pero no reemplazan esos archivos.

## 4. Proceso propuesto TO-BE

La mejora es que cada consulta tenga datos registrados, seguimiento y un resultado que podamos revisar después. Así no dependemos solamente de buscar conversaciones en distintas redes.

| Actividad | Mejora que buscamos |
| --- | --- |
| Registrar los datos del interesado. | Identificar a la persona, su contacto, el canal y lo que necesita. |
| Gestionar y responder la consulta. | Mantener la atención dentro de un recorrido conocido. |
| Realizar seguimiento comercial. | Saber qué acción se hizo y cuándo. |
| Registrar el resultado. | Distinguir si compró o si no concretó, anotando el motivo cuando se conozca. |

Según el registro de simulación incorporado por Lautaro, el modelo actual tiene una decisión con dos salidas: compra realizada y compra no realizada.

Hay un punto que debemos resolver con Elvis y Geraldine: una consulta todavía en seguimiento no debería cerrarse como si ya se hubiera descartado. La propuesta es mantenerla pendiente hasta poder registrar un resultado. Falta definir si eso se manejará dentro de la tarea de seguimiento o mediante otro recorrido y ajustar el modelo y los formularios de la misma forma.

No damos por implementada esa continuidad mientras no quede configurada y probada.

## 5. Qué debe funcionar en esta entrega

Al comienzo usamos como referencia `Inicio -> Tarea humana -> Fin` para probar lo básico. Con la pauta específica, eso queda como un primer paso y no como el alcance completo de la entrega.

Ahora debemos demostrar:

1. Un proceso basado en el TO-BE desplegado en Flowable Enterprise.
2. Formularios de Flowable Forms para recorrer las principales actividades.
3. Una instancia de prueba con sus decisiones e interacciones.
4. Un web service Java de prueba que responda una solicitud.
5. Un external worker integrado al proceso que invoque ese servicio y permita continuar la ejecución.

Podemos usar datos ficticios y una lógica simplificada, pero hay que mostrar lo que efectivamente funciona.

**Nombre propuesto del proceso:** Registro de consultas Peluditos Pet.  
**Clave propuesta:** `registro_consultas_peluditos`.  
**Plataforma del curso:** [Flowable de IPLACEX](https://iplacex.cloud.flowable.com/).

### Datos propuestos para los formularios

| Campo | Variable propuesta | Uso |
| --- | --- | --- |
| Nombre del interesado | `nombreInteresado` | Identificar la consulta. Se propone obligatorio. |
| Medio de contacto | `medioContacto` | Indicar si usaremos teléfono, usuario de red social o correo. |
| Dato de contacto | `datoContacto` | Guardar el número, usuario o correo. Se propone obligatorio. |
| Canal de origen | `canalOrigen` | Instagram, Facebook, TikTok, WhatsApp u Otro. |
| Motivo de consulta | `motivoConsulta` | Registrar qué producto o información necesita. Se propone obligatorio. |
| Estado de trabajo | `estadoLead` | Propuesta para diferenciar una consulta pendiente o en seguimiento de una cerrada. |
| Resultado final | `resultadoLead` | Compró o No concretó, cuando ya exista una decisión de cierre. |
| Observaciones | `observaciones` | Anotar seguimiento o motivo de no compra cuando se conozca. |

Esta tabla es una propuesta para revisar con Geraldine. No confirma que todos los campos ya estén configurados. El estado de trabajo se propone para resolver la diferencia entre los dos caminos actuales y las consultas que siguen pendientes.

Si necesitamos registrar el cumpleaños de la mascota en otra iteración, conviene definir un campo de fecha propio. Por ahora no lo consideramos una función implementada ni dependemos de buscar esa fecha dentro de observaciones.

### Servicios y arquitectura

Geraldine informó el 21 que tiene avanzada la integración con Web Services. Falta revisar con ella el servicio y el worker que está utilizando, cómo se ejecutan y qué evidencia podemos mostrar.

Debemos documentar la lista inicial de servicios previstos, indicando para cada operación:

- Nombre y propósito.
- Método y ruta si usamos REST, o servicio y operación si usamos SOAP.
- Datos que recibe y devuelve.
- Actividad del proceso con la que se relaciona.
- Estado: propuesto, desarrollado o probado.

No se exige implementar toda la lista, pero sí al menos un servicio Java de prueba integrado al proceso mediante el worker. Revisaremos las rutas con Geraldine para documentar las que realmente esté usando.

También falta comprobar el diagrama de arquitectura en `Arquitectura/`. Debe mostrar los componentes reales y su comunicación, y distinguir lo que todavía esté planificado. Para esta entrega basta un esquema claro con cajas y flechas.

## 6. Simulación del TO-BE registrada por Lautaro

Este apartado conserva el recorrido que Lautaro añadió al documento. Corresponde a la simulación del modelo; no lo presentamos todavía como evidencia de ejecución real con formularios, servicio Java y worker.

### Escenario A - Compra realizada

1. El recorrido comienza en `Cliente realiza una consulta`.
2. El token pasa por `Registro de datos potencial cliente`.
3. Continúa por `Gestionar y responder consulta`.
4. Pasa por `Realizar seguimiento comercial`.
5. En `¿La compra fue realizada?` se recorre la salida Sí.
6. Continúa por `Registro cliente y compra realizada`.
7. Llega al final `Lead convertido`.

### Escenario B - Sin compra

1. El recorrido comienza con una consulta.
2. Pasa por el registro de datos, la atención y el seguimiento comercial.
3. En `¿La compra fue realizada?` se recorre la salida No.
4. Continúa por `Registro de resultado sin compra`.
5. Llega al final `Lead no convertido`.

**Resultado informado:** ambos caminos llegaron al final en la simulación. Esto nos sirve para revisar el recorrido del diagrama. No demuestra todavía que los datos se guarden o que la integración funcione en una instancia real.

### Pruebas que faltan registrar

- Iniciar una instancia real del proceso desplegado.
- Completar los formularios con datos ficticios y revisar las validaciones configuradas.
- Recorrer las opciones de compra y no compra.
- Verificar cómo se mantiene una consulta pendiente, cuando esa decisión esté resuelta e implementada.
- Mostrar que la instancia llega a la actividad externa, que el worker la atiende e invoca el servicio Java y que el proceso continúa.
- Guardar capturas, resultados o video, indicando qué se probó y qué quedó pendiente.

Lautaro lleva la parte de pruebas y Geraldine la implementación. Por mi parte, debo revisar que los formularios y los resultados correspondan al análisis del negocio.

## 7. Dudas que seguimos teniendo

1. ¿Utilizan hoy un cuaderno o Excel aparte de los chats?
2. ¿Cuánto esperan antes de volver a contactar a alguien que no respondió?
3. ¿Cuándo se considera concretada la compra: al pagar un abono, al pagar todo o al entregar el pedido?
4. ¿Cuándo corresponde cerrar una consulta sin compra?
5. ¿Cómo reconocen a una persona que pregunta por distintos canales?
6. ¿Con cuánta anticipación necesitan preparar un contacto por cumpleaños u otra fecha comercial?

Cuando tengamos respuestas, las agregaremos y revisaremos si cambian las reglas del proceso. No las damos por respondidas solamente por haberlas anotado.

## 8. Estado al 21 de octubre y trabajo pendiente

| Parte | Avance informado | Qué falta comprobar o terminar |
| --- | --- | --- |
| Análisis | Documento actualizado con el caso y las observaciones del grupo. | Revisar el alcance y las dudas con el equipo. |
| BPMN | Elvis informó que ambos modelos estaban en Flowable y que corrigió el error del AS-IS. | Respaldar los editables y revisar que coincidan con el análisis y el prototipo. |
| Simulación | Geraldine informó que los dos caminos llegaban al final; Lautaro agregó el recorrido. | Mantener separadas la simulación y la prueba de ejecución real. |
| Implementación e integración | Geraldine informó avances en ambas el 21 de octubre. | Resolver el problema con la plataforma y mostrar qué componentes ya funcionan. |
| Pruebas | Hay un registro del recorrido simulado. | Ejecutar y documentar el prototipo y la integración. |
| Repositorio | Creado; se compartieron invitaciones y usuarios. | Comprobar accesos, fuentes, actas separadas, README y enlaces de evidencia. |
| Videos | Se conversó su propósito. Me ofrecí para el de la emprendedora. | Coordinar, grabar y subir los enlaces. |

Geraldine avisó que tiene un problema con Flowable de IPLACEX y que ya consultó al profesor. En los mensajes revisados todavía no aparece una respuesta ni una confirmación de que esté resuelto. No damos la implementación por terminada.

Las fechas internas del 22, 23 y 24 de octubre estaban anotadas en la versión anterior. El plazo final informado para entregar es el jueves 29. Si necesitamos cambiar las metas internas, debemos acordarlo y anotarlo.

## 9. Videos y entrega individual

### Video para la emprendedora

Debemos explicar el problema, mostrar la propuesta y el prototipo disponible, y comentar sus beneficios en palabras sencillas. Hay que separar lo que funciona de lo que sigue pendiente.

Me ofrecí para hacerlo, solo o con otro compañero. Falta coordinarlo con el grupo. La pauta permite que lo presente una sola persona y no asigna esa obligación exclusivamente al analista.

### Video técnico

Debemos hablar los cuatro. Cada uno tiene un máximo de cinco minutos, por lo que el video del grupo puede durar como máximo veinte minutos.

Para mi intervención voy a preparar la explicación del problema, el alcance inicial, las mejoras propuestas y las revisiones en las que participé. Los demás deben mostrar sus propios aportes y evidencias. Podemos usar el diagrama de arquitectura para ordenar la explicación del equipo.

Los enlaces de ambos videos deben quedar en el repositorio.

### Envío individual

Cada integrante debe enviar por separado el enlace al repositorio en la actividad de entrega. La pauta indica nota 1 si no se realiza ese envío individual, aunque el trabajo del equipo esté disponible.

También debemos revisar que los usuarios de GitHub permitan identificarnos por nombre y apellido, como solicita el profesor. Las contribuciones deben poder reconocerse; no se evaluará solamente la cantidad de commits.

## 10. Actas y organización del repositorio

La pauta actual pide al menos una reunión semanal, que puede ser por WhatsApp. Cada registro debe incluir fecha, participantes, objetivo, avances, acuerdos, compromisos con responsables y plazos, bloqueos y próximos pasos.

Las actas se guardan por separado en `Actas/`, en Markdown. Word y PDF no las reemplazan. Más abajo quedan incluidas también en este documento para conservar el registro completo, pero deben subirse además los archivos individuales.

Archivos de actas preparados con esta actualización:

- [Acta 1 - organización inicial](Actas/Acta-2026-10-16.md).
- [Acta 2 - modelos y primeros avances](Actas/Acta-2026-10-20.md).
- [Acta 3 - actualización del 21 de octubre](Actas/Acta-2026-10-21.md).

Seguimos con un solo repositorio para todo el proyecto. El README debe explicar las carpetas, el propósito de los componentes y cómo revisar o ejecutar lo que entregamos. Los modelos editables van en `BPMN/`, el diagrama de arquitectura en `Arquitectura/` y los enlaces de videos deben quedar accesibles desde el repositorio. La estructura interna del código se ajustará a los proyectos que realmente usemos.

## 11. Documentos usados

- `PeluditosPetChile.pdf`, para el contexto y las necesidades del emprendimiento.
- `INP-SD-Entrega-1.pdf`, para los requisitos específicos de esta entrega.
- La planificación y el anuncio inicial del curso como antecedentes. Donde la pauta nueva cambia el alcance, actualizamos el trabajo según esa pauta.
- La coordinación por WhatsApp del equipo, resumida en las actas siguientes.

Los avances que conversamos por WhatsApp deben acompañarse con sus archivos y evidencias en el repositorio. Así podremos revisar las pruebas y mostrar lo que ya funciona.

## 12. Registro de actas

### Acta 1 - Organización inicial del equipo

**Fecha de cierre del registro:** 16 de octubre de 2026.  
**Período que resume:** del 13 al 16 de octubre de 2026.  
**Modalidad:** coordinación asincrónica por WhatsApp.  
**Participantes del período:** Felipe Quicel, Geraldine González, Elvis Gutiérrez y Lautaro Salinas.  
**Objetivo:** organizar el equipo, revisar el acceso a Flowable y preparar el trabajo mientras se conocía el caso.

#### Avances

- El 13 de octubre se incorporó Lautaro y quedaron los cuatro integrantes en el grupo.
- El 14 propuse comenzar como analista y crear el repositorio. Geraldine estuvo de acuerdo y se ofreció para apoyar con las actas.
- Compartimos las herramientas que estábamos usando y conversamos sobre trabajar con un entorno común.
- Hubo problemas para entrar a Flowable. Geraldine compartió una respuesta del profesor sobre la carga de cuentas y Elvis informó después que el enlace ya permitía entrar.
- El 15 compartí el enlace del repositorio del equipo en GitHub.
- El 16 conversamos sobre Peluditos Pet Chile. Geraldine propuso Java 21, Maven y Visual Studio Code; Elvis estuvo de acuerdo con usar las mismas herramientas.

#### Acuerdos y decisiones

- Comenzar con Felipe en análisis y preparación del repositorio.
- Seguir coordinándonos por WhatsApp.
- Usar como referencia la propuesta de Java 21, Maven y Visual Studio Code, y revisar cualquier diferencia antes de ejecutar los proyectos.
- Definir los demás roles al revisar el caso. En este período todavía no estaba cerrada esa distribución.

#### Compromisos

| Tarea | Responsable | Plazo registrado | Estado al cierre del período |
| --- | --- | --- | --- |
| Crear y compartir el repositorio. | Felipe | Compartido el 15 de octubre. | Realizado. |
| Revisar cómo entrar a Flowable. | Equipo, con apoyo de Elvis y Geraldine | Sin una fecha de compromiso registrada. | Elvis informó que el enlace ya funcionaba. |
| Definir los roles de la primera entrega. | Equipo | Sin una fecha de compromiso registrada. | Pendiente al 16 de octubre; se retomó después. |

#### Bloqueos y dificultades

- Al principio hubo confusión entre la instalación local y el acceso a la plataforma Cloud, además de problemas con las cuentas.
- El 14 todavía estábamos esperando el caso para ajustar el trabajo.

#### Logros y próximos pasos

Quedó creado el repositorio y comenzamos a organizarnos alrededor del caso de Peluditos. El siguiente paso era cerrar los roles y preparar el análisis y los primeros modelos.


---

### Acta 2 - Modelos BPMN y primeros avances

**Fecha de cierre del registro:** 20 de octubre de 2026.  
**Período que resume:** del 17 al 20 de octubre de 2026.  
**Modalidad:** coordinación asincrónica por WhatsApp.  
**Participantes del período:** Felipe Quicel, Elvis Gutiérrez, Geraldine González y Lautaro Salinas.  
**Objetivo:** distribuir los roles, revisar los modelos y organizar la implementación y las pruebas.

#### Revisión de lo anterior

- El repositorio ya estaba creado y su enlace se había compartido.
- Continuamos con la definición de roles y la revisión del proceso de Peluditos.
- El 20 pedí los usuarios de GitHub para agregar a los compañeros. Lautaro confirmó que aceptó la invitación. Geraldine y Elvis también compartieron sus usuarios; falta comprobar el acceso de todos dentro del repositorio.

#### Avances

- El 17 Elvis compartió el primer borrador AS-IS y comentó que después haría el TO-BE.
- Geraldine se ofreció para implementar en Flowable. El 18 confirmé en el grupo la propuesta de roles y ella confirmó expresamente su rol de implementadora. Elvis confirmó que se encargaría de los modelos.
- Lautaro revisó la necesidad de hacer seguimiento desde la consulta y propuso registrar el motivo de no compra cuando se conozca.
- El 19 Elvis informó que los dos modelos estaban subidos a Flowable.
- Geraldine probó los dos caminos del TO-BE con la simulación de tokens y comentó que llegaban al final.
- Elvis informó un error en el AS-IS y después avisó que estaba corregido. Geraldine indicó que comenzaría la implementación.
- El 20 Lautaro informó que añadió el log de pruebas al documento.

#### Acuerdos y decisiones

- La distribución de trabajo quedó con Felipe en análisis y repositorio, Elvis en BPMN, Geraldine en implementación y Lautaro en pruebas.
- El análisis debe considerar el seguimiento del interesado y no solamente la compra.
- En la conversación del 20 se planteó preparar un informe en Word. Después se compartió la pauta y Lautaro indicó que usaría Markdown y lo subiría al repositorio.
- Conversamos sobre los dos videos. Quedó pendiente ordenar su contenido y la participación de cada integrante según la pauta.

#### Compromisos registrados en el documento del equipo

Las fechas siguientes estaban anotadas en la versión del documento del 20 de octubre. Son metas internas; no corresponden a la fecha final de entrega ni se cambian por la ampliación del plazo.

| Tarea | Responsable | Fecha interna registrada | Estado informado en esa versión |
| --- | --- | --- | --- |
| Actualizar el análisis y el registro de coordinación. | Felipe | 20-10-2026 | Listo en la versión de ese día; requiere ajustes con la pauta nueva. |
| Subir los archivos BPMN y sus imágenes al repositorio. | Elvis | 22-10-2026 | En curso; los modelos ya estaban en Flowable. |
| Terminar la implementación ejecutable. | Geraldine | 23-10-2026 | En curso. |
| Realizar las pruebas y subir evidencias. | Lautaro y Geraldine | 24-10-2026 | En curso. |

#### Bloqueos y dificultades

- Elvis informó un error en el AS-IS el 19 y después indicó que estaba corregido.
- Al principio entendíamos que bastaba un borrador y que la integración vendría después. Debemos ajustar esa planificación a la pauta específica de la Entrega 1.

#### Logros y próximos pasos

Tenemos modelos compartidos en Flowable y una simulación de los dos caminos del TO-BE. Falta comprobar la ejecución real, guardar los archivos editables y completar la integración, la documentación y los videos.


---

### Acta 3 - Actualización de avances y bloqueo en Flowable

**Fecha:** 21 de octubre de 2026.  
**Modalidad:** coordinación asincrónica por WhatsApp.  
**Participantes con mensajes en el intercambio disponible de este día:** Geraldine González y Felipe Quicel.  
**Objetivo del registro:** dejar anotado el avance informado en implementación y la propuesta sobre los videos.

#### Avances

- A las 09:13 Geraldine informó que estaba trabajando en la implementación en Flowable y también en la integración con Web Services. Indicó que tenía avanzada esa parte.
- Geraldine comentó que encontró un problema con el Flowable de IPLACEX y que ya había consultado al profesor. Estaba esperando su respuesta para continuar.
- A las 17:10 propuse hacer el video para la emprendedora, solo o acompañado por otro integrante. También aclaré que en el video técnico debemos participar los cuatro.

#### Acuerdos y decisiones

- La propuesta sobre quién grabará el video para la emprendedora sigue pendiente de coordinación con el grupo.
- El video técnico debe incluir la intervención oral de todos, como pide la pauta. No se ha registrado todavía un orden de presentación.
- En los mensajes disponibles no aparece una nueva distribución detallada de tareas para el servicio Java y el worker.

#### Compromisos y seguimiento

| Tarea o seguimiento | Responsable | Plazo registrado | Estado |
| --- | --- | --- | --- |
| Consultar al profesor por el problema en Flowable. | Geraldine | Consulta ya realizada según su mensaje. | Esperando respuesta al momento del aviso. |
| Coordinar la participación en el video para la emprendedora. | Felipe se ofreció; falta acuerdo del grupo. | Sin fecha de grabación acordada. | Propuesto. |
| Organizar las intervenciones del video técnico. | Por coordinar entre los cuatro. | Sin fecha de grabación acordada. | Pendiente. |

#### Bloqueos y dificultades

El problema informado por Geraldine impide continuar con parte de la implementación. En el intercambio revisado todavía no aparece una respuesta del profesor ni una confirmación de solución.

#### Logros y próximos pasos

Ya sabemos que Geraldine está avanzando también en integración. Corresponde revisar con ella qué tiene funcionando, qué falta del servicio y del worker, y cómo podemos apoyarla. Después debemos registrar la ejecución real y preparar los videos.
