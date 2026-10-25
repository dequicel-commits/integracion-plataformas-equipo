# Peluditos Pet Chile - Integración de Plataformas

Proyecto del curso Integración de Plataformas de IPLACEX. Estamos trabajando en una solución para ordenar las consultas de Peluditos Pet Chile, registrar el seguimiento y conocer su resultado. Más adelante también se consideran las fechas importantes y las oportunidades de recompra.

**Profesor:** Santiago Neira.  
**Primera entrega:** jueves 29 de octubre de 2026.  
**Actualizado:** 24 de octubre de 2026.

## Integrantes y roles

| Integrante | Trabajo principal |
| --- | --- |
| Felipe Octavio Quicel Soto | Análisis del proceso y organización del repositorio. |
| Elvis Brayan Gutiérrez Bernal | Modelos BPMN AS-IS y TO-BE. |
| Geraldine Andrea González Acuña | Implementación en Flowable e integración con el servicio REST Java y el worker. |
| Lautaro Nelson Salinas Alvear | Pruebas, registro de problemas y propuesta de arquitectura. |

Nos coordinamos por WhatsApp y dejamos los avances y compromisos en las actas. Los roles pueden rotar en las siguientes entregas.

## Qué queremos resolver

El emprendimiento recibe consultas por distintos canales digitales y no cuenta con un mecanismo formal de registro y seguimiento. Queremos ordenar los datos de los interesados, las acciones realizadas y el resultado de cada consulta.

La primera entrega es un prototipo. El servicio Java actual demuestra la conexión con Flowable; todavía no guarda clientes ni compras. La gestión de campañas y fechas sigue planificada.

## Cómo revisar los archivos

- [Análisis del proceso y registro del equipo](<analisis y actas entrega.md>).
- [Acta del 16 de octubre](Actas/Acta-2026-10-16.md).
- [Acta del 20 de octubre](Actas/Acta-2026-10-20.md).
- [Acta del 21 de octubre](Actas/Acta-2026-10-21.md).
- [Acta del 24 de octubre](Actas/Acta-2026-10-24.md).
- [Descripción de la implementación](Entrega-1/README.md).
- [Documentación técnica y pasos de ejecución](Entrega-1/DOCUMENTACION-TECNICA.md).
- [Guía para el tester](Entrega-1/GUIA-PARA-TESTER.md).
- [Capturas de las pruebas](Entrega-1/Evidencias/Implementacion/).
- [Diagrama de arquitectura actual](arquitectura-demo.md).
- [Problemas y ajustes encontrados por Lautaro](problemas-prueba1.md).

## Qué tenemos al 24 de octubre

| Parte | Estado |
| --- | --- |
| Análisis y actas | Documentación del caso y de la coordinación del grupo. |
| Modelos BPMN | Informados en Flowable. La versión adaptada para OSS está en `Entrega-1/bpmn-oss/`; faltan los originales AS-IS y TO-BE en el repositorio revisado. |
| Prototipo Enterprise | Hay capturas del formulario de seguimiento y del proceso completado. |
| Servicio Java | Implementado `GET /api/seguimiento`, que responde un JSON de prueba. |
| Worker e integración | Código y capturas disponibles. Geraldine informó que la integración local funcionó y Lautaro también realizó una prueba en su equipo. |
| Arquitectura | Diagrama agregado por Lautaro, pendiente de organizar en `Arquitectura/`. |
| Videos | PowerPoint base compartido por Geraldine. Faltan completar las imágenes, grabar y publicar los enlaces. |

## Cómo se hizo la prueba

El prototipo con formularios se probó en Flowable Enterprise de IPLACEX. La integración se probó en Flowable OSS local porque, según la documentación del equipo, el Sandbox no generaba los trabajos del worker. Se consultó esta situación al profesor.

En la prueba local, el worker recibe un trabajo del tema `seguimiento-peluditos`, consulta el servicio Java y completa el trabajo cuando recibe HTTP 200. El proceso puede seguir hacia la decisión de compra.

Las capturas muestran el resultado de esas pruebas. Debemos explicar los dos entornos al presentar el proyecto, sin dar por solucionado el problema del Sandbox.

## Programas y ejecución

La documentación de implementación usa Java 21, Maven, Visual Studio Code, Flowable Enterprise y Flowable OSS local con Tomcat. El servicio REST se desarrolló con Javalin.

El código está en:

- [Servicio REST Java](Entrega-1/servicio-peluditos/).
- [External worker](Entrega-1/external-worker-peluditos/).
- [BPMN adaptado para OSS](Entrega-1/bpmn-oss/peluditos-integracion-oss.bpmn20.xml).

Para ejecutar los componentes, seguir la [documentación técnica](Entrega-1/DOCUMENTACION-TECNICA.md). Antes de repetir la prueba, revisar también [los ajustes del tester](problemas-prueba1.md):

- El código subido usa el puerto 7070. Lautaro usó 7071 porque el primero estaba ocupado en su equipo; si se cambia, debe coincidir en el servicio y en el worker.
- La variable `compraRealizada` debe estar definida antes de llegar a la decisión. No debe quedar fija en verdadero si se quiere probar la ruta sin compra.
- El modelo local tenía una referencia a un formulario no incluido. Lautaro pudo avanzar completando la tarea por API; eso no equivale a tener ese formulario local funcionando.

Geraldine y Lautaro deben dejar acordados los pasos y la configuración de la versión que se entregará.

## Qué queda por cerrar

- Subir `BPMN/AS-IS.bpmn` y `BPMN/TO-BE.bpmn` y revisar que coincidan con el análisis.
- Organizar el diagrama dentro de `Arquitectura/` y actualizar sus enlaces.
- Confirmar y documentar la ejecución de ambas rutas en la versión final.
- Completar el PowerPoint con los diagramas y evidencias de cada integrante.
- Grabar los dos videos y agregar sus enlaces.

La documentación técnica ya incluye una lista inicial de servicios propuestos para seguir el proyecto. No todos están implementados.

## Videos y envío

- **Video para la emprendedora:** enlace pendiente. Debe explicar el problema y la propuesta, mostrar el prototipo disponible y sus límites. Felipe se ofreció para participar; falta cerrar la organización.
- **Video técnico:** enlace pendiente. Debemos hablar los cuatro, con un máximo de cinco minutos por integrante y veinte minutos en total. Conversamos grabar por separado y unir las partes usando el PowerPoint base.

Cada integrante debe enviar individualmente el enlace del repositorio en la actividad de entrega. Debemos comprobar que el profesor pueda acceder a los archivos y a los videos.
