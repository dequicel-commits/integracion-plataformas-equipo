# MATRIZ DE TRAZABILIDAD DE REQUISITOS – ENTREGA 3

## Proyecto: Peluditos Pet Chile

**Rol:** Analista de Procesos / Elvis Gutiérrez

**Fecha de revisión:** 21 de noviembre de 2026

### 1. Objetivo

La presente matriz tiene como objetivo relacionar los requisitos funcionales del proyecto Peluditos Pet Chile con los componentes tecnológicos implementados, los casos de prueba y las evidencias obtenidas durante la tercera entrega.

El análisis considera el funcionamiento integrado del proceso BPMN TO-BE, Flowable Open Source 7.2.0, los External Workers, los servicios Jakarta REST, la base de datos SQLite y las interfaces web.

El propósito es identificar los requisitos que cuentan con evidencia de funcionamiento, sus limitaciones y las mejoras recomendadas antes de la siguiente entrega.

### 2. Matriz de trazabilidad

| Código | Requisito funcional | Componente relacionado | Caso de prueba | Estado |
|---|---|---|---|---|
| RF-01 | Registrar consultas | Interfaz web / Jakarta REST | CP-01 | Aprobado en pruebas documentadas |
| RF-02 | Iniciar procesos BPMN | Flowable Open Source | CP-01 | Aprobado |
| RF-03 | Registrar resultado comercial | Interfaz web / Flowable / REST | CP-02 | Aprobado para seguimiento comercial |
| RF-04 | Gestionar tareas humanas | Flowable / User Tasks | CP-04 | Aprobado en pruebas locales |
| RF-05 | Ejecutar tareas automáticas | Service Tasks / External Workers | CP-05 | Aprobado |
| RF-06 | Integrar servicios REST | Jakarta REST / Flowable / Workers | CP-06 | Aprobado en pruebas locales |
| RF-07 | Almacenar información | Servicios REST / SQLite | CP-07 | Aprobado en pruebas locales |
| RF-08 | Gestionar decisiones | BPMN / Gateway exclusivo | CP-02 y CP-03 | Aprobado |
| RF-09 | Consultar información | Interfaz web / Servicios REST | CP-07 | Aprobado en pruebas documentadas |
| RF-10 | Finalizar procesos | Flowable Open Source | CP-08 | Aprobado |

### 2.1 Evidencias utilizadas

| Requisito | Evidencias principales |
|---|---|
| RF-01 | IMP_Formulario_Cliente_Completado.png, IMP_Cliente_Simon_Registrado.png, QA_Validacion_Campos_Obligatorios.png |
| RF-02 | RESULTADO-PRUEBAS-BPMN.txt, resultados-rutas.json |
| RF-03 | QA_Seguimiento_Enviado_OK.png, QA_Cliente8_Compra_Realizada.png |
| RF-04 | resultados-rutas.json |
| RF-05 | RESULTADO-PRUEBAS-BPMN.txt, resultados-rutas.json |
| RF-06 | RESULTADO-PRUEBAS-BPMN.txt, resultado-ambas-rutas.png |
| RF-07 | resultado-ambas-rutas.png, IMP_Cliente_Simon_Registrado.png |
| RF-08 | resultados-rutas.json |
| RF-09 | IMP_Clientes_Registrados_6.png, QA_Cliente8_Compra_Realizada.png |
| RF-10 | RESULTADO-PRUEBAS-BPMN.txt, resultados-rutas.json |

Las evidencias se encuentran organizadas en las carpetas `Evidencias/BPMN`, `Evidencias/IMPLEMENTACION` y `Evidencias/QA` del repositorio GitHub del equipo.

### 3. Estados de validación

Los estados utilizados para el seguimiento son:

- **Aprobado:** el requisito cuenta con evidencia de cumplimiento en los escenarios probados.
- **Aprobado con alcance limitado:** el requisito funciona en los escenarios documentados, pero no se ha demostrado su cumplimiento en todas las condiciones posibles.
- **Rechazado:** el requisito fue probado y no cumplió el resultado esperado.
- **Pendiente de validar:** todavía no existe evidencia suficiente para confirmar su cumplimiento.

Los resultados de esta matriz corresponden a las pruebas documentadas de la tercera entrega y no representan una certificación de funcionamiento en producción.

### 4. Seguimiento de requisitos

#### 4.1 Resultados de ejecución BPMN

Durante las pruebas del 20 de noviembre de 2026 se verificaron las dos rutas principales del proceso TO-BE en Flowable OSS 7.2.0.

**Ruta SÍ — Compra realizada**

- El proceso inició correctamente mediante una solicitud REST.
- El Worker 1 registró los datos del cliente y su mascota.
- Se ejecutaron las tareas humanas de atención y seguimiento.
- El gateway evaluó `compraRealizada=true`.
- El Worker 2 actualizó el estado comercial a Compra realizada.
- El proceso alcanzó su evento de finalización.

**Resultado:** Correcto.

**Ruta NO — Sin compra**

- El proceso inició correctamente.
- El Worker 1 registró los datos del cliente y su mascota.
- Se ejecutaron las tareas humanas correspondientes.
- El gateway evaluó `compraRealizada=false`.
- El Worker 2 actualizó el estado comercial a Sin compra.
- El proceso alcanzó su evento de finalización.

**Resultado:** Correcto.

Los resultados quedaron respaldados mediante el historial de actividades de Flowable, respuestas HTTP y registros de clientes.

#### 4.2 Resultados de implementación

Las capturas de implementación muestran:

- Formularios para registrar clientes y mascotas.
- Registro de información de potenciales clientes.
- Panel de gestión del emprendimiento.
- Consulta de clientes y mascotas registrados.
- Visualización de los estados comerciales.
- Integración documentada con los servicios REST y la base de datos SQLite.

Estos resultados respaldan el funcionamiento de las funcionalidades principales de registro y consulta.

#### 4.3 Resultados de QA

Las evidencias QA revisadas muestran:

- Registro del seguimiento comercial desde la interfaz.
- Visualización del estado Compra realizada para el cliente con ID 8.
- Consulta de clientes y sus mascotas.
- Validación de un campo obligatorio mediante el navegador.
- Evidencias adicionales disponibles de servicios REST, Workers y Flowable.

Las capturas del seguimiento muestran coherencia entre el resultado enviado y el estado posterior del cliente.

#### 4.4 Riesgos y oportunidades de mejora

Durante la revisión del modelo BPMN, los formularios y el código Java de los External Workers se identificaron los siguientes aspectos:

1. **Validación de datos:** revisar que los campos obligatorios sean validados tanto en la interfaz como en los servicios.

2. **Autorización comercial:** evitar que la autorización para recibir promociones se asigne automáticamente como verdadera cuando no exista consentimiento.

3. **Identificación del cliente:** verificar que el Worker 2 utilice el identificador correcto del cliente antes de actualizar su seguimiento.

4. **Valores predeterminados:** revisar el uso de datos de ejemplo en el Worker 1 para evitar registros incorrectos cuando falten variables.

5. **Pruebas automatizadas:** fortalecer `AppTest.java`, ya que actualmente contiene una prueba básica que no comprueba la integración real.

6. **Identidad visual:** comprobar la incorporación del logotipo institucional en la interfaz pública.

7. **Preparación para producción:** complementar las pruebas locales con revisiones de seguridad, manejo de errores y funcionamiento en escenarios adicionales.

Estos aspectos constituyen recomendaciones de mejora y no implican que todos correspondan a fallos observados durante las pruebas exitosas.

### 5. Conclusión

El análisis de trazabilidad realizado para la tercera entrega permite establecer que el equipo de Peluditos Pet Chile cuenta con evidencias de funcionamiento del proceso comercial integrado.

Los resultados documentados respaldan la ejecución de las rutas Compra realizada y Sin compra mediante Flowable Open Source, la intervención de tareas humanas y automáticas, la comunicación con servicios Jakarta REST y la persistencia de datos en SQLite.

Asimismo, las capturas de implementación y QA muestran funcionalidades de registro, consulta y seguimiento comercial mediante interfaces web.

Desde el rol de Analista de Procesos, se considera que el proyecto presenta avances significativos en la integración de sus componentes y en el cumplimiento de los requisitos funcionales definidos para esta entrega.

Se recomienda continuar fortaleciendo las validaciones, las pruebas automatizadas y el manejo de errores, manteniendo los resultados y evidencias actualizados en GitHub.

El flujo de campañas de cumpleaños y fechas especiales permanece planificado para la Entrega 4 y no se considera validado en esta matriz.
