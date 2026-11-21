# Modelos BPMN — Peluditos Pet Chile

Directorio unificado con los modelos BPMN del proceso actual (AS-IS) y del proceso propuesto e integrado con Flowable OSS (TO-BE), junto con su representación visual y formularios.

## Archivos incluidos

- **`AS-IS.bpmn`**: modelo del proceso actual de atención de consultas.
- **`TO-BE.bpmn`**: modelo del proceso propuesto.
- **`peluditos-integracion-oss-entrega2.bpmn20.xml`**: definición BPMN 2.0 ejecutable en Flowable Engine OSS (`key: peluditosPetChileTOBE12`), integrada con los 2 External Workers (`guardar-cliente-peluditos` y `actualizar-seguimiento-peluditos`).
- **`TO-BE-Entrega3.png`**: imagen visual actualizada del modelo TO-BE integrado.
- **`form_registro_cliente.form`** y **`form_seguimiento_comercial.form`**: definiciones de formularios de respaldo para Flowable.

## Despliegue en Flowable Engine OSS (`:8080`)

Desde la raíz del repositorio:

```powershell
curl.exe -i -u rest-admin:test -F "file=@BPMN/peluditos-integracion-oss-entrega2.bpmn20.xml" "http://127.0.0.1:8080/flowable-rest/service/repository/deployments"
```
