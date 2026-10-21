# Peluditos Pet Chile - Integración de Plataformas

Proyecto del curso Integración de Plataformas de IPLACEX. Estamos trabajando en una solución para ordenar las consultas de potenciales clientes de Peluditos Pet Chile y facilitar su seguimiento. Más adelante también se considera la gestión de oportunidades de recompra por fechas importantes.

**Profesor:** Santiago Neira.  
**Primera entrega:** jueves 29 de octubre de 2026.  
**Estado de esta descripción:** actualizado con los avances informados al 21 de octubre de 2026.

## Integrantes y roles

| Integrante | Trabajo principal |
| --- | --- |
| Felipe Octavio Quicel Soto | Análisis del proceso y organización del repositorio. |
| Elvis Brayan Gutiérrez Bernal | Modelos BPMN AS-IS y TO-BE. |
| Geraldine Andrea González Acuña | Implementación en Flowable y avances en integración con Web Services. |
| Lautaro Nelson Salinas Alvear | Pruebas y registro de resultados. |

Nos coordinamos por WhatsApp y dejamos los avances y compromisos en las actas. Los roles pueden rotar durante las siguientes entregas.

## Qué queremos resolver

El emprendimiento recibe consultas por distintos canales digitales. El problema es que no existe un mecanismo formal para registrar a los interesados y dar seguimiento a cada consulta.

La propuesta considera:

- Registrar los datos del interesado, su contacto y el canal por el que llegó.
- Dejar anotadas las acciones de seguimiento.
- Identificar si la consulta terminó en compra o no concretó, manteniendo diferenciadas las que siguen pendientes.
- Incorporar después las fechas relevantes y las oportunidades de recompra.

En esta entrega debemos demostrar un prototipo en Flowable Enterprise con formularios y una primera integración entre el proceso, un external worker y un servicio de prueba en Java. No se espera que toda la solución definitiva esté terminada.

## Cómo revisar lo que tenemos

1. Leer el [análisis del proceso](<analisis y actas entrega.md>) para conocer el caso, la propuesta, los datos del formulario y las dudas pendientes.
2. Revisar las actas para ver los avances y la distribución del trabajo:
   - [Acta 1 - Organización inicial](Actas/Acta-2026-10-16.md).
   - [Acta 2 - Modelos y primeros avances](Actas/Acta-2026-10-20.md).
   - [Acta 3 - Actualización del 21 de octubre](Actas/Acta-2026-10-21.md).
3. Revisar los modelos, el código y las evidencias a medida que se incorporen al repositorio.

## Estado del trabajo

| Parte | Avance informado al 21 de octubre |
| --- | --- |
| Análisis | Documento actualizado con el caso, la propuesta y las observaciones del grupo. |
| Actas | Registros de coordinación separados en archivos Markdown dentro de `Actas/`. |
| BPMN | Elvis informó que ambos modelos estaban en Flowable y que corrigió el error del AS-IS. Falta comprobar su respaldo editable en el repositorio. |
| Simulación | Se informó que los dos caminos del TO-BE llegaban al final. El recorrido está descrito en el análisis. |
| Implementación e integración | Geraldine informó avances, pero encontró un problema con Flowable de IPLACEX y consultó al profesor. Falta registrar su solución y la ejecución integrada. |
| Pruebas | Hay un registro de simulación. La evidencia de ejecución real debe incorporarse cuando se realice. |
| Videos | Pendientes de coordinación, grabación y publicación de sus enlaces. |

La simulación de tokens sirve para revisar el recorrido del modelo. La ejecución real con formularios, servicio Java y worker debe demostrarse por separado.

## Herramientas de trabajo

- Flowable Enterprise de IPLACEX y Flowable Forms para el prototipo.
- Java para el servicio web de prueba. Se propuso trabajar con Java 21.
- Maven y Visual Studio Code como herramientas de desarrollo propuestas para el equipo.
- GitHub para compartir los archivos y registrar los aportes.

## Archivos que debemos completar para la entrega

- Modelos editables `BPMN/AS-IS.bpmn` y `BPMN/TO-BE.bpmn`.
- Código fuente y documentación del servicio Java y del external worker.
- Lista inicial de servicios y operaciones previstas, con sus entradas, salidas y relación con el proceso.
- Diagrama de arquitectura actualizado dentro de `Arquitectura/`.
- Evidencia de una instancia real que complete el recorrido de prueba y la integración.
- Enlaces del video para la emprendedora y del video técnico.

Los responsables deben actualizar este README cuando suban los componentes, incluyendo sus ubicaciones y los pasos para revisarlos o ejecutarlos.

## Ejecución del prototipo

La plataforma del curso es [Flowable de IPLACEX](https://iplacex.cloud.flowable.com/), con acceso mediante la cuenta institucional habilitada.

Todavía falta incorporar aquí las instrucciones verificadas del despliegue y de la ejecución del servicio Java y del worker. Deben incluir los requisitos, la configuración necesaria, cómo iniciar cada componente y un ejemplo de prueba. Las completaremos con Geraldine cuando se confirme qué está funcionando.

## Videos y envío

- **Video para la emprendedora:** enlace pendiente. Debe explicar el problema, la propuesta y mostrar las funciones disponibles.
- **Video técnico:** enlace pendiente. Participamos oralmente los cuatro, con un máximo de cinco minutos por integrante y veinte minutos en total.

Cada integrante debe enviar individualmente el enlace del repositorio en la actividad de entrega. También debemos comprobar que el profesor pueda acceder a los archivos y videos.
