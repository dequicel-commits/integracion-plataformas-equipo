# Informe de pruebas QA - Avance de Entrega 4

**Proyecto:** Peluditos Pet Chile  
**Responsable:** Felipe Quicel  
**Fecha:** 25 de noviembre de 2026  
**Estado:** 8 pruebas aprobadas y 1 pendiente.

## Versión y entorno

Probé la copia de `main` correspondiente al commit `cdad1af71e0e0382661e1e8ab9478492c9e0b830`, obtenido del ZIP descargado. Su documentación todavía corresponde a la Entrega 3. Estas pruebas sirven como avance para el examen; si cambia el código, se deben repetir los casos afectados.

| Componente | Entorno utilizado |
| --- | --- |
| Sistema y navegador | Windows de 64 bits, PowerShell y Brave |
| Java | Microsoft OpenJDK 21.0.12; los proyectos compilan con release 17 |
| Maven | Apache Maven 3.10.0 |
| Motor | Flowable OSS 7.2.0, puerto 8080 |
| Servicio | Jakarta REST con Jersey/Grizzly, puerto 7072 |
| Base del motor | H2 persistente, archivo `Datos/flowable-qa.mv.db` |
| Base de negocio | SQLite, archivo `Datos/peluditos-qa.db` |
| Interfaz | `/interfaz/index.html`, `/interfaz/clientes.html` y `/interfaz/seguimiento.html` |

Utilicé la guía `Documentacion/GUIA-EJECUCION.md` de la versión probada. Los dos componentes Java terminaron con `BUILD SUCCESS` al ejecutar `mvn -B -DskipTests package`. Esa comprobación fue de compilación y empaquetado; las pruebas automatizadas no se ejecutaron. Después realicé los recorridos manuales descritos abajo.

Registros de compilación: [servicio Java](Registros/compilacion-servicio-peluditos-con-dependencias.log) y [workers](Registros/compilacion-external-worker-peluditos-con-dependencias.log).

## Resumen

| Código | Prueba | Resultado |
| --- | --- | --- |
| QA-01 | Registrar un cliente e iniciar su proceso | Aprobada, después de resolver ERR-01 |
| QA-02 | Consultar el cliente y su mascota | Aprobada |
| QA-03 | Completar el seguimiento con compra | Aprobada |
| QA-04 | Completar el seguimiento sin compra | Aprobada |
| QA-05 | Intentar avanzar con el formulario vacío | Aprobada |
| QA-06 | Ingresar un correo sin arroba | Aprobada |
| QA-07 | Conservar los datos después de reiniciar | Aprobada |
| QA-08 | Completar un nuevo recorrido después del reinicio | Aprobada |
| QA-09 | Instalar y comprobar la solución en otro equipo o entorno | Pendiente |

## Pruebas realizadas

### QA-01. Registro del cliente

Ingresé a **Cliente QA Registro**, con su mascota **Luna QA**, y envié el formulario desde la interfaz. La aplicación confirmó la recepción. Después de resolver la incidencia de consola ERR-01, el Worker 1 recibió el trabajo, obtuvo HTTP 201 al guardar los datos y completó la tarea en Flowable. Se generaron `clienteId=1` y `mascotaId=1`.

**Resultado:** aprobado. Caso Flowable: `561b01f2-e8f8-11f1-bce2-0a002700000b`.

Evidencias: [confirmación del envío](Capturas/QA-01-02-confirmacion-envio-consulta.png), [Worker 1](Capturas/QA-01-04-worker1-HTTP201-completado.png).

### QA-02. Consulta del cliente

Abrí el listado y comprobé que aparecieran el cliente ID 1, su teléfono, canal WhatsApp, mascota Luna QA y estado **En seguimiento**. Los datos visibles coincidieron con los ingresados. El correo y el cumpleaños no se muestran en ese listado y no se validaron con esa captura.

**Resultado:** aprobado para los datos consultados.

Evidencia: [cliente y mascota en el listado](Capturas/QA-02-01-cliente-y-mascota-en-listado.png).

### QA-03. Seguimiento con compra

Seleccioné al cliente ID 1 y la opción **Sí - Compra realizada**. El Worker 2 envió la actualización con `compraRealizada=true`, recibió HTTP 200 y completó su trabajo. El listado mostró **Compra realizada** y la observación ingresada. Una consulta al historial confirmó el final **Venta concretada**, con fecha de término y sin motivo de eliminación.

**Resultado:** aprobado. No alcancé a capturar el mensaje de confirmación de la interfaz; se conservaron las evidencias del worker, el listado y el historial.

Evidencias: [Worker 2](Capturas/QA-03-02-worker2-compra-HTTP200-completado.png), [estado final](Capturas/QA-03-03-listado-compra-realizada.png), [historial del proceso](Registros/QA-03-historial-proceso.json).

### QA-04. Seguimiento sin compra

Registré a **Cliente QA Sin Compra**, con su mascota **Alan QA**. El Worker 1 guardó el registro con HTTP 201 y generó el cliente ID 2. Después seleccioné **No - Sin compra** en el seguimiento. El Worker 2 recibió `compraRealizada=false`, actualizó el registro con HTTP 200 y completó el trabajo. El listado mostró **Sin compra**. El historial confirmó el final **Venta no concretada de potencial cliente**.

**Resultado:** aprobado. Caso Flowable: `d9094f71-e8fa-11f1-bce2-0a002700000b`.

Evidencias: [confirmación del seguimiento](Capturas/QA-04-03-confirmacion-seguimiento.png), [workers](Capturas/QA-04-04-worker2-sin-compra-HTTP200.png), [listado con ambas rutas](Capturas/QA-04-05-listado-ambas-rutas.png), [verificación](Registros/QA-04-verificacion-seguimiento.json).

### QA-05. Formulario vacío

Pulsé **Limpiar** e intenté avanzar con los campos vacíos. El navegador dejó el foco en **Nombre completo** y mostró el aviso de campo obligatorio. No permitió pasar a la revisión. La consulta posterior confirmó que seguían existiendo los dos clientes anteriores y que no había procesos activos nuevos.

**Resultado:** aprobado. Esta prueba comprueba el intento con el formulario vacío y el primer campo obligatorio, no cada campo por separado ni las validaciones del servidor.

Evidencias: [aviso de campo obligatorio](Capturas/QA-05-01-campo-obligatorio-vacio.png), [comprobación sin nuevos registros](Registros/QA-05-verificacion-sin-nuevos-registros.json).

### QA-06. Correo incorrecto

Completé los campos obligatorios e ingresé `correo-invalido` en el correo electrónico. Al intentar revisar los datos, el navegador indicó que faltaba `@` e impidió avanzar. La consulta posterior mantuvo los mismos dos clientes y cero procesos activos.

**Resultado:** aprobado para el formato del correo en el navegador. No se probó el rechazo directo de ese dato por el servicio REST.

Evidencias: [aviso del correo](Capturas/QA-06-01-correo-sin-arroba-rechazado.png), [comprobación sin nuevos registros](Registros/QA-06-verificacion-sin-nuevos-registros.json).

### QA-07. Persistencia después del reinicio

Registré a **Cliente QA Reinicio**, con su mascota **Blanca QA**, y lo dejé en **En seguimiento** como cliente ID 3. Antes de detener los componentes había tres clientes, un proceso pendiente y dos finalizados.

Detuve primero los workers, después Jakarta REST y finalmente Flowable. Se comprobó que los puertos 7072 y 8080 ya no aceptaban conexiones y que los archivos de las bases seguían presentes. Después inicié los componentes con las mismas rutas de SQLite y H2.

El listado recuperó los tres clientes con los mismos estados. La comparación de la API confirmó los mismos campos consultados de SQLite, la misma instancia y tarea pendientes de Flowable y los dos historiales finalizados. Los workers se volvieron a iniciar; su procesamiento posterior se comprobó en QA-08.

**Resultado:** aprobado para el reinicio de componentes en este equipo. No corresponde a una instalación en otro entorno ni a una prueba de recuperación de respaldos.

Evidencias: [antes del reinicio](Capturas/QA-07-02-tres-clientes-antes-reinicio.png), [apagado de Flowable](Capturas/QA-07-06-Flowable-apagado-completo.png), [después del reinicio](Capturas/QA-07-10-tres-clientes-despues-reinicio.png), [workers reiniciados](Capturas/QA-07-11-workers-reiniciados.png).

Comparaciones: [estado anterior](Registros/QA-07-estado-antes-reinicio.json), [componentes detenidos](Registros/QA-07-componentes-detenidos.json), [Flowable después del reinicio](Registros/QA-07-Flowable-despues-reinicio.json), [SQLite después del reinicio](Registros/QA-07-SQLite-despues-reinicio.json).

### QA-08. Recorrido posterior al reinicio

Registré a **Cliente QA Post Reinicio**, con su mascota **Copito QA**, después de volver a iniciar los componentes. El Worker 1 creó el cliente ID 4 con HTTP 201. Luego completé el seguimiento con compra: el Worker 2 obtuvo HTTP 200 y dejó el estado **Compra realizada**. La interfaz confirmó el seguimiento y el listado mostró los cuatro clientes. El historial confirmó el final **Venta concretada**.

**Resultado:** aprobado. Caso Flowable: `7885ae14-e8fe-11f1-b442-0a002700000b`.

Evidencias: [confirmación](Capturas/QA-08-02-confirmacion-seguimiento.png), [procesamiento de los dos workers](Capturas/QA-08-03-workers-HTTP201-HTTP200.png), [cuatro clientes](Capturas/QA-08-04-listado-cuatro-clientes.png), [verificación del recorrido](Registros/QA-08-verificacion-recorrido.json).

### QA-09. Instalación en otro entorno

**Pendiente de ejecutar.** Falta coordinar el equipo o entorno de destino, seguir la guía de instalación y registrar la configuración, el inicio de los componentes y una prueba integrada. La revisión local y el reinicio de QA-07 no se presentan como sustitutos de esta prueba.

## Incidencia encontrada

Durante QA-01, el worker quedó esperando la respuesta de Flowable mientras la consola del motor estaba bloqueada al escribir. Después de salir del modo de selección con **Esc**, el mismo envío fue procesado y el cliente quedó guardado. No fue necesario modificar código ni repetir el registro.

La incidencia quedó resuelta en esta ejecución. El detalle está en [ERRORES-ENCONTRADOS.md](ERRORES-ENCONTRADOS.md).

## Estado de los datos al terminar estas pruebas

| ID | Cliente | Mascota | Estado |
| --- | --- | --- | --- |
| 1 | Cliente QA Registro | Luna QA | Compra realizada |
| 2 | Cliente QA Sin Compra | Alan QA | Sin compra |
| 3 | Cliente QA Reinicio | Blanca QA | En seguimiento |
| 4 | Cliente QA Post Reinicio | Copito QA | Compra realizada |

El cliente 3 se dejó pendiente para comprobar que Flowable conservara una instancia y su tarea durante el reinicio.

## Pendientes y próxima actualización

- Ejecutar QA-09 y agregar sus evidencias.
- Revisar y repetir las pruebas afectadas si se incorporan cambios para el examen.
- Mantener el commit probado y la fecha de cada nueva ejecución en el informe.
- Las validaciones probadas fueron las del navegador. No se evaluaron todos los errores de servicios ni funcionalidades fuera de estos recorridos.

Las ocho pruebas realizadas funcionaron dentro del alcance descrito. El plan sigue abierto hasta completar la prueba de instalación y validar la versión que el equipo entregará como final.
