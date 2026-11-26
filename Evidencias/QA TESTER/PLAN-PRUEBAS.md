# Plan de pruebas — Entrega 4

Proyecto: Peluditos Pet Chile  
Responsable de las pruebas: Felipe Quicel  
Estado: Avance del 25 de noviembre de 2026. QA-01 a QA-08 aprobadas; QA-09 pendiente.

## Objetivo

Comprobar que la aplicación permita registrar clientes, completar el seguimiento comercial y conservar los datos. También revisar que las instrucciones permitan instalar e iniciar la solución.

## Versión y entorno

Entorno utilizado en esta ejecución:

- Fecha de ejecución: 25 de noviembre de 2026.
- Rama y commit: main según el ZIP; cdad1af71e0e0382661e1e8ab9478492c9e0b830 según su comentario de archivo.
- Carpeta del proyecto: copia extraída del ZIP de main, identificada por el commit anterior.
- Sistema operativo: Windows amd64.
- Versión de Java y Maven: Microsoft OpenJDK 21.0.12 y Apache Maven 3.10.0.
- Versión de Flowable OSS: 7.2.0.
- Base de datos de Flowable: H2 persistente, Datos/flowable-qa.mv.db.
- Base de datos de negocio: SQLite, Datos/peluditos-qa.db.
- Direcciones y puertos: Flowable en localhost:8080 y Jakarta REST en localhost:7072.
- Guía de instalación utilizada: Documentacion/GUIA-EJECUCION.md de esta copia (Entrega 3).

## Preparación

1. Obtener la versión integrada que acuerde el equipo y anotar su commit.
2. Leer el README y la guía de ejecución.
3. Preparar la configuración de prueba sin publicar credenciales reales.
4. Iniciar los componentes en el orden indicado por la guía.
5. Usar datos ficticios que permitan reconocer cada caso.
6. Guardar las capturas y completar los resultados mientras se realizan las pruebas.

## Casos de prueba

| Código | Prueba y pasos principales | Resultado esperado | Estado |
| --- | --- | --- | --- |
| QA-01 | Registrar desde la interfaz un cliente ficticio con todos los datos obligatorios. | El cliente se guarda y se inicia el proceso correspondiente en Flowable. | Aprobada |
| QA-02 | Consultar el cliente creado en QA-01. | Los datos recuperados coinciden con los ingresados. | Aprobada |
| QA-03 | Utilizar el caso pendiente registrado en QA-01 y completar el seguimiento indicando que hubo compra. | El estado queda en Compra realizada, se guarda en la base de negocio y el proceso llega a su final correspondiente. | Aprobada |
| QA-04 | Registrar otro caso y completar el seguimiento indicando que no hubo compra. | El estado queda en Sin compra, se guarda en la base de negocio y el proceso llega a su final correspondiente. | Aprobada |
| QA-05 | Intentar enviar un formulario dejando vacíos campos definidos como obligatorios. | El sistema informa el problema y evita guardar un registro inválido. | Aprobada |
| QA-06 | Ingresar un dato con formato incorrecto en un campo que tenga una validación definida. Anotar el campo y el valor probado. | Se aplica la validación acordada y aparece un mensaje comprensible. Si no hay una regla definida, confirmar el alcance con el equipo. | Aprobada |
| QA-07 | Guardar los identificadores y estados de los clientes de prueba, detener los componentes y volver a iniciarlos usando las mismas bases de datos. Consultar nuevamente los registros. | Los clientes y sus estados permanecen disponibles después del reinicio. Registrar también la persistencia del motor según el escenario definido. | Aprobada |
| QA-08 | Registrar un nuevo cliente y completar su seguimiento después del reinicio. | La integración sigue funcionando y el resultado queda guardado. | Aprobada |
| QA-09 | Seguir la guía de instalación en otro equipo o entorno de destino, preparar la configuración y las bases de datos, iniciar los componentes y ejecutar un recorrido completo. | La instalación se puede repetir siguiendo la documentación y el recorrido funciona. | Pendiente |

## Datos ficticios sugeridos

- Cliente QA Registro.
- Cliente QA Compra.
- Cliente QA Sin Compra.
- Cliente QA Reinicio.

Usar datos de contacto ficticios apropiados para las validaciones del sistema. Anotar los identificadores generados para poder comprobar los mismos registros después.

## Registro de cada ejecución

Copiar este bloque en el informe por cada prueba realizada:

- Código de prueba:
- Fecha y versión probada:
- Datos e identificadores utilizados:
- Pasos realizados:
- Resultado esperado:
- Resultado obtenido:
- Estado: Aprobada / Fallida / Bloqueada / Pendiente.
- Evidencia:
- Error relacionado, si corresponde:
- Observaciones:

Una prueba está aprobada cuando se comprobó su resultado esperado. Si no pudo ejecutarse por una dependencia o un problema del entorno, registrar Bloqueada y explicar el motivo.

## Capturas

Guardar los archivos dentro de Capturas, por ejemplo:

- QA-01-registro-cliente.png
- QA-03-compra-realizada.png
- QA-04-sin-compra.png
- QA-07-datos-despues-reinicio.png
- QA-09-instalacion-verificacion.png

Tomar las evidencias de la ejecución actual. Para resultados que no se vean en la interfaz, complementar con el estado del proceso o una consulta a la base de datos.

