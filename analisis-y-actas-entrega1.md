[entrega_1_analista_de_procesos_y_actas_inp.md](https://github.com/user-attachments/files/33442371/entrega_1_analista_de_procesos_y_actas_inp.md)
# Entregable 1 — Análisis de Procesos (AS-IS / TO-BE) y Actas de Seguimiento

**Curso:** Integración de Plataformas (INP) — IPLACEX  
**Docente:** Santiago Neira (`sneira@profesor.iplacex.cl`)  
**Repositorio Oficial del Equipo:** [dequicel-commits/integracion-plataformas-equipo](https://www.google.com/search?q=https%3A%2F%2Fgithub.com%2Fdequicel-commits%2Fintegracion-plataformas-equipo)  
**Fecha de actualización:** 14 de octubre de 2026  

**Integrantes del Equipo y Roles (Iteración Semanas 3–4):**
* **Felipe Octavio Quicel Soto** — *Analista de Procesos*
* **Geraldine Andrea González Acuña** — *Modeladora BPMN*
* **Elvis Brayan Gutiérrez Bernal** — *Implementador Flowable*
* **Lautaro Nelson Salinas Alvear** — *QA / Tester y Gestión de Repositorio*

## 1. Identificación del Caso y Diagnóstico Inicial

### 1.1 Antecedentes del Socio Comunitario (Micro o Pequeña Empresa)

| Campo | Detalle del Caso Asignado |
| :--- | :--- |
| **Nombre de la organización / empresa** | *(Completar al recibir asignación del docente)* |
| **Rubro o giro principal** | *(Ej. Comercio minorista, servicios técnicos, gestión de socios, atención de clientes)* |
| **Proceso de negocio a intervenir** | *(Ej. Proceso de ingreso y validación de solicitudes / pedidos / reservas)* |
| **Problema central detectado** | *(Describir qué demora, pérdida de trazabilidad o reproceso ocurre hoy al hacerlo de forma manual)* |
| **Objetivo del rediseño (TO-BE)** | Estandarizar y digitalizar el flujo del proceso en Flowable, asegurando trazabilidad de estados y reduciendo tiempos de respuesta. |
| **Nivel de madurez inicial (BPMM)** | **Nivel 1 (Inicial / Ad-hoc):** Proceso ejecutado de forma manual (correo/mensajería/planillas) sin control formal de estados ni métricas. |
| **Nivel de madurez objetivo (BPMM)** | **Nivel 2–3 (Gestionado / Estandarizado):** Flujo formalizado en BPMN 2.0, con roles definidos, reglas de negocio claras y ejecución trazable en Flowable. |

### 1.2 Participantes del Proceso (Pools y Lanes para el Diagrama BPMN)

| Participante / Rol (Lane BPMN) | Tipo de Actor | Responsabilidad dentro del proceso |
| :--- | :--- | :--- |
| **Solicitante / Cliente** | Externo (Iniciador) | Inicia el proceso ingresando los datos de su solicitud o requerimiento. |
| **Encargado Operativo / Ejecutivo** | Interno (Usuario Flowable) | Revisa la solicitud recibida, verifica los antecedentes y aprueba, observa o rechaza el caso. |
| **Administrador / Supervisor** | Interno (Usuario Flowable) | Resuelve casos especiales o autoriza el cierre/entrega final del servicio. |
| **Sistema Flowable / Servicios Web** | Automatizado (Motor BPMN) | Registra el estado del caso, asigna tareas humanas y ejecuta validaciones automáticas (*Service Tasks* en Entregable 2). |

## 2. Levantamiento del Proceso Actual (BPMN AS-IS Simplificado)

Esta tabla sirve de insumo directo para que **Geraldine González** (*Modeladora BPMN*) dibuje el diagrama **AS-IS** de la Semana 3. Refleja cómo opera la organización hoy sin automatización:

| Paso | Actor (Lane) | Nombre de la Actividad (Verbo + Objeto) | Canal / Herramienta Actual | Problema o Cuello de Botella (Dolor) |
| :--- | :--- | :--- | :--- | :--- |
| **1** | Solicitante / Cliente | Enviar solicitud o requerimiento | WhatsApp, correo electrónico o presencial | Los datos llegan incompletos y sin un formato estándar. |
| **2** | Encargado Operativo | Recibir y anotar datos manualmente | Planilla Excel o cuaderno de registro | Doble digitación, riesgo de pérdida de mensajes y falta de folio único. |
| **3** | Encargado Operativo | Verificar disponibilidad o antecedentes | Revisión manual en archivos o consultas internas | Demora en la revisión; el cliente no sabe en qué estado está su solicitud. |
| **4** | Encargado Operativo | Informar respuesta al solicitante | Respuesta manual por WhatsApp o correo | Falta de trazabilidad histórica y retrasos en la confirmación. |
| **5** | Encargado Operativo | Archivar o cerrar el requerimiento | Registro manual en planilla | Sin indicadores automáticos de tiempos de atención ni control de estados. |

## 3. Propuesta de Proceso Mejorado (BPMN TO-BE Preliminar)

Esta tabla guía a **Geraldine González** (*Modeladora BPMN*) para construir el diagrama **TO-BE preliminar** (y proyecta dónde se conectarán los Web Services y *Gateways* en las siguientes iteraciones):

| Paso | Actor (Lane) | Actividad en el Nuevo Proceso | Tipo de Elemento BPMN 2.0 | Mejora Incorporada en el TO-BE |
| :--- | :--- | :--- | :--- | :--- |
| **Inicio** | Solicitante / Sistema | Solicitud ingresada en formulario | **Start Event** (Evento de Inicio) | Captura estructurada con campos obligatorios validados desde el origen. |
| **1** | Solicitante / Encargado | Registrar datos de la solicitud | **User Task** (Tarea Humana) | Registro único en Flowable con generación automática de instancia y trazabilidad. |
| **2** | Sistema (Iteración 2) | Validar antecedentes / consultar servicio | **Service Task** *(External Worker REST/SOAP)* | *(Proyectado para Semana 5)* Validación automática mediante Web Service en Java. |
| **3** | Encargado Operativo | Evaluar y resolver solicitud | **User Task** + **Exclusive Gateway (XOR)** | El encargado revisa la información en su bandeja de Flowable y decide aprobar o rechazar. |
| **4** | Sistema / Encargado | Actualizar estado y notificar resolución | **User Task** / **Service Task** | El estado del caso queda registrado formalmente (`APROBADO` / `RECHAZADO`). |
| **Fin** | Sistema | Solicitud finalizada y trazada | **End Event** (Evento de Término) | Cierre formal de la instancia con historial completo de auditoría en Flowable. |

## 4. Especificación del Proceso Mínimo Ejecutable (Semana 3 — Flowable)

De acuerdo con la programación de la **Semana 3**, el primer prototipo desplegado en [Flowable Cloud IPLACEX](https://www.google.com/search?q=https%3A%2F%2Fiplacex.cloud.flowable.com%2F) debe implementar el flujo mínimo ejecutable:

`Evento de Inicio → Tarea Humana (User Task) → Evento de Fin`

### 4.1 Ficha Técnica para el Implementador de Flowable

| Parámetro en Flowable Design | Valor Definido |
| :--- | :--- |
| **Nombre del Proceso (Process Name)** | `Proceso Gestion de Solicitudes MVP` |
| **Identificador del Proceso (Process Key)** | `proceso_solicitud_mvp` |
| **Evento de Inicio (Start Event)** | `Inicio de Solicitud` |
| **Tarea Humana (User Task)** | `Revisar y Validar Solicitud` |
| **Asignación (Assignee / Candidate Group)** | Usuario ejecutor del equipo en Flowable Cloud |
| **Evento de Término (End Event)** | `Solicitud Procesada` |

### 4.2 Campos del Formulario de la Tarea Humana (Form Fields)

Estos son los campos base que **Elvis Gutiérrez** (*Implementador de Flowable*) debe agregar en el formulario asociado a la *User Task* (sirven para cualquier rubro y solo se ajusta la etiqueta según la empresa asignada):

| Etiqueta en Pantalla (Label) | Nombre de Variable (`id`) | Tipo de Dato (Flowable) | Obligatorio | Uso dentro del Proceso |
| :--- | :--- | :--- | :--- | :--- |
| **Nombre o Razón Social del Solicitante** | `nombreSolicitante` | Texto (`Text`) | Sí | Identificar a la persona o cliente que origina el caso. |
| **Identificador / Folio / RUT** | `identificadorCliente` | Texto (`Text`) | Sí | Llave de búsqueda para futuras integraciones con Web Services (Semana 5). |
| **Correo Electrónico de Contacto** | `correoContacto` | Texto (`Text`) | Sí | Canal para notificaciones del proceso. |
| **Tipo o Motivo de Solicitud** | `tipoSolicitud` | Selección (`Dropdown` / `Text`) | Sí | Clasificar el requerimiento del negocio. |
| **Detalle o Descripción del Requerimiento** | `detalleSolicitud` | Texto multilínea (`Multiline Text`) | Sí | Contexto específico de la solicitud ingresada. |
| **Estado de Revisión Inicial** | `estadoSolicitud` | Selección (`APROBADO` / `PENDIENTE` / `RECHAZADO`) | Sí | Variable de control de estado (base para el *Gateway* de las Semanas 4–6). |
| **Observaciones del Revisor** | `observacionesRevision` | Texto multilínea (`Multiline Text`) | No | Registro de comentarios técnicos o motivo de la decisión. |

## 5. Registro de Actas de Reunión (Evidencia Obligatoria para el Repositorio)

Según los lineamientos del curso, el equipo debe registrar **al menos 2 reuniones semanales** (sincrónicas o asincrónicas por WhatsApp/Discord/Meet) y guardarlas en GitHub.

### Acta de Reunión N°1 — Conformación, Roles y Preparación Base (Semana 2)

* **Fecha:** 14 de octubre de 2026
* **Modalidad:** Coordinación de equipo (Sincrónica / Asincrónica)
* **Participantes y Roles Asignados (Iteración Semanas 3–4):**
  * **Analista de Procesos:** Felipe Octavio Quicel Soto
  * **Modeladora BPMN:** Geraldine Andrea González Acuña
  * **Implementador Flowable:** Elvis Brayan Gutiérrez Bernal
  * **QA / Tester y Gestión de Repositorio:** Lautaro Nelson Salinas Alvear
* **Resumen de avances realizados:**
  * Revisión de los lineamientos de evaluación del curso y calendario de entregas (Semanas 3 a 9).
  * Conformación oficial del equipo de 4 integrantes y distribución de roles iniciales para las Semanas 3 y 4.
  * Creación del repositorio grupal en GitHub ([dequicel-commits/integracion-plataformas-equipo](https://www.google.com/search?q=https%3A%2F%2Fgithub.com%2Fdequicel-commits%2Fintegracion-plataformas-equipo)).
  * Elaboración de la estructura base del documento de Análisis de Procesos (diagnóstico BPMM, tablas AS-IS y TO-BE, y especificación técnica de la Tarea Humana mínima para Flowable) por parte de Felipe Quicel.
  * Verificación de acceso a la plataforma [Flowable Cloud IPLACEX](https://www.google.com/search?q=https%3A%2F%2Fiplacex.cloud.flowable.com%2F).
* **Tareas comprometidas para el siguiente período:**

| Tarea Comprometida | Responsable | Fecha Límite | Estado |
| :--- | :--- | :--- | :--- |
| Enviar nómina oficial del grupo al profesor y consultar asignación de empresa/caso | Equipo Completo | 15-10-2026 | En progreso |
| Adaptar tablas AS-IS y TO-BE apenas se confirme el caso de la micro/pequeña empresa | Felipe Octavio Quicel Soto (Analista) | 19-10-2026 | Preparado (base lista en GitHub) |
| Iniciar bocetos de diagramas BPMN AS-IS y TO-BE según tablas 2 y 3 | Geraldine Andrea González Acuña (Modeladora) | 20-10-2026 | Pendiente |
| Configurar flujo mínimo (`Inicio → Tarea humana → Fin`) en Flowable Cloud | Elvis Brayan Gutiérrez Bernal (Implementador) | 20-10-2026 | Pendiente |
| Mantener estructura del repositorio en GitHub y preparar pruebas de ejecución | Lautaro Nelson Salinas Alvear (QA / Tester) | 21-10-2026 | En progreso |

* **Bloqueos o dificultades identificados:**
  * **Bloqueo 1 (Externo):** A la fecha de esta reunión, el equipo se encuentra a la espera de la asignación oficial de la micro o pequeña empresa (socio comunitario) por parte del docente para completar los datos específicos del rubro en las secciones 1, 2 y 3 del análisis. **Acción de mitigación:** Felipe Quicel dejó construida toda la arquitectura documental y el diseño del formulario base del MVP en GitHub para completar los datos del negocio en cuanto sea asignado.

### Acta de Reunión N°2 — Revisión del Caso Asignado y Modelamiento (Plantilla Lista)

* **Fecha:** *(Completar fecha de la segunda reunión de la semana)*
* **Modalidad:** *(Sincrónica online / Asincrónica por WhatsApp o Discord)*
* **Participantes:**
  * Felipe Octavio Quicel Soto (*Analista de Procesos*)
  * Geraldine Andrea González Acuña (*Modeladora BPMN*)
  * Elvis Brayan Gutiérrez Bernal (*Implementador Flowable*)
  * Lautaro Nelson Salinas Alvear (*QA / Tester*)
* **Revisión de compromisos de la reunión anterior:**
  * Confirmación de grupo enviado al docente: *(Completado / En espera)*
  * Repositorio GitHub creado y estructurado con Acta N°1: **Completado**
* **Resumen de avances realizados:**
  * Recepción y análisis del caso de negocio asignado.
  * Cierre del levantamiento AS-IS y propuesta TO-BE por parte del Analista de Procesos (Felipe Quicel).
  * Avance en el diseño de los diagramas BPMN AS-IS y TO-BE preliminar y configuración del proceso mínimo en Flowable.
* **Tareas comprometidas para el cierre del Entregable 1 (Semana 3):**

| Tarea Comprometida | Responsable | Fecha Límite | Estado |
| :--- | :--- | :--- | :--- |
| Publicar versión final del Análisis AS-IS / TO-BE con datos de la empresa en GitHub | Felipe Octavio Quicel Soto (Analista) | 20-10-2026 | Pendiente |
| Subir diagramas BPMN AS-IS y TO-BE preliminar (`.bpmn20.xml` y `.png`) al repositorio | Geraldine Andrea González Acuña (Modeladora) | 22-10-2026 | Pendiente |
| Desplegar proceso mínimo (`Inicio → Tarea humana → Fin`) en Flowable Cloud | Elvis Brayan Gutiérrez Bernal (Implementador) | 23-10-2026 | Pendiente |
| Grabar evidencia de ejecución y validar entrega completa en el repositorio | Lautaro Nelson Salinas Alvear (QA / Tester) | 24-10-2026 | Pendiente |

* **Bloqueos o dificultades identificados:**
  * *(Registrar cualquier duda técnica con Flowable Cloud o con las reglas del negocio asignado).*
