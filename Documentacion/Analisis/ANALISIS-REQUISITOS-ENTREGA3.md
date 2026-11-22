# ANÁLISIS DE REQUISITOS – ENTREGA 3

## Proyecto: Peluditos Pet Chile

**Rol:** Analista de Procesos / Elvis Gutiérrez Bernal

**Fecha de revisión:** 21 de noviembre de 2026

### 1. Introducción

El presente documento tiene como finalidad analizar los avances del proyecto Peluditos Pet Chile durante la tercera entrega, enfocándose en la integración de los procesos de negocio, servicios web, automatizaciones e interfaces de usuario mediante el motor Flowable Open Source.

El proyecto busca mejorar la gestión de consultas y el seguimiento comercial de los potenciales clientes, permitiendo registrar sus datos y los de sus mascotas, automatizar actividades y mantener información actualizada sobre los resultados comerciales.

Durante esta entrega, el equipo ha desarrollado y probado componentes que permiten avanzar desde funcionalidades individuales hacia una solución integrada para el emprendimiento.

### 2. Objetivo del análisis

Evaluar el funcionamiento del proceso comercial de Peluditos Pet Chile, verificando la integración del modelo BPMN TO-BE, Flowable OSS 7.2.0, servicios Jakarta REST, External Workers, base de datos SQLite e interfaces web.

Los objetivos específicos son:

- Identificar los requisitos funcionales y no funcionales del sistema.
- Analizar las actividades humanas y automáticas del proceso.
- Revisar los resultados de las pruebas realizadas por el equipo.
- Identificar problemas potenciales y oportunidades de mejora.
- Relacionar los requisitos con las evidencias disponibles.
- Proponer recomendaciones para fortalecer la solución.

### 3. Análisis del proceso actual

El proceso comercial de Peluditos Pet Chile tiene como finalidad gestionar las consultas de los potenciales clientes, registrar información sobre sus mascotas y realizar el seguimiento correspondiente.

El proceso TO-BE actualizado contempla las siguientes actividades:

1. El cliente realiza una consulta.
2. Se registran los datos del potencial cliente y su mascota.
3. Un External Worker guarda la información mediante un servicio REST.
4. Se atiende y responde la consulta del cliente.
5. Se realiza el seguimiento comercial.
6. Un gateway determina si la compra fue realizada.
7. Un External Worker actualiza el estado comercial.
8. El proceso finaliza como Compra realizada o Sin compra.

El modelo incorpora actividades humanas mediante User Tasks y actividades automatizadas mediante Service Tasks configuradas como External Workers.

Las campañas de cumpleaños y fechas especiales se encuentran planificadas para la Entrega 4 y no forman parte de las pruebas del flujo comercial principal de esta entrega.

#### 3.1 Componentes tecnológicos del proyecto

| Componente | Función |
|---|---|
| BPMN TO-BE | Representar las actividades, decisiones y resultados comerciales. |
| Flowable OSS 7.2.0 | Ejecutar y administrar las instancias del proceso. |
| Jakarta REST | Proporcionar servicios para registrar y consultar información. |
| External Workers | Ejecutar las actividades automáticas definidas en el BPMN. |
| Interfaz web | Permitir registrar consultas, clientes, mascotas y resultados comerciales. |
| SQLite | Almacenar la información del emprendimiento. |
| GitHub | Mantener el código, la documentación y las evidencias del equipo. |

#### 3.2 Estado del proceso

Durante la tercera entrega se documentaron pruebas de integración realizadas el 20 de noviembre de 2026.

Estas pruebas utilizaron Flowable OSS 7.2.0, servicios Jakarta REST y bases de datos locales de prueba.

Se ejecutaron los siguientes escenarios:

- **Ruta SÍ:** el cliente realiza una compra y su estado se actualiza a Compra realizada.
- **Ruta NO:** el cliente no realiza una compra y su estado se actualiza a Sin compra.

Ambos escenarios fueron documentados con resultado correcto, incluyendo actividades humanas, tareas automáticas, servicios REST, persistencia y eventos de finalización.

Las pruebas BPMN fueron ejecutadas mediante APIs locales. Por separado, las evidencias de implementación y QA muestran funcionalidades de registro, consulta y seguimiento mediante interfaces web.

### 4. Identificación de problemas y oportunidades de mejora

Durante la revisión del BPMN, formularios, código Java y evidencias de funcionamiento se identificaron aspectos que requieren atención para mejorar la confiabilidad del sistema.

#### 4.1 Problemas o riesgos identificados

1. **Valores predeterminados en el registro:** el Worker encargado de guardar clientes utiliza datos de ejemplo cuando determinadas variables no se encuentran disponibles. Esto podría generar registros incorrectos.

2. **Autorización comercial:** el Worker 1 utiliza un valor verdadero predeterminado para `clienteAutoriza`. Se recomienda evitar que una autorización promocional se considere concedida sin confirmación del cliente.

3. **Identificación del cliente:** el Worker 2 dispone de un mecanismo alternativo para obtener un ID si no recibe uno válido desde Flowable. Esto podría provocar la actualización de un registro distinto del correspondiente al proceso.

4. **Validación de decisiones:** el formulario de seguimiento utiliza la variable booleana `compraRealizada`, pero el campo no está marcado como obligatorio en su definición de respaldo.

5. **Validación de datos:** es necesario verificar que los campos obligatorios, teléfonos, correos electrónicos y fechas sean validados tanto en la interfaz como en el servidor.

6. **Pruebas automatizadas:** el archivo `AppTest.java` contiene una prueba básica que no comprueba directamente la integración de los External Workers.

7. **Preparación para producción:** las pruebas locales exitosas no demuestran por sí solas el cumplimiento de todos los requisitos de seguridad, rendimiento y disponibilidad.

Estos puntos representan riesgos identificados mediante el análisis del código y la documentación, y no necesariamente errores observados durante las pruebas.

#### 4.2 Oportunidades de mejora

Se recomienda:

- Exigir datos válidos antes de registrar clientes y mascotas.
- Evitar utilizar automáticamente datos ficticios en operaciones reales.
- Confirmar explícitamente la autorización para recibir promociones.
- Validar el identificador del cliente antes de actualizar el seguimiento.
- Garantizar que `compraRealizada` se encuentre definida correctamente.
- Incorporar pruebas automatizadas funcionales y de integración.
- Mantener los registros de errores y las evidencias de ejecución.
- Verificar la identidad visual institucional en las interfaces.
- Priorizar la estabilidad de los componentes existentes antes de desarrollar nuevas funcionalidades.

### 5. Definición de requisitos funcionales

Los requisitos funcionales describen las acciones necesarias para gestionar las consultas, los clientes y el seguimiento comercial.

| Código | Requisito funcional | Descripción |
|---|---|---|
| RF-01 | Registrar consultas | Permitir el ingreso de consultas mediante la interfaz web. |
| RF-02 | Iniciar procesos BPMN | Iniciar instancias del proceso comercial utilizando Flowable OSS. |
| RF-03 | Registrar resultado comercial | Registrar si el cliente concretó o no una compra y actualizar su estado. |
| RF-04 | Gestionar tareas humanas | Ejecutar las actividades que requieren intervención de usuarios mediante User Tasks. |
| RF-05 | Ejecutar tareas automáticas | Ejecutar actividades mediante Service Tasks y External Workers. |
| RF-06 | Integrar servicios REST | Intercambiar información entre los componentes mediante servicios Jakarta REST. |
| RF-07 | Almacenar información | Guardar y recuperar información de clientes, mascotas y seguimiento comercial. |
| RF-08 | Gestionar decisiones | Utilizar gateways para seleccionar la ruta correspondiente según `compraRealizada`. |
| RF-09 | Consultar información | Permitir consultar registros y estados comerciales desde la interfaz web. |
| RF-10 | Finalizar procesos | Completar correctamente las instancias del proceso en Flowable OSS. |

#### 5.1 Validación de requisitos

Las pruebas realizadas y documentadas durante la tercera entrega respaldan el cumplimiento de los requisitos funcionales en los escenarios evaluados.

Se verificaron:

- Inicio de procesos BPMN.
- Registro de clientes y mascotas.
- Ejecución de actividades humanas.
- Ejecución de los External Workers.
- Comunicación mediante servicios REST.
- Persistencia y consulta de información.
- Evaluación de las dos rutas del gateway.
- Finalización de instancias del proceso.
- Registro y consulta del seguimiento comercial mediante la interfaz web.

Las evidencias se encuentran relacionadas con cada requisito en el documento `MATRIZ-TRAZABILIDAD-ENTREGA3.md`.

La validación corresponde al alcance de las pruebas documentadas y no garantiza el funcionamiento en todos los escenarios posibles.

### 6. Definición de requisitos no funcionales

Los requisitos no funcionales establecen las condiciones de calidad necesarias para el funcionamiento adecuado del sistema.

| Código | Requisito no funcional | Descripción |
|---|---|---|
| RNF-01 | Seguridad | Proteger la información de los clientes y controlar el acceso a funciones administrativas. |
| RNF-02 | Rendimiento | Mantener tiempos de respuesta adecuados en los servicios y las interfaces. |
| RNF-03 | Disponibilidad | Mantener operativos los componentes necesarios durante la utilización del sistema. |
| RNF-04 | Usabilidad | Ofrecer interfaces claras para registrar y consultar información. |
| RNF-05 | Mantenibilidad | Organizar el código y la documentación para facilitar futuras modificaciones. |
| RNF-06 | Confiabilidad | Ejecutar las actividades correctamente y proteger la integridad de la información. |
| RNF-07 | Compatibilidad | Permitir la comunicación correcta entre Flowable, servicios REST y aplicaciones externas. |
| RNF-08 | Identidad visual | Incorporar los colores y el logotipo institucional de Peluditos Pet Chile. |

#### 6.1 Consideraciones de calidad

Las capturas de QA muestran que la interfaz dispone de mecanismos básicos de validación para campos obligatorios.

También se observan formularios organizados, opciones de consulta y mensajes de confirmación.

Por otra parte, el código de los External Workers incorpora mecanismos para reportar errores a Flowable y utiliza variables de entorno para configurar las conexiones.

No obstante, se recomienda complementar las pruebas con revisiones específicas de seguridad, rendimiento, disponibilidad y validación de datos en el servidor.

La identidad visual debe revisarse para asegurar la incorporación del logotipo institucional, además de los colores utilizados.

### 7. Análisis de integración de componentes

La tercera entrega busca demostrar que el proceso BPMN, el motor Flowable, los servicios REST, los External Workers y las interfaces trabajan de manera integrada.

#### 7.1 Componentes y responsabilidades

| Componente | Responsabilidad |
|---|---|
| Interfaz web | Registrar consultas, clientes, mascotas y resultados comerciales. |
| Flowable OSS | Ejecutar el BPMN y administrar las instancias del proceso. |
| User Tasks | Gestionar actividades realizadas por personas. |
| Service Tasks | Representar actividades automáticas del flujo. |
| External Worker 1 | Registrar clientes y mascotas mediante `POST /api/clientes`. |
| External Worker 2 | Actualizar el seguimiento mediante `PUT /api/clientes/{clienteId}/seguimiento`. |
| Jakarta REST | Procesar solicitudes y proporcionar acceso a la información. |
| SQLite | Almacenar clientes, mascotas y estados comerciales. |

#### 7.2 Funcionamiento de la integración

El proceso integrado contempla las siguientes etapas:

1. Se registra una consulta y se inicia el proceso correspondiente.
2. Flowable administra la instancia BPMN.
3. Se completan los datos del potencial cliente.
4. El Worker 1 registra la información mediante Jakarta REST.
5. Se ejecutan las actividades de atención y seguimiento.
6. El gateway evalúa la variable `compraRealizada`.
7. El Worker 2 actualiza el estado comercial del cliente.
8. SQLite conserva la información actualizada.
9. Flowable alcanza el evento de finalización correspondiente.

Las pruebas locales respaldan el funcionamiento de esta secuencia para ambas rutas comerciales.

#### 7.3 Validación de la integración

El equipo documentó las siguientes verificaciones:

**Ruta Compra realizada**

- Inicio del proceso mediante una solicitud REST: HTTP 202.
- Registro del cliente mediante Worker 1: HTTP 201.
- Seguimiento comercial: HTTP 200.
- Actualización del estado mediante Worker 2: HTTP 200.
- Estado final: Compra realizada.
- Evento de finalización alcanzado.

**Ruta Sin compra**

- Inicio del proceso mediante una solicitud REST: HTTP 202.
- Registro del cliente mediante Worker 1: HTTP 201.
- Seguimiento comercial: HTTP 200.
- Actualización del estado mediante Worker 2: HTTP 200.
- Estado final: Sin compra.
- Evento de finalización alcanzado.

Las pruebas utilizaron registros ficticios y bases de datos de prueba.

Los resultados quedaron documentados en `RESULTADO-PRUEBAS-BPMN.txt` y `resultados-rutas.json`.

### 8. Escenarios de prueba y criterios de aceptación

Los escenarios de prueba permiten comprobar que el sistema responde a los requisitos definidos.

#### 8.1 Escenarios de prueba

| Código | Escenario | Resultado documentado |
|---|---|---|
| CP-01 | Registrar consulta e iniciar proceso | Correcto en los escenarios documentados. |
| CP-02 | Cliente realiza una compra | Ruta SÍ ejecutada y finalizada correctamente. |
| CP-03 | Cliente no realiza una compra | Ruta NO ejecutada y finalizada correctamente. |
| CP-04 | Ejecutar tareas humanas | Las User Tasks aparecen completadas en el historial. |
| CP-05 | Ejecutar tareas automáticas | Los External Workers aparecen ejecutados correctamente. |
| CP-06 | Comunicación mediante REST | Se documentaron respuestas HTTP satisfactorias. |
| CP-07 | Almacenar y consultar información | Se verificaron registros y estados comerciales. |
| CP-08 | Finalizar procesos BPMN | Ambas instancias alcanzaron sus eventos de finalización. |

#### 8.2 Criterios de aceptación

Para considerar cumplidos los objetivos funcionales de esta entrega se establecen los siguientes criterios:

1. El proceso BPMN debe encontrarse desplegado y ejecutarse en Flowable OSS.
2. Las User Tasks deben completarse correctamente.
3. Los External Workers deben procesar las tareas automáticas.
4. Los servicios REST deben responder adecuadamente.
5. Los registros deben almacenarse y poder consultarse.
6. El gateway debe seleccionar correctamente ambas rutas comerciales.
7. Las instancias deben finalizar en sus eventos correspondientes.
8. La interfaz web debe permitir las operaciones principales del negocio.
9. Las pruebas deben contar con evidencias verificables.

Las evidencias revisadas respaldan estos criterios en los escenarios probados, con las limitaciones descritas en el presente informe.

#### 8.3 Registro de resultados

Las principales evidencias revisadas corresponden a:

**BPMN**

- `RESULTADO-PRUEBAS-BPMN.txt`
- `resultados-rutas.json`
- `resultado-ambas-rutas.png`

**Implementación**

- `IMP_Formulario_Cliente_Completado.png`
- `IMP_Cliente_Simon_Registrado.png`
- `IMP_Clientes_Registrados_6.png`
- `IMP_Panel_Emprendedora.png`

**QA**

- `QA_Seguimiento_Enviado_OK.png`
- `QA_Cliente8_Compra_Realizada.png`
- `QA_Validacion_Campos_Obligatorios.png`

Estas evidencias respaldan diferentes aspectos de la implementación, incluyendo ejecución BPMN, persistencia, registro, consulta, seguimiento comercial y validación básica de formularios.

Las pruebas BPMN se realizaron mediante APIs locales, por lo que no constituyen por sí solas una validación completa de la experiencia de usuario desde la interfaz pública.

### 9. Conclusiones y recomendaciones

#### 9.1 Conclusiones

El análisis realizado para la tercera entrega de Peluditos Pet Chile permite identificar avances importantes en la integración de los componentes tecnológicos del proyecto.

El equipo documentó la ejecución de ambas rutas del proceso comercial mediante Flowable OSS, incluyendo tareas humanas, automatizaciones, decisiones y eventos de finalización.

Además, las evidencias de implementación y QA muestran funcionalidades para registrar clientes y mascotas, consultar información y actualizar estados comerciales mediante interfaces web.

Estos resultados respaldan el funcionamiento de la solución dentro del alcance de las pruebas realizadas.

Sin embargo, el análisis del código permitió identificar riesgos relacionados con valores predeterminados, autorización comercial, identificación de clientes y cobertura de pruebas automatizadas.

#### 9.2 Recomendaciones del Analista

Se recomienda al equipo considerar las siguientes acciones:

1. **Mantener la integración funcional:** priorizar la estabilidad del proceso BPMN y los servicios existentes.

2. **Fortalecer las validaciones:** comprobar los datos obligatorios y evitar registros generados con información de ejemplo cuando se trate de operaciones reales.

3. **Proteger la autorización comercial:** asegurar que las promociones se envíen únicamente cuando corresponda y exista autorización.

4. **Validar la identificación del cliente:** exigir un identificador correcto antes de actualizar los estados comerciales.

5. **Mejorar las pruebas automatizadas:** incorporar casos que verifiquen directamente los External Workers y la comunicación REST.

6. **Mantener las evidencias:** conservar los historiales de Flowable, respuestas REST, capturas y resultados de QA.

7. **Fortalecer la interfaz:** verificar la usabilidad y la incorporación del logotipo y colores institucionales.

8. **Continuar el trabajo colaborativo:** mantener actas, compromisos, asignación de responsabilidades y commits significativos.

9. **Preparar la siguiente entrega:** abordar los riesgos pendientes antes de activar el flujo de campañas de cumpleaños y fechas especiales.

#### 9.3 Conclusión final

Desde el rol de Analista de Procesos, se concluye que Peluditos Pet Chile presenta avances significativos hacia una solución integrada y funcional para la gestión de consultas y seguimiento comercial.

Las evidencias documentadas demuestran el funcionamiento de las rutas Compra realizada y Sin compra, así como la participación de Flowable OSS, External Workers, servicios Jakarta REST y SQLite.

Asimismo, las interfaces revisadas permiten observar funcionalidades relacionadas con el registro, consulta y seguimiento de clientes.

Se recomienda continuar fortaleciendo las validaciones y las pruebas del sistema, procurando que las funcionalidades existentes mantengan su estabilidad antes de incorporar nuevos procesos.

De esta manera, el equipo podrá avanzar hacia una solución más confiable, mantenible y alineada con las necesidades reales del emprendimiento.
