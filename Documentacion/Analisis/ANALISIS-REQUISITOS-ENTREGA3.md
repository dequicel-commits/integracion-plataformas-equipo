# ANÁLISIS DE REQUISITOS – ENTREGA 3

## Proyecto: Peluditos Pet Chile

**Rol:** Analista de Procesos/ Elvis Gutiérrez Bernal

### 1. Introducción

El presente documento tiene como finalidad analizar los avances del proyecto Peluditos Pet Chile durante la tercera entrega, enfocándose en la integración de los procesos de negocio, servicios web e interfaces de usuario mediante el motor de procesos Flowable Open Source.

El proyecto busca mejorar la gestión de consultas y ventas de alimentos para mascotas, permitiendo organizar las actividades comerciales y automatizar tareas mediante tecnologías de integración de plataformas.

### 2. Objetivo del análisis

Evaluar el funcionamiento del proceso de ventas de Peluditos Pet Chile, identificando los requisitos necesarios para integrar correctamente el modelo BPMN, los servicios REST, las tareas automáticas y las interfaces de usuario.

Además, se busca identificar oportunidades de mejora que permitan entregar una solución estable, funcional y alineada con las necesidades del emprendimiento.

### 3. Análisis del proceso actual

El proceso de ventas de Peluditos Pet Chile tiene como finalidad gestionar las consultas de los clientes y facilitar la compra de alimentos para perros y gatos.

El proceso comienza cuando un cliente realiza una consulta sobre los productos disponibles. Posteriormente, se atiende su solicitud y se determina si desea realizar una compra.

Si el cliente decide comprar, el proceso continúa con el registro de la venta, la preparación del pedido y su entrega. En caso contrario, se proporciona la información solicitada y finaliza el proceso.

### 3.1 Componentes tecnológicos del proyecto

Para apoyar la automatización del proceso de ventas, el equipo ha trabajado con los siguientes componentes:

- **BPMN:** permite representar las actividades, decisiones y secuencia del proceso comercial.
- **Flowable Open Source:** motor encargado de ejecutar y administrar las instancias del proceso.
- **Servicios REST:** permiten intercambiar información entre los componentes del sistema.
- **External Workers:** permiten ejecutar actividades automáticas mediante servicios externos.
- **Interfaz web:** facilita la interacción de los usuarios con el proceso de negocio.
- **Base de datos:** permite almacenar información relacionada con las operaciones del emprendimiento.

### 3.2 Estado del proceso

Durante las entregas anteriores se desarrollaron los modelos BPMN AS-IS y TO-BE, y se realizaron pruebas de ejecución utilizando Flowable Open Source.

Para la tercera entrega, el principal desafío consiste en verificar que el proceso BPMN, los servicios REST, las tareas automáticas y la interfaz web funcionen de manera integrada.

Esta validación permitirá identificar posibles problemas de comunicación entre los componentes y establecer las mejoras necesarias antes de la entrega final.

### 4. Identificación de problemas y oportunidades de mejora

Durante el análisis del proceso comercial de Peluditos Pet Chile se identifican los siguientes aspectos que requieren revisión para asegurar el correcto funcionamiento de la solución integrada.

#### 4.1 Problemas o riesgos identificados

1. **Integración entre componentes:** es necesario verificar que Flowable, los servicios REST y la interfaz web intercambien información correctamente.

2. **Automatización de actividades:** se debe comprobar que las tareas automáticas se ejecuten mediante Service Tasks o External Workers sin intervención manual innecesaria.

3. **Validación de datos:** es importante asegurar que la información ingresada por los clientes sea correcta antes de procesarla.

4. **Manejo de errores:** se requiere revisar cómo responde el sistema ante fallas de comunicación o indisponibilidad de los servicios.

5. **Pruebas del proceso:** es necesario validar los diferentes escenarios de ejecución, incluyendo clientes que realizan una compra y clientes que solamente solicitan información.

#### 4.2 Oportunidades de mejora

Para fortalecer el funcionamiento de la solución se proponen las siguientes acciones:

- Verificar la comunicación entre Flowable y los servicios REST.
- Validar que las tareas humanas y automáticas estén correctamente diferenciadas.
- Incorporar validaciones en los formularios para evitar datos incompletos.
- Implementar mecanismos de control y registro de errores.
- Realizar pruebas de extremo a extremo para comprobar el funcionamiento del proceso.
- Mejorar la interfaz web incorporando los colores y el logotipo institucional de Peluditos Pet Chile.

Estas mejoras permitirán avanzar hacia una solución más estable, integrada y alineada con los objetivos del emprendimiento.

### 5. Definición de requisitos funcionales

Los requisitos funcionales describen las acciones que debe realizar el sistema de Peluditos Pet Chile para gestionar correctamente el proceso de ventas.

| Código | Requisito funcional | Descripción |
|---|---|---|
| RF-01 | Registrar consultas | El sistema debe permitir ingresar las consultas realizadas por los clientes mediante una interfaz web. |
| RF-02 | Iniciar procesos BPMN | El sistema debe permitir iniciar instancias del proceso de ventas utilizando Flowable Open Source. |
| RF-03 | Registrar ventas | El sistema debe permitir registrar la información de una venta cuando el cliente decide comprar. |
| RF-04 | Gestionar tareas humanas | Flowable debe asignar y administrar las actividades que requieren intervención de una persona. |
| RF-05 | Ejecutar tareas automáticas | El sistema debe permitir ejecutar actividades automáticas mediante Service Tasks o External Workers. |
| RF-06 | Integrar servicios REST | Los componentes deben intercambiar información correctamente mediante servicios REST. |
| RF-07 | Almacenar información | El sistema debe permitir guardar y consultar los datos necesarios para el proceso comercial. |
| RF-08 | Gestionar decisiones | El proceso BPMN debe utilizar gateways para determinar el camino correspondiente según la decisión del cliente. |
| RF-09 | Consultar información | La interfaz debe permitir consultar información relevante sobre las operaciones registradas. |
| RF-10 | Finalizar procesos | Flowable debe permitir completar las actividades y finalizar correctamente las instancias del proceso. |

### 5.1 Validación de requisitos

Los requisitos definidos deberán comprobarse mediante pruebas funcionales y de integración.

Las pruebas deberán considerar los siguientes escenarios:

- Cliente que realiza una consulta sin concretar una compra.
- Cliente que realiza una consulta y decide comprar.
- Ejecución de tareas automáticas mediante servicios externos.
- Registro y consulta de información mediante servicios REST.
- Finalización correcta del proceso BPMN en Flowable.

La validación de estos requisitos permitirá determinar el nivel de cumplimiento de la solución y detectar los componentes que requieren ajustes antes de la entrega final.

### 6. Definición de requisitos no funcionales

Los requisitos no funcionales establecen las condiciones de calidad que debe cumplir la solución tecnológica de Peluditos Pet Chile para garantizar un funcionamiento adecuado.

| Código | Requisito no funcional | Descripción |
|---|---|---|
| RNF-01 | Seguridad | El sistema debe proteger la información de los clientes y controlar el acceso a las funciones administrativas. |
| RNF-02 | Rendimiento | Los servicios REST deben responder de manera eficiente a las solicitudes realizadas desde la interfaz web. |
| RNF-03 | Disponibilidad | Flowable y los servicios necesarios deben encontrarse disponibles durante la operación del sistema. |
| RNF-04 | Usabilidad | La interfaz web debe ser sencilla y permitir que los usuarios ingresen y consulten información fácilmente. |
| RNF-05 | Mantenibilidad | El código y la documentación deben estar organizados para facilitar futuras modificaciones. |
| RNF-06 | Confiabilidad | El sistema debe ejecutar correctamente las actividades del proceso y evitar pérdidas de información. |
| RNF-07 | Compatibilidad | Los componentes deben comunicarse correctamente mediante APIs REST y formatos de intercambio de datos compatibles. |
| RNF-08 | Identidad visual | La interfaz pública debe incorporar el logotipo y los colores institucionales de Peluditos Pet Chile. |

### 6.1 Consideraciones de calidad

Para verificar estos requisitos se recomienda realizar pruebas de funcionamiento, revisar las respuestas de los servicios REST y comprobar que la interfaz web permita utilizar correctamente las funcionalidades implementadas.

También se debe revisar el manejo de errores, la protección de los datos y la estabilidad de la comunicación entre los componentes.

Estas verificaciones contribuirán a mejorar la calidad de la solución antes de la entrega final.

### 7. Análisis de integración de componentes

La tercera entrega del proyecto Peluditos Pet Chile tiene como objetivo consolidar la integración de los componentes tecnológicos desarrollados durante las etapas anteriores.

Para cumplir este propósito, es necesario verificar que el proceso BPMN, el motor Flowable Open Source, los servicios REST, los External Workers, la base de datos y la interfaz web trabajen de manera coordinada.

### 7.1 Componentes y responsabilidades

| Componente | Responsabilidad |
|---|---|
| Interfaz web | Permitir que los usuarios ingresen consultas y visualicen información relacionada con el proceso comercial. |
| Flowable Open Source | Ejecutar el proceso BPMN y administrar las actividades y decisiones del flujo. |
| User Tasks | Representar actividades que requieren intervención humana. |
| Service Tasks | Representar actividades automatizadas dentro del proceso BPMN. |
| External Workers | Ejecutar tareas automáticas mediante aplicaciones o servicios externos. |
| Servicios REST | Permitir el intercambio de información entre los componentes del sistema. |
| Base de datos SQLite | Almacenar la información necesaria para las operaciones comerciales. |

### 7.2 Funcionamiento esperado de la integración

El funcionamiento integrado del sistema debe considerar las siguientes etapas:

1. El cliente ingresa una consulta mediante la interfaz web.
2. El sistema envía la información al componente encargado de iniciar el proceso en Flowable.
3. Flowable ejecuta el proceso BPMN y determina las actividades correspondientes.
4. Las tareas humanas son realizadas por los usuarios responsables.
5. Las tareas automáticas se ejecutan mediante Service Tasks o External Workers, según la implementación.
6. Los servicios REST permiten registrar o consultar información en la base de datos.
7. El proceso continúa según las decisiones definidas en los gateways.
8. Finalmente, Flowable registra la finalización de la instancia del proceso.

### 7.3 Validación de la integración

Para comprobar el funcionamiento de la solución, se deberán realizar pruebas que permitan verificar:

- La comunicación entre la interfaz web y el servicio correspondiente.
- El inicio correcto de las instancias del proceso BPMN.
- La ejecución de las tareas humanas y automáticas.
- El funcionamiento de los servicios REST.
- El almacenamiento y consulta de información.
- La finalización correcta de las instancias en Flowable.

Los resultados de estas pruebas deberán documentarse mediante capturas de pantalla, respuestas de servicios y registros de ejecución.

Este análisis representa el funcionamiento esperado de la solución. El cumplimiento efectivo de cada componente deberá confirmarse mediante las pruebas realizadas por el equipo.

### 8. Escenarios de prueba y criterios de aceptación

Para verificar el funcionamiento de la solución integrada de Peluditos Pet Chile, se definen los siguientes escenarios de prueba.

Estas pruebas permitirán comprobar que los componentes del sistema trabajan correctamente y que el proceso comercial responde a las necesidades del emprendimiento.

### 8.1 Escenarios de prueba

| Código | Escenario | Resultado esperado |
|---|---|---|
| CP-01 | Cliente realiza una consulta | La interfaz permite ingresar la información y se inicia correctamente el proceso correspondiente. |
| CP-02 | Cliente decide realizar una compra | El proceso continúa con el registro de la venta y las actividades de preparación y entrega del pedido. |
| CP-03 | Cliente no realiza una compra | El proceso permite entregar la información solicitada y finalizar correctamente. |
| CP-04 | Ejecución de una tarea humana | Flowable permite identificar y completar la User Task correspondiente. |
| CP-05 | Ejecución de una tarea automática | El Service Task o External Worker ejecuta la actividad y permite continuar el proceso. |
| CP-06 | Comunicación con servicios REST | Los servicios reciben las solicitudes y entregan respuestas correctas. |
| CP-07 | Almacenamiento de información | Los datos enviados por los servicios se guardan y pueden consultarse posteriormente. |
| CP-08 | Finalización del proceso | La instancia del proceso finaliza y su estado puede verificarse en Flowable. |

### 8.2 Criterios de aceptación

Para considerar que la solución cumple con los objetivos de la tercera entrega, se deberán verificar los siguientes criterios:

1. El proceso BPMN se encuentra desplegado y puede ejecutarse en Flowable Open Source.
2. Las tareas humanas y automáticas funcionan según el flujo establecido.
3. Los servicios REST se comunican correctamente con los componentes correspondientes.
4. La interfaz web permite ingresar y consultar información relevante.
5. Los datos del proceso se almacenan correctamente.
6. Se demuestra la ejecución del proceso en diferentes escenarios.
7. Se dispone de evidencias que permitan comprobar los resultados obtenidos.

### 8.3 Registro de resultados

Los escenarios definidos deberán ser ejecutados y documentados por el equipo, registrando el resultado obtenido, las evidencias correspondientes y las posibles incidencias detectadas.

Los resultados deberán clasificarse como:

- **Aprobado:** el escenario cumple con el resultado esperado.
- **Rechazado:** el escenario presenta errores o no cumple con el resultado esperado.
- **Pendiente:** el escenario todavía no ha sido ejecutado o validado.

Los escenarios descritos corresponden a pruebas propuestas y no deben considerarse aprobados hasta contar con evidencia de su ejecución.

### 9. Conclusiones y recomendaciones

#### 9.1 Conclusiones

El análisis realizado para la tercera entrega de Peluditos Pet Chile permite establecer los requisitos necesarios para avanzar hacia una solución tecnológica integrada que apoye la gestión de consultas y ventas de alimentos para mascotas.

Durante las entregas anteriores se desarrollaron los modelos BPMN AS-IS y TO-BE, además de realizarse pruebas de ejecución de procesos mediante Flowable Open Source.

Para esta tercera entrega, el principal objetivo es consolidar la comunicación entre el motor de procesos, los servicios REST, los External Workers, la base de datos y la interfaz web.

La correcta integración de estos componentes permitirá mejorar la organización de las actividades comerciales, reducir tareas manuales y facilitar el seguimiento de las operaciones del emprendimiento.

Sin embargo, será necesario comprobar el funcionamiento de la solución mediante pruebas de extremo a extremo y evidencias que demuestren la ejecución de los distintos escenarios.

#### 9.2 Recomendaciones del Analista

Se recomienda al equipo de desarrollo considerar las siguientes acciones:

1. **Priorizar la integración:** verificar que los componentes existentes funcionen correctamente antes de incorporar nuevas funcionalidades.

2. **Validar el proceso BPMN:** comprobar que las actividades, decisiones y finalización de las instancias se ejecuten correctamente en Flowable Open Source.

3. **Comprobar la automatización:** verificar el funcionamiento de las Service Tasks y los External Workers implementados.

4. **Fortalecer las pruebas:** ejecutar los escenarios definidos y registrar los resultados obtenidos.

5. **Mejorar la interfaz web:** asegurar que permita interactuar con el proceso e incorpore la identidad visual de Peluditos Pet Chile.

6. **Mantener la documentación:** actualizar los archivos del repositorio GitHub y registrar los cambios realizados por cada integrante.

7. **Promover el trabajo colaborativo:** mantener reuniones, actas y seguimiento de compromisos para asegurar el cumplimiento de los objetivos.

#### 9.3 Conclusión final

Desde el rol de Analista de Procesos, se considera fundamental garantizar que la solución desarrollada responda a las necesidades reales de Peluditos Pet Chile.

Por ello, se recomienda concentrar los esfuerzos de la tercera entrega en validar la integración de los componentes existentes, corregir posibles problemas y demostrar mediante evidencias el funcionamiento del proceso comercial.

De esta manera, el equipo podrá avanzar hacia una solución más confiable, mantenible y preparada para futuras mejoras.

