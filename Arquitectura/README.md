# Diagrama de Arquitectura — Peluditos Pet Chile (Entrega 3)

**Proyecto:** Peluditos Pet Chile  
**Herramienta utilizada:** Mermaid Live Editor

Se actualizó el diagrama de arquitectura de componentes reflejando la solución integrada de la **Entrega 3**:
- **Interfaz web propia (`interfaz-peluditos`)** separada para clientes y panel de la emprendedora.
- **Servicio Web Java con Jakarta REST (`servicio-peluditos` en `:7072`)** y persistencia relacional en **SQLite (`peluditos.db`)**.
- **Flowable Engine OSS (`:8080`)** con persistencia H2 y proceso BPMN TO-BE desplegado.
- **2 External Workers (`external-worker-peluditos`)**:
  - **Worker 1 (`guardar-cliente-peluditos`)**: registra clientes y mascotas en SQLite (`POST /api/clientes`).
  - **Worker 2 (`actualizar-seguimiento-peluditos`)**: actualiza el estado final de compra en SQLite (`PUT /api/clientes/{id}/seguimiento`).
- **Funcionalidad planificada para la Entrega 4**: Gestión de campañas y fechas especiales.

**Archivos incluidos:**
- `Arquitectura_Peluditos_Entrega3.png`: imagen del diagrama de arquitectura vigente.
- `Arquitectura_Peluditos_Entrega3.mmd`: código fuente editable en Mermaid.
