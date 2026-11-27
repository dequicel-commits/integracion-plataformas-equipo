# Modelos BPMN — Peluditos Pet Chile (Entrega 4)

Directorio unificado con los modelos BPMN del proceso actual (AS-IS) y del proceso propuesto e integrado con Flowable Engine OSS (TO-BE), incluyendo los dos recorridos operativos:
1. **Recorrido 1 — Registro de consultas y seguimiento comercial** (con los External Workers 1 y 2).
2. **Recorrido 2 — Campañas, cumpleaños de mascotas y fechas especiales** (con los External Workers 3 y 4).

## Archivos incluidos

- **`AS-IS.bpmn`**: modelo del proceso actual de atención de consultas.
- **`TO-BE.bpmn`** y **`peluditos-integracion-oss-entrega2.bpmn20.xml`**: definición BPMN 2.0 ejecutable en Flowable Engine OSS (`key: peluditosPetChileTOBE12`), integrada con los **4 External Workers**:
  - **Worker 1 (`Activity_10hj4gn`):** *Guardar datos del potencial cliente* (`topic: guardar-cliente-peluditos`).
  - **Worker 2 (`FormTask_13` / `FormTask_11`):** *Actualizar estado a Compra realizada / Sin compra* (`topic: actualizar-seguimiento-peluditos`).
  - **Worker 3 (`FormTask_23`):** *Seleccionar clientes para la campaña* (`topic: seleccionar-clientes-campana`). Filtra clientes con `CLIENTE_AUTORIZA = true` según dos criterios opcionales/combinables: **fecha/mes de cumpleaños** (`mesCumpleanos`) y **tipo/tamaño de mascota** (`especieMascota`, `tamanoMascota`).
  - **Worker 4 (`FormTask_29`):** *Realizar seguimiento de la campaña* (`topic: registrar-seguimiento-campana`). Registra en la base de datos la respuesta y resultado del seguimiento de la campaña comercial.
- **`TO-BE-Entrega4.png`**: representación visual actualizada del modelo TO-BE con los 4 External Workers generada por Flowable Engine OSS.
- **`form_registro_cliente.form`** y **`form_seguimiento_comercial.form`**: definiciones de formularios para Flowable.

## Variables del Recorrido 2 (Campañas, Cumpleaños y Fechas Especiales)

| Actividad BPMN | ID | Tipo | Variables de Entrada / Salida en Flowable |
| :--- | :--- | :--- | :--- |
| **Inicio campañas, cumpleaños y fechas especiales** | `StartNoneEvent_20` | `StartMessageEvent` (`MensajeActivarCampanasEntrega3`) | Inicia el recorrido de campañas mediante mensaje en Flowable OSS. |
| **Identificar cumpleaños de mascotas y fechas especiales** | `FormTask_21` | `UserTask` | **Define filtros:** `tipoCampana` (`"CUMPLEANOS"` / `"FECHA_ESPECIAL"`), `mesCumpleanos` (opcional), `especieMascota` (`"Perro"`, `"Gato"`, `"Todos"`), `tamanoMascota` (`"Pequeno"`, `"Mediano"`, `"Grande"`, `"Todos"`). |
| **Seleccionar clientes para la campaña** | `FormTask_23` | `ServiceTask` (`external-worker`)<br>Topic: **`seleccionar-clientes-campana`** | **Lee:** `tipoCampana`, `mesCumpleanos`, `especieMascota`, `tamanoMascota`.<br>**Escribe:** `clientesSeleccionados` (`true`/`false`), `cantidadClientesCampana`, `listaClientesCampana`. |
| **Preparar y enviar promociones** / **Registrar respuestas de los clientes** | `FormTask_25` / `FormTask_27` | `UserTask` | **Registra:** `clienteId`, `promocionEnviada`, `respuestaClienteCampana`, `compraRealizadaCampana` (`true`/`false`), `observacionesCampana`. |
| **Realizar seguimiento de la campaña** | `FormTask_29` | `ServiceTask` (`external-worker`)<br>Topic: **`registrar-seguimiento-campana`** | **Lee:** `clienteId`, `tipoCampana`, `promocionEnviada`, `respuestaClienteCampana`, `compraRealizadaCampana`, `observacionesCampana`.<br>**Escribe:** `seguimientoCampanaRegistrado` (`true`), `estadoCampana` (`"Finalizada"`). |

## Despliegue en Flowable Engine OSS (`:8080`)

Desde la raíz del repositorio:

```powershell
curl.exe -i -u rest-admin:test -F "file=@BPMN/peluditos-integracion-oss-entrega2.bpmn20.xml" "http://127.0.0.1:8080/flowable-rest/service/repository/deployments"
```
