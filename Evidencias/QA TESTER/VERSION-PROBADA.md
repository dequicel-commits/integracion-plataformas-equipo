# Versión y entorno probados

- Fecha de las pruebas: 25 de noviembre de 2026.
- Responsable: Felipe Quicel.
- Rama de la copia descargada: main.
- Commit indicado en el comentario del ZIP: `cdad1af71e0e0382661e1e8ab9478492c9e0b830`.
- SHA-256 del ZIP: `c567f5695e5ea960582940b015023c6e14197e0ea9e1971ee356c1c8e7dbffba`.
- Referencia de la documentación incluida: Entrega 3.
- Sistema: Windows de 64 bits, PowerShell y navegador Brave.
- JDK utilizado: Microsoft OpenJDK 21.0.12.
- Compilación de los proyectos: release 17.
- Maven: Apache Maven 3.10.0.
- Flowable OSS: 7.2.0, puerto 8080.
- Jakarta REST: Jersey/Grizzly, puerto 7072.
- Base del motor: H2 en archivo persistente `Datos/flowable-qa.mv.db`.
- Base de negocio: SQLite en `Datos/peluditos-qa.db`.
- Guía consultada: `Documentacion/GUIA-EJECUCION.md` de la copia descargada.

## Alcance de este avance

QA-01 a QA-08 aprobadas según el informe. QA-09 no ejecutada. La compilación se realizó con `-DskipTests`; no se presentan esos comandos como ejecución de pruebas automatizadas.

Las verificaciones de API se guardaron en JSON dentro de Registros. Las capturas corresponden a las ejecuciones compartidas por el responsable de las pruebas y se conservan sin editar.

El identificador de commit se obtuvo de los metadatos del ZIP. Una nueva ejecución sobre otra versión debe registrar su propio commit y sus resultados.
