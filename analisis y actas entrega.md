# Primera entrega - Analisis de Peluditos Pet Chile y actas

**Asignatura:** Integración de Plataformas  
**Profesor:** Santiago Neira  
**Fecha:** 20 de octubre de 2026  

## 1. Integrantes, roles y herramientas del grupo

El 18 de octubre terminamos de confirmar por WhatsApp los roles de cada uno para esta primera entrega (Semanas 3 y 4):

| Integrante | Rol en esta entrega |
| --- | --- |
| Felipe Octavio Quicel Soto | Analista de procesos y repositorio |
| Elvis Brayan Gutiérrez Bernal | Modelador BPMN (AS-IS y TO-BE) |
| Geraldine Andrea González Acuña | Implementadora en Flowable |
| Lautaro Nelson Salinas Alvear | Tester y revisión de pruebas |

Sobre la duda que salió en el grupo por el rol de **Integrador**: como los servicios web en Java recién entran en la Semana 5 (Entregable 2), en esta primera entrega trabajamos con estos 4 roles y en la rotación de la Semana 5 asignamos al integrador.

**Programas acordados:** para no tener problemas de compatibilidad entre nosotros, acordamos trabajar con **Flowable Cloud** (con el correo de IPLACEX), **Visual Studio Code**, **Java 21** y **Maven**.

## 2. Qué problema vamos a resolver

Peluditos Pet Chile es un emprendimiento que hace y vende pastelería para mascotas (perros y gatos). Hoy reciben las consultas por redes sociales y WhatsApp, pero no tienen un registro ordenado de la gente que pregunta ni seguimiento de quiénes compran.

El caso tiene dos partes:

**Gestión de potenciales clientes (leads):** anotar los datos de quien consulta, por qué red social llegó, qué seguimiento se le hizo y si finalmente compró o no (y como comentó Lautaro en el grupo, si no compra, dejar anotado el motivo para saber qué ofrecerle más adelante).
**Oportunidades de recompra:** tener registradas fechas importantes (cumpleaños de la mascota, Navidad, Halloween, Día del Perro/Gato, etc.) para volver a contactar a los clientes y ofrecerles promociones.

Para esta primera entrega partimos con el proceso base de **registro y seguimiento de consultas (leads)** para dejar andando el primer flujo en Flowable, y en las siguientes entregas sumaremos la parte de fechas y servicios web.

### Participantes del proceso (Lanes)

**Cliente / Interesado:** persona que escribe preguntando por tortas o snacks para su mascota.
**Encargada de atención (Peluditos Pet Chile):** quien recibe la consulta, responde, hace el seguimiento y anota el resultado.

## 3. Proceso actual (AS-IS)

Según el documento del caso, hoy el proceso funciona así:

Las consultas llegan por distintos lados (Instagram, Facebook, TikTok, WhatsApp).
Algunas personas compran y otras solo preguntan precios, diseños o fechas de entrega.
No hay una planilla o sistema formal para anotar las consultas ni hacerles seguimiento.
Tampoco hay un registro que avise cuando se acerca el cumpleaños de una mascota o una fecha comercial.

| Paso | Quién participa | Qué pasa hoy |
| --- | --- | --- |
| 1 | Cliente / Interesado | Manda una consulta por redes sociales o WhatsApp. |
| 2 | Encargada | Responde la consulta por el mismo chat. |
| 3 | Cliente y encargada | Si quiere comprar se coordina el pedido; si no, la conversación queda hasta ahí sin registro de seguimiento. |

**Flujo actual:** consulta recibida -> responder consulta -> ¿quiere comprar? -> coordinar pedido o terminar consulta.

### Revisión de los diagramas en Flowable

El 17 de octubre Elvis mandó el primer avance del AS-IS por el grupo y el 19 de octubre dejó subidos los dos diagramas (AS-IS y TO-BE) en Flowable Cloud.

Al revisarlos entre el grupo vimos lo siguiente:

En el **AS-IS** Elvis corrigió un detalle que le tiraba error en Flowable y quedó listo mostrando cómo se atienden hoy las consultas y la decisión de si coordina pedido o se cierra.
En el **TO-BE** Geraldine probó el diagrama en Flowable Design con la simulación de tokens y verificó que los dos caminos llegan bien al final.
Queda pendiente descargar también los archivos `.bpmn20.xml` y las capturas para dejarlos respaldados en las carpetas de este repositorio.

## 4. Proceso propuesto (TO-BE preliminar)

En el TO-BE la idea es que cada consulta se registre en Flowable para llevar el control del seguimiento y saber qué pasó con cada interesado.

| Paso | Actividad | Qué se guarda |
| --- | --- | --- |
| 1 | Registrar consulta | Nombre, contacto, red social por donde habló y qué producto busca. |
| 2 | Hacer seguimiento | Fecha, contacto realizado y comentario. |
| 3 | Evaluar resultado (Compuerta) | Ver si concretó la compra, si sigue pendiente o si no compró (anotando el motivo). |
| 4 | Cerrar registro | Si compró, queda guardado como cliente para futuras promociones; si no compró, se cierra dejando el historial. |

**Flujo propuesto:** inicio -> registrar consulta -> hacer seguimiento -> registrar resultado -> compuerta exclusiva (compró / pendiente / no concretó) -> fin.

## 5. Proceso mínimo para Flowable (Semana 3)

Para cumplir con el flujo básico ejecutable (`Inicio -> Tarea humana -> Fin`) en [Flowable Cloud](https://www.google.com/search?q=https%3A%2F%2Fiplacex.cloud.flowable.com%2F):

**Nombre del proceso:** Registro de consultas Peluditos Pet
**Clave:** `registro_consultas_peluditos`
**Flujo base:** Inicio -> Tarea humana (Registrar consulta y seguimiento) -> Fin

### Campos para el formulario en Flowable

| Campo | Variable | Tipo |
| --- | --- | --- |
| Nombre del interesado | `nombreInteresado` | Texto (obligatorio) |
| Medio de contacto | `medioContacto` | Lista o texto (teléfono, usuario red social o correo) |
| Dato de contacto | `datoContacto` | Texto (obligatorio) |
| Canal por donde llegó | `canalOrigen` | Lista (Instagram, Facebook, TikTok, WhatsApp, Otro) |
| Motivo o producto consultado | `motivoConsulta` | Texto (obligatorio) |
| Resultado de la consulta | `resultadoLead` | Lista (Compró, En seguimiento, No concretó) |
| Observaciones / Motivo de no compra | `observaciones` | Texto opcional (sirve también para anotar cumpleaños de la mascota) |

### Pruebas que vamos a hacer para la entrega

1. Iniciar el proceso en Flowable.
2. Llenar la tarea con datos de un cliente de prueba de Peluditos Pet.
3. Probar que el flujo avance bien por las opciones hasta terminar.
4. Sacar capturas o grabar un video corto de la prueba y subirlo al repositorio.

## 6. Dudas anotadas para el negocio

Puntos que dejamos anotados como equipo por si se precisan más adelante:

1. ¿Usan hoy algún cuaderno o Excel aparte de los chats de redes sociales?
2. ¿Cuánto tiempo esperan para volver a hablarle a alguien que pidió precio y no contestó más?
3. ¿En qué momento exacto cuentan el pedido como compra cerrada (cuando abona o cuando se entrega)?
4. ¿Con cuántos días de anticipación les acomoda recibir el aviso antes del cumpleaños de una mascota o de una fecha como Navidad?

## 7. Actas de reuniones del grupo

### Acta 1 - Semana del 13 al 16 de octubre de 2026

**Modalidad:** Coordinación por grupo de WhatsApp.
**Participantes:** Felipe Quicel, Geraldine González, Elvis Gutiérrez y Lautaro Salinas.

**Temas vistos y avances:**

Nos integramos los 4 al grupo de WhatsApp y revisamos las fechas del curso.
Felipe propuso partir como Analista y creó este repositorio en GitHub el 15 de octubre.
Revisamos cómo entrar a Flowable porque al principio el link daba problemas con la licencia.
El 16 de octubre salió el caso de **Peluditos Pet Chile** y Geraldine propuso trabajar todos con **Java 21, Maven y Visual Studio Code**, quedando todos de acuerdo.

**Tareas que quedaron de esa reunión:**

| Tarea | Responsable | Fecha | Estado |
| --- | --- | --- | --- |
| Crear repositorio en GitHub y armar documento base | Felipe Quicel | 15-10-2026 | Listo |
| Revisar acceso a Flowable Cloud con cuenta IPLACEX | Elvis y Geraldine | 15-10-2026 | Listo |
| Repartir los roles definitivos viendo el caso de Peluditos Pet | Grupo | 18-10-2026 | Listo |

**Dificultades o bloqueos:**

El link de Flowable al principio bloqueaba la entrada y pedía licencia, pero Geraldine le consultó al profesor (quien avisó que estaban cargando las cuentas de los alumnos) y luego Elvis confirmó que el link nuevo ya entraba directo.
Al inicio de la semana aún no teníamos el PDF del caso, lo que quedó solucionado cuando subieron el documento de Peluditos Pet Chile.

### Acta 2 - Del 17 al 20 de octubre de 2026

**Modalidad:** Coordinación por WhatsApp y pruebas en Flowable Cloud.
**Participantes:**
Felipe Quicel (Analista)
Elvis Gutiérrez (Modelador BPMN)
Geraldine González (Implementadora Flowable)
Lautaro Salinas (Tester)

**Revisión de tareas anteriores:**

Repositorio creado: Listo.
Cuentas de Flowable funcionando: Listo.
Roles confirmados por los 4 en el grupo: Listo.

**Avances de estos días:**

Elvis armó primero el borrador del AS-IS el sábado 17 y el lunes 19 dejó subidos los dos modelos (AS-IS y TO-BE) en Flowable.
Lautaro revisó el requerimiento y comentó que además de ver si el cliente compra, hay que considerar el seguimiento desde la consulta (saber por qué no compró para poder ofrecerle cosas que le sirvan después, y mandar promociones a los que sí compraron).
Felipe actualizó este documento de análisis con el caso de Peluditos Pet Chile y las observaciones del grupo.
Geraldine probó el modelo TO-BE en Flowable Design con la simulación de tokens y confirmó que los dos caminos llegan bien al final.
Elvis arregló un error que le salía en el modelo AS-IS el lunes 19 y Geraldine empezó a armar la parte de implementación en Flowable.

**Compromisos para cerrar la Entrega 1:**

| Tarea | Responsable | Fecha tope | Estado |
| --- | --- | --- | --- |
| Dejar actualizado en GitHub el análisis de Peluditos Pet y las dos actas | Felipe Quicel | 20-10-2026 | Listo |
| Subir al repositorio los archivos `.bpmn` e imágenes de los diagramas AS-IS y TO-BE | Elvis Gutiérrez | 22-10-2026 | En curso (ya están en Flowable) |
| Terminar la implementación ejecutable en Flowable | Geraldine González | 23-10-2026 | En curso |
| Hacer las pruebas de ejecución y subir capturas o video de evidencia a GitHub | Lautaro Salinas y Geraldine | 24-10-2026 | En curso |

**Dificultades o bloqueos:**

A Elvis le salía un error en el modelo AS-IS al probarlo en Flowable el lunes 19, pero lo pudo corregir esa misma tarde para que Geraldine siguiera con la implementación.
