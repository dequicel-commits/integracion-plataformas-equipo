[entrega_1_analista_de_procesos_y_actas_inp.md](https://github.com/user-attachments/files/33442539/entrega_1_analista_de_procesos_y_actas_inp.md)
# Avance Entregable 1 - Análisis del Proceso y Actas de Reunión

**Asignatura:** Integración de Plataformas (INP)  
**Profesor:** Santiago Neira  
**Fecha:** 14 de octubre de 2026  

**Integrantes del grupo y roles (Semanas 3 y 4):**
* **Felipe Octavio Quicel Soto** - Analista de procesos
* **Geraldine Andrea González Acuña** - Modeladora BPMN
* **Elvis Brayan Gutiérrez Bernal** - Implementador Flowable
* **Lautaro Nelson Salinas Alvear** - QA / Tester y encargado de repositorio

## 1. Resumen del Caso y Participantes

*(Nota: Datos específicos de la empresa pendientes de confirmación por el profesor).*

* **Empresa / Socio comunitario:** *(Por confirmar)*
* **Rubro:** *(Por confirmar)*
* **Proceso que vamos a trabajar:** Recepción, revisión y gestión de solicitudes de clientes.
* **Problema actual:** Actualmente el proceso se lleva de forma manual (por correo o WhatsApp y planillas Excel), lo que genera demoras en responder, datos incompletos al recibir una solicitud y poca claridad sobre en qué estado va cada caso.
* **Objetivo de la mejora:** Pasar el proceso a Flowable para que las solicitudes entren con datos ordenados, cada encargado tenga sus tareas claras en pantalla y quede registro del estado de principio a fin (pasando de un nivel inicial desordenado a un proceso estandarizado según BPMM).

**Actores del proceso (Lanes para el diagrama BPMN):**
1. **Cliente / Solicitante:** Persona que inicia el proceso enviando su solicitud.
2. **Encargado / Ejecutivo:** Quien recibe el caso en el sistema, revisa los datos y aprueba o rechaza.
3. **Administrador / Supervisor:** Quien valida el cierre o revisa casos con observaciones.
4. **Sistema (Flowable):** Encargado de mover las tareas y guardar el estado del proceso.

## 2. Proceso Actual (AS-IS)

Paso a paso de cómo se hace hoy el proceso de manera manual (base para armar el diagrama BPMN AS-IS):

| N° | Quién lo hace (Lane) | Paso del proceso | Cómo se hace hoy | Problema que genera |
| :--- | :--- | :--- | :--- | :--- |
| 1 | Cliente | Envía la solicitud | Por WhatsApp o correo | A veces faltan datos importantes para atender el pedido. |
| 2 | Encargado | Recibe y anota los datos | Planilla Excel a mano | Se pierde tiempo traspasando datos y se pueden traspapelar. |
| 3 | Encargado | Revisa antecedentes o disponibilidad | Búsqueda manual en archivos | Toma mucho tiempo validar cada solicitud. |
| 4 | Encargado | Responde al cliente | Mensaje manual por correo o teléfono | El cliente no tiene cómo saber el estado si no pregunta. |
| 5 | Encargado | Cierra y guarda el registro | Actualiza el Excel | Cuesta hacer seguimiento de qué casos siguen pendientes. |

## 3. Proceso Propuesto (TO-BE Preliminar)

Paso a paso de cómo funcionará con el sistema en Flowable (base para el diagrama BPMN TO-BE):

| N° | Quién lo hace (Lane) | Paso en el nuevo flujo | Elemento en BPMN | Qué mejora |
| :--- | :--- | :--- | :--- | :--- |
| Inicio | Cliente / Sistema | Ingreso de solicitud | Evento de Inicio (Start Event) | El proceso parte con todos los datos necesarios desde el inicio. |
| 1 | Cliente / Encargado | Registrar solicitud en formulario | Tarea Humana (User Task) | Queda guardado al tiro en Flowable con un identificador único. |
| 2 | Sistema | Validar datos automáticamente | Tarea de Servicio (Service Task) | *(Para el Entregable 2)* Consulta automática mediante servicio web en Java. |
| 3 | Encargado | Revisar solicitud y decidir | Tarea Humana + Compuerta (Gateway) | El encargado revisa en su bandeja y marca si se aprueba o rechaza. |
| 4 | Sistema / Encargado | Actualizar estado y avisar | Tarea Humana / Servicio | El caso cambia de estado (`Aprobado` o `Rechazado`) de forma ordenada. |
| Fin | Sistema | Fin del proceso | Evento de Fin (End Event) | Queda el historial completo de quién atendió el caso y cuándo terminó. |

## 4. Definición del Proceso Mínimo para Flowable (Semana 3)

Para cumplir con el flujo básico que pide la primera entrega (`Inicio -> Tarea humana -> Fin`), definimos la siguiente estructura para configurarla en [Flowable Cloud](https://www.google.com/search?q=https%3A%2F%2Fiplacex.cloud.flowable.com%2F):

* **Nombre del proceso en Flowable:** `Proceso Gestion de Solicitudes`
* **Clave (Key):** `proceso_solicitudes`
* **Flujo mínimo:** `Inicio de solicitud` -> `Tarea Humana: Revisar solicitud` -> `Fin de solicitud`

**Datos que tendrá el formulario de la Tarea Humana:**

| Campo en el formulario | Variable en Flowable | Tipo de dato | Obligatorio |
| :--- | :--- | :--- | :--- |
| Nombre del solicitante | `nombreSolicitante` | Texto (`Text`) | Sí |
| RUT o identificador | `rutSolicitante` | Texto (`Text`) | Sí |
| Correo de contacto | `correoContacto` | Texto (`Text`) | Sí |
| Tipo de solicitud | `tipoSolicitud` | Texto / Lista | Sí |
| Detalle del pedido | `detalleSolicitud` | Texto largo | Sí |
| Estado de la revisión | `estadoSolicitud` | Lista (`Aprobado` / `Rechazado` / `Pendiente`) | Sí |
| Comentarios del encargado | `observaciones` | Texto largo | No |

## 5. Actas de Reuniones Semanales

### Acta de Reunión 1 (Semana 2)

* **Fecha:** 14 de octubre de 2026
* **Modalidad:** Online / Coordinación por grupo
* **Asistentes:**
  * Felipe Octavio Quicel Soto (Analista)
  * Geraldine Andrea González Acuña (desconocido)
  * Elvis Brayan Gutiérrez Bernal (desconocido)
  * Lautaro Nelson Salinas Alvear (desconocido)

**Avances revisados:**
* Revisamos el documento de planificación del curso y las fechas de las entregas.
* Nos repartimos los primeros roles del equipo para las semanas 3 y 4.
* Se creó el repositorio del grupo en GitHub para ir subiendo los avances y las actas.
* Felipe avanzó con el documento base de análisis del proceso (AS-IS, TO-BE y los campos de la tarea humana para Flowable).

**Compromisos para la próxima reunión:**

| Tarea | Responsable | Fecha estimada | Estado |
| :--- | :--- | :--- | :--- |
| Enviar correo al profesor con los integrantes del grupo y consultar por el caso | Grupo | 15-10-2026 | En curso |
| Completar los datos específicos de la empresa cuando el profesor confirme el caso | Felipe Quicel | 19-10-2026 | Listo para rellenar |
| Ir armando el borrador de los diagramas AS-IS y TO-BE en BPMN | Geraldine González | 20-10-2026 | Pendiente |
| Probar el flujo básico (`Inicio -> Tarea humana -> Fin`) en Flowable Cloud | Elvis Gutiérrez | 20-10-2026 | Pendiente |
| Ordenar carpetas del repositorio y revisar que estén todos los archivos | Lautaro Salinas | 21-10-2026 | En curso |

**Bloqueos o dificultades:**
* Todavía no tenemos asignada la empresa/socio comunitario por parte del profesor. Para no atrasarnos, dejamos armada la estructura del análisis AS-IS y TO-BE y los campos base del formulario para ajustarlos apenas tengamos el caso definitivo.

### Acta de Reunión 2 (Plantilla para la siguiente reunión)

* **Fecha:** *(Por definir)*
* **Modalidad:** Online / Grupo de WhatsApp
* **Asistentes:** Felipe Quicel, Geraldine González, Elvis Gutiérrez, Lautaro Salinas.

**Revisión de tareas anteriores:**
* Envío de grupo al profesor: *(Completado / En espera)*
* Creación de repositorio y subida de Acta 1: Completado.

**Avances de la reunión:**
* *(Anotar aquí cuando reciban el caso de la empresa y revisen los primeros diagramas).*

**Compromisos acordados:**
* **Felipe Quicel:**
* **Geraldine González:** S
* **Elvis Gutiérrez:** 
* **Lautaro Salinas:** 

**Bloqueos o dificultades:**
* *(Anotar si hubo algún problema técnico con Flowable o dudas con el caso).*
