# Entrega 1 - Peluditos Pet Chile

## Integración de Plataformas - IPLACEX
**Grupo 3**

Esta carpeta corresponde a la primera evaluación del proyecto Peluditos Pet Chile, desarrollado para la asignatura Integración de Plataformas.

Su objetivo es presentar el diagnóstico del proceso actual, una propuesta de mejora y un prototipo inicial utilizando Flowable y servicios web Java.

## Contenido de la primera entrega

- **BPMN AS-IS:** modelo del proceso actual.
- **BPMN TO-BE:** propuesta de mejora del proceso.
- **Flowable Enterprise:** prototipo con formulario de seguimiento comercial.
- **Servicio REST Java:** servicio de prueba para el seguimiento comercial.
- **External Worker:** integración entre Flowable y el servicio REST Java.
- **Arquitectura:** diagrama de los componentes de la solución.
- **Evidencias:** capturas de las pruebas realizadas.
- **Videos:** presentación para la emprendedora y presentación técnica para el docente.

## Pruebas realizadas

Se desarrolló y ejecutó un servicio REST Java que responde correctamente a una solicitud.

También se probó la integración mediante un External Worker utilizando Flowable OSS local. El programa recibió el trabajo, consultó el servicio REST y permitió que el proceso BPMN continuara correctamente.

Además, se probó el formulario de seguimiento comercial en Flowable Enterprise, utilizando datos ficticios.

## Observación sobre Flowable Cloud Sandbox

Durante las pruebas se detectó que Flowable Enterprise Cloud Sandbox no generaba los trabajos del External Worker.

Esta situación fue consultada al profesor, quien confirmó que se trataba de un problema conocido de la plataforma.

Por este motivo, la integración técnica se probó utilizando Flowable OSS local, mientras que el prototipo con formularios se mantuvo en Flowable Enterprise.

## Alcance de la entrega

Esta primera evaluación corresponde a un prototipo inicial. El proyecto continuará desarrollándose y mejorándose durante las siguientes entregas.
