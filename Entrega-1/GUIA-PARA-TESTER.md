# Guía para el Tester — Peluditos Pet Chile

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Entrega:** Primera evaluación

## 1. Objetivo

Este documento tiene como finalidad compartir la información de la implementación realizada con Java y Flowable, para facilitar la revisión y las pruebas de integración.

Se desarrolló un servicio REST en Java y un External Worker para conectar una actividad del proceso BPMN con el servicio.

## 2. Programas utilizados

Para realizar la implementación y las pruebas se utilizaron los siguientes programas:

| Programa | Versión utilizada | Uso |
|---|---|---|
| Visual Studio Code | No especificada | Desarrollo de los proyectos Java |
| Java JDK | 21 | Ejecución de los programas |
| Apache Maven | 3.10.0 | Compilación y ejecución de los proyectos |
| Apache Tomcat | 10.1.60 | Ejecución de Flowable REST local |
| Flowable OSS | 7.2.0 | Pruebas del BPMN y External Worker |
| Flowable Enterprise Cloud | Plataforma IPLACEX | Modelos BPMN y formularios |
| GitHub | Plataforma web | Código, documentación y evidencias |

## 3. Archivos de la implementación

Los archivos están disponibles en el repositorio del equipo:

https://github.com/dequicel-commits/integracion-plataformas-equipo

Dentro de la carpeta `Entrega-1` se encuentran:

- `servicio-peluditos`: proyecto Java del servicio REST.
- `external-worker-peluditos`: proyecto Java del External Worker.
- `bpmn-oss`: diagrama BPMN adaptado para Flowable OSS.
- `DOCUMENTACION-TECNICA.md`: información técnica y comandos utilizados.
- `Evidencias/Implementacion`: capturas de las pruebas realizadas.

Los proyectos Java incluyen sus archivos `pom.xml` para trabajar con Maven.

## 4. Servicio REST desarrollado

Se implementó un servicio REST de prueba para el seguimiento comercial de Peluditos Pet Chile.

**Método:** GET  
**Dirección local:** `http://localhost:7070/api/seguimiento`

**Respuesta del servicio:**

```json
{
  "estado": "OK",
  "mensaje": "Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"
}
```

El servicio corresponde al prototipo inicial y utiliza datos de prueba.

## 5. Organización de los diagramas en Flowable

Para organizar los modelos utilizados durante la implementación, creé un proyecto en Flowable Design:

**My workspace → Peluditos Pet Chile - Grupo 3**

En ese espacio se encuentran los siguientes diagramas:

- Peluditos Pet Chile - TO-BE Integracion Java.
- Peluditos Pet Chile - TO-BE.
- Peluditos_Pet_Chile - AS-IS.

También dejé en GitHub la versión del BPMN adaptada para las pruebas con Flowable OSS:

`Entrega-1/bpmn-oss/peluditos-integracion-oss.bpmn20.xml`

Esta versión se utilizó para realizar las pruebas de integración sin modificar directamente el TO-BE original del compañero.

## 6. Observación sobre Flowable Cloud

Durante las primeras pruebas encontramos que Flowable Cloud Sandbox no generaba los trabajos del External Worker.

Consultamos esta situación al profesor, quien confirmó el problema de la plataforma.

Por ese motivo se utilizó Flowable OSS local para comprobar la integración entre el BPMN, el External Worker y el servicio REST Java.

Por otra parte, se utilizó Flowable Enterprise para probar el proceso TO-BE y sus formularios.

## 7. Información para las pruebas de integración

La implementación se encuentra disponible en GitHub para realizar las pruebas correspondientes.

Durante el desarrollo se comprobó el funcionamiento del servicio REST, la comunicación con el External Worker y la continuación del proceso BPMN mediante Flowable OSS.

El resultado obtenido fue:

```text
Trabajos recibidos: 1
Respuesta del servicio: {"estado":"OK","mensaje":"Servicio de seguimiento comercial de Peluditos Pet Chile funcionando"}
Trabajo completado en Flowable.
BUILD SUCCESS
```

También se comprobó la finalización del proceso utilizando la ruta de compra realizada.

Las capturas de estas pruebas se encuentran en la carpeta `Evidencias/Implementacion`.

**Observación:** Si durante las pruebas aparece algún problema relacionado con el servicio REST, el External Worker o la integración con Flowable, me pueden avisar para revisarlo y realizar los ajustes necesarios.
