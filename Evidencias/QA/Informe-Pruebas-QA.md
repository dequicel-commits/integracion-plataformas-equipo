# INFORME DE PRUEBAS QA / TESTER

## Proyecto: Peluditos Pet Chile — Entrega 2

**Rol:** QA / Tester  
**Fecha de pruebas:** 08 de noviembre de 2026  
**Plataforma:** Flowable OSS 7.2 y Java REST

---

## 1. Objetivo

Verificar el funcionamiento del proceso BPMN TO-BE de Peluditos Pet Chile, comprobando su integración con el External Worker desarrollado en Java y el servicio REST de seguimiento comercial.

## 2. Herramientas utilizadas

- Flowable OSS 7.2
- Apache Tomcat 10.1
- Java JDK 21
- Apache Maven 3.9.16
- Visual Studio Code
- PowerShell
- API REST de Flowable

## 3. Pruebas realizadas

| Código | Descripción | Resultado |
|---|---|---|
| QA-01 | Verificación de Java y Maven | Aprobada |
| QA-02 | Compilación y pruebas automáticas del servicio REST | Aprobada |
| QA-03 | Inicio del servicio REST en el puerto 7070 | Aprobada |
| QA-04 | Consulta HTTP GET al servicio REST | Aprobada |
| QA-05 | Verificación de Flowable OSS 7.2 | Aprobada |
| QA-06 | Despliegue del proceso BPMN | Aprobada |
| QA-07 | Compilación y pruebas del External Worker | Aprobada |
| QA-08 a QA-15 | Ejecución de actividades y verificación de variables BPMN | Aprobadas |
| QA-16 | Integración Flowable, External Worker y servicio REST Java | Aprobada tras correcciones |
| QA-17 | Verificación de la finalización del proceso BPMN | Aprobada |

## 4. Incidencias detectadas

### Incidencia 1: Problemas de conexión del External Worker

Se detectaron errores de conexión durante las primeras ejecuciones del External Worker. Se revisó y corrigió la dirección de la API utilizada para adquirir trabajos externos desde Flowable.

### Incidencia 2: Servicio REST Java no disponible

Durante las pruebas se detectó que el servicio REST del puerto 7070 no estaba respondiendo.

**Solución:** Se inició nuevamente el servicio Java y se verificó su funcionamiento mediante una petición HTTP GET.

### Incidencia 3: Variable compraRealizada inexistente

Flowable generó un error al evaluar la condición `${compraRealizada == false}` en una compuerta exclusiva.

**Solución:** Se registró la variable booleana `compraRealizada = true` en la instancia de prueba y se recuperó el trabajo fallido.

## 5. Resultados de integración

El External Worker adquirió el trabajo correspondiente al tema `seguimiento-peluditos`, consultó el servicio REST Java y notificó su finalización a Flowable.

Posteriormente, el proceso avanzó a la actividad **Registro cliente y compra realizada**, la cual fue completada mediante la API REST.

## 6. Verificación de finalización

**ID de instancia:** `a20080ad-dba3-11f1-a042-0a0027000011`

**Inicio:** 08/11/2026, 15:38:46 (hora de Chile)

**Finalización:** 08/11/2026, 16:55:38 (hora de Chile)

**Duración total:** 4.611.970 milisegundos.

**Resultado:** Proceso BPMN finalizado correctamente.

Se comprobó la finalización mediante el historial de Flowable, verificando la existencia de una fecha válida en el campo `endTime`.

## 7. Evidencias

Las capturas de pantalla de las pruebas se encuentran almacenadas en la carpeta:

`Entrega-2/QA-Tester/Evidencias`

Entre ellas se incluyen las comprobaciones de Java, Maven, servicio REST, Flowable, External Worker y finalización del proceso BPMN.

## 8. Conclusión

Las pruebas permitieron comprobar el funcionamiento del proceso BPMN TO-BE de Peluditos Pet Chile y su integración con el servicio REST Java.

Durante la ejecución se identificaron incidencias de conexión y configuración de variables que fueron corregidas.

Finalmente, Flowable registró la finalización del proceso BPMN, por lo que la prueba de integración realizada se considera aprobada.

**Estado final: APROBADO.**