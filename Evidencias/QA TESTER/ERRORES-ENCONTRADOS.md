# Registro de incidencias QA - Avance de Entrega 4

**Fecha:** 25 de noviembre de 2026  
**Responsable:** Felipe Quicel  
**Versión probada:** `cdad1af71e0e0382661e1e8ab9478492c9e0b830`.

## ERR-01. Registro detenido mientras el worker esperaba a Flowable

**Prueba:** QA-01.  
**Tipo:** incidencia del entorno de consola.  
**Estado:** resuelta en la ejecución local.  
**Impacto:** impidió temporalmente completar el registro del cliente.

Después de enviar el formulario, la interfaz confirmó la recepción, pero el listado seguía vacío. Flowable mantenía la instancia activa. El diagnóstico mostró al worker esperando una respuesta HTTP y a Flowable bloqueado al escribir en su consola.

Se indicó pulsar **Esc** para salir del modo de selección en las consolas. Después de hacerlo, el Worker 1 procesó el mismo trabajo, recibió HTTP 201 y completó la tarea. La consulta del listado recuperó al cliente ID 1 con su mascota.

La secuencia observada respalda una pausa asociada a la consola. No se modificó el código, no se reiniciaron los componentes para esta corrección y no se repitió el envío.

## Evidencias

- [Worker sin completar el registro](Capturas/QA-01-03-worker-sin-procesamiento.png).
- [Trabajo procesado con HTTP 201](Capturas/QA-01-04-worker1-HTTP201-completado.png).
- [Cliente recuperado en el listado](Capturas/QA-02-01-cliente-y-mascota-en-listado.png).

## Consideración para próximas pruebas

Al seleccionar texto en una consola de Windows PowerShell, salir de ese modo con Esc para permitir que continúe normalmente. Si vuelve a ocurrir una espera, revisar los componentes antes de repetir una solicitud, para evitar duplicados.

Este registro contiene la incidencia observada en los escenarios ejecutados. No supone que se hayan descartado todos los errores posibles de la aplicación.
