# Peluditos Pet Chile - Integración de Plataformas

Proyecto del curso Integración de Plataformas de IPLACEX. Estamos trabajando en una solución para ordenar las consultas de Peluditos Pet Chile, guardar los datos de los potenciales clientes y sus mascotas, registrar el seguimiento comercial y conocer su resultado. Más adelante también se consideran las campañas promocionales en fechas importantes y las oportunidades de recompra.

**Profesor:** Santiago Neira.  
**Primera entrega:** jueves 29 de octubre de 2026.  
**Segunda entrega:** noviembre de 2026.  
**Actualizado:** 9 de noviembre de 2026.

## Integrantes y roles

| Integrante | Trabajo en Entrega 1 | Trabajo en Entrega 2 |
| --- | --- | --- |
| Felipe Octavio Quicel Soto | Análisis del proceso y organización del repositorio. | Implementación de la interfaz web local e integración del registro con Flowable REST. |
| Elvis Brayan Gutiérrez Bernal | Modelos BPMN AS-IS y TO-BE. | Pruebas QA / Tester, validación del flujo BPMN e informe de pruebas. |
| Geraldine Andrea González Acuña | Implementación en Flowable e integración con el servicio REST Java y el worker. | Actualización del modelo BPMN TO-BE, análisis de requisitos y evidencias en Flowable. |
| Lautaro Nelson Salinas Alvear | Pruebas, registro de problemas y propuesta de arquitectura. | Desarrollo del servicio REST con persistencia en SQLite, External Worker, modelo de datos y arquitectura. |

Nos coordinamos por WhatsApp y dejamos los avances y compromisos en las actas. Los roles rotan entre entregas según las necesidades del proyecto.

## Qué queremos resolver

El emprendimiento recibe consultas por distintos canales digitales (WhatsApp, Instagram, Facebook y TikTok) y no cuenta con un mecanismo formal de registro y seguimiento. Queremos ordenar los datos de los interesados y sus mascotas, las acciones realizadas y el resultado de cada consulta.

En la primera entrega construimos un prototipo inicial que demostró la conexión entre Flowable y el servicio Java. En esta **segunda entrega** incorporamos el guardado automático de potenciales clientes y sus mascotas en una base de datos relacional (**SQLite**), un formulario web local conectado a Flowable y las pruebas de integración completas. La activación del flujo de campañas y cumpleaños queda planificada para la Entrega 3.

## Cómo revisar los archivos

### General, actas y modelos BPMN
- [Análisis del proceso y registro del equipo](<analisis y actas entrega.md>).
- [Modelos BPMN (AS-IS, TO-BE y TO-BE Entrega 2)](BPMN/README.md).
- Actas de coordinación:
  - [Acta del 16 de octubre](Actas/Acta-2026-10-16.md).
  - [Acta del 20 de octubre](Actas/Acta-2026-10-20.md).
  - [Acta del 21 de octubre](Actas/Acta-2026-10-21.md).
  - [Acta del 24 de octubre](Actas/Acta-2026-10-24.md).
  - [Acta del 5 de noviembre (Entrega 2)](Actas/Acta-2026-11-05.md).

### Entrega 2 (Actual)
- [Análisis de requisitos — Entrega 2](Entrega-2/Analisis/ANALISIS-REQUISITOS-ENTREGA2.md).
- [Diagrama Entidad-Relación y tablas de la base de datos](Entrega-2/Analisis/DIAGRAMA-ENTIDAD-RELACION.md).
- [Diagrama de arquitectura — Entrega 2](Entrega-2/Arquitectura/README.md).
- [Guía para el tester y pasos de ejecución — Entrega 2](Entrega-2/GUIA-PARA-TESTER.md).
- [Informe de pruebas QA / Tester](Entrega-2/QA-Tester/Informe-Pruebas-QA.md) y [evidencias QA](Entrega-2/QA-Tester/Evidencias/).
- [Evidencias de validación BPMN](Entrega-2/Evidencias/BPMN/) y [evidencias de implementación](Entrega-2/Evidencias/Evidencias/Implementacion/).
- [Video para la emprendedora — Entrega 2](Videos/Entrega-2/video-emprendedora.md).

### Entrega 1 (Histórico)
- [Descripción de la implementación — Entrega 1](Entrega-1/README.md).
- [Documentación técnica — Entrega 1](Entrega-1/DOCUMENTACION-TECNICA.md).
- [Guía para el tester — Entrega 1](Entrega-1/GUIA-PARA-TESTER.md).
- [Diagrama de arquitectura inicial — Entrega 1](arquitectura-demo.md).
- [Problemas y ajustes encontrados en la Entrega 1](problemas-prueba1.md).
- [Videos de la Entrega 1](Videos/Entrega-1/).

## Qué tenemos en la Entrega 2

| Parte | Estado |
| --- | --- |
| Análisis y modelo de datos | Requisitos funcionales actualizados y diseño de base de datos relacional con 4 tablas (`TABLA_CLIENTE`, `TABLA_MASCOTA`, `TABLA_TIPO_MASCOTA` y `TABLA_PRODUCTO`). |
| Modelos BPMN | `BPMN/TO-BE-Entrega2.bpmn` y su versión ejecutable en `Entrega-2/bpmn-oss/peluditos-integracion-oss-entrega2.bpmn20.xml`, incorporando la tarea automática «Guardar datos del potencial cliente» (`topic: guardar-cliente-peluditos`). |
| Arquitectura | Diagrama de componentes actualizado para la Entrega 2 en `Entrega-2/Arquitectura/`. |
| Interfaz web y registro | Formulario web en `Entrega-2/interfaz-peluditos/` servido en `/interfaz/index.html`, que envía las consultas a `POST /api/consultas/{referencia}` para iniciar el caso en Flowable OSS. |
| Servicio REST y SQLite | Implementado con Javalin y SQLite (`peluditos.db`). Expone `POST /api/clientes`, `GET /api/clientes`, `GET /api/productos`, `POST /api/consultas/{referencia}` y `GET /api/seguimiento`. |
| External Worker | Consume trabajos del tema `guardar-cliente-peluditos`, envía los datos del cliente y su mascota a `POST /api/clientes` y completa la tarea en Flowable. |
| Pruebas QA | Informe de pruebas (`QA-01` a `QA-17`) y capturas de verificación disponibles en `Entrega-2/QA-Tester/`. |

## Cómo se hizo la prueba en la Entrega 2

En la prueba local se levanta Flowable OSS (`flowable-rest` o `flowable-ui` en el puerto `8080`) junto con el servicio REST Java en el puerto `7070` (o `7071` como respaldo automático si el `7070` está ocupado).

El registro se puede iniciar tanto desde Flowable como desde el formulario web local (`/interfaz/index.html`). Al completarse la primera tarea («Registrar datos del potencial cliente»), el proceso avanza a la tarea automática «Guardar datos del potencial cliente». Al ejecutar el External Worker, este toma el trabajo pendiente del tema `guardar-cliente-peluditos`, invoca `POST /api/clientes` en el servicio Java para insertar al cliente y su mascota en SQLite (`peluditos.db`) y marca el trabajo como completado en Flowable para continuar con la atención de la consulta y el seguimiento comercial.

## Programas y ejecución

El proyecto utiliza Java 17/21, Apache Maven 3.9+, SQLite JDBC, Javalin 6.7 y Flowable OSS local sobre Apache Tomcat (`:8080`).

El código de la Entrega 2 está en:

- [Servicio REST Java + SQLite](Entrega-2/servicio-peluditos/).
- [External Worker](Entrega-2/external-worker-peluditos/).
- [Interfaz web local](Entrega-2/interfaz-peluditos/).
- [BPMN y formularios adaptados para OSS](Entrega-2/bpmn-oss/).

Para levantar el entorno paso a paso y reproducir las pruebas desde Flowable o desde el formulario web, seguir la [guía para el tester de la Entrega 2](Entrega-2/GUIA-PARA-TESTER.md).

## Videos y envío

- **Video para la emprendedora (Entrega 2):** [disponible aquí](Videos/Entrega-2/video-emprendedora.md).
- **Video técnico (Entrega 2):** pendiente de agregar el enlace final.
- **Videos de la Entrega 1:** [video emprendedora](Videos/Entrega-1/video-emprendedora-1.md) y [video técnico](Videos/Entrega-1/video-tecnico-1.md).

Cada integrante debe enviar individualmente el enlace del repositorio en la actividad de entrega y verificar que todos los archivos y videos estén accesibles.
