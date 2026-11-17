
# Documentación — Peluditos Pet Chile

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Etapa:** Entrega 3

## Objetivo

Organizar la documentación del proyecto Peluditos Pet Chile y registrar los avances y correcciones necesarios para la Entrega 3.

## Organización del trabajo

Los roles acordados para esta entrega son:

- Elvis Gutiérrez: Analista.
- Felipe Quicel: Modelador BPMN.
- Geraldine González: Implementadora y QA/Tester.
- Lautaro Salinas: Integrador.

Geraldine también se encargará de preparar las actas de las reuniones.

## Actividades pendientes

- Revisar las observaciones del profesor sobre la Entrega 2.
- Identificar la versión vigente de cada componente.
- Revisar la organización del repositorio y los archivos duplicados.
- Comprobar la ejecución del proceso en Flowable OSS.
- Revisar los servicios web Java y su integración.
- Verificar la persistencia de datos en SQLite.
- Documentar las pruebas y sus resultados.
- Actualizar el diagrama de arquitectura.
- Preparar las instrucciones de ejecución.
- Revisar los enlaces y permisos de los videos.

## Estado actual

La Entrega 2 fue enviada el 9 de noviembre de 2026.

Para la Entrega 3 se está revisando la organización del repositorio y preparando las correcciones solicitadas por el profesor.

Los cambios se preparan inicialmente en la rama `organizacion-entrega3`, sin modificar directamente la rama principal `main`.

## Próximas entregas

- Entrega 3: 23 de noviembre de 2026.
- Entrega final: 30 de noviembre de 2026.


## Propuesta de organización del monorepo

Para la Entrega 3 se propone ordenar el repositorio por componentes del proyecto, evitando mantener varias copias del mismo código en diferentes carpetas de entrega.

### Estructura propuesta

- `Actas/`: reuniones, acuerdos y compromisos del equipo.
- `BPMN/`: modelos AS-IS y TO-BE, incluyendo sus imágenes.
- `Arquitectura/`: diagramas actualizados y archivos editables.
- `Documentacion/`: análisis, guías e instrucciones del proyecto.
- `bpmn-oss/`: proceso BPMN ejecutable y formularios de Flowable OSS.
- `servicio-peluditos/`: servicios web Java y conexión con SQLite.
- `external-worker-peluditos/`: integración de Flowable con los servicios.
- `interfaz-peluditos/`: interfaz web HTML, CSS y JavaScript.
- `QA-Tester/`: pruebas, resultados y evidencias.
- `Videos/`: enlaces a los videos organizados por entrega.
- `README.md`: presentación general e instrucciones de ejecución.

### Consideraciones

Esta estructura es una propuesta y todavía no se encuentra implementada completamente.

Antes de reorganizar los archivos se deberá:

1. Identificar la versión vigente de cada componente.
2. Revisar las rutas y configuraciones utilizadas por los programas.
3. Conservar el historial de las entregas anteriores mediante Git.
4. Coordinar los cambios con los integrantes responsables.
5. Comprobar que los componentes sigan funcionando después de reorganizarlos.

Por ahora no se eliminarán ni moverán archivos de las entregas anteriores.
 

  
