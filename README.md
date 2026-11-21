# Peluditos Pet Chile - Integración de Plataformas

Proyecto del curso Integración de Plataformas de IPLACEX. Desarrollamos una solución basada en **BPMN (Flowable Engine OSS)**, **Web Services en Java con Jakarta REST (JAX-RS)**, **External Workers**, **persistencia relacional en SQLite** y una **interfaz web propia** para gestionar el registro de potenciales clientes, sus mascotas y el seguimiento comercial de Peluditos Pet Chile.

**Profesor:** Santiago Neira.  
**Grupo:** 3.  
**Etapa actual:** Entrega 3 (23 de noviembre de 2026).  
**Versiones anteriores en Git Tags:** `entrega-1`, `entrega-2`.

## Integrantes y roles (Entrega 3)

| Integrante | Rol en Entrega 3 | Trabajo principal |
| --- | --- | --- |
| Elvis Brayan Gutiérrez Bernal | Analista | Análisis de requisitos funcionales/no funcionales y matriz de trazabilidad de la Entrega 3. |
| Felipe Octavio Quicel Soto | Modelador BPMN | Actualización del proceso BPMN TO-BE ejecutable en Flowable OSS, imagen del modelo y validación de ambas rutas. |
| Geraldine Andrea González Acuña | Implementadora y QA/Tester | Migración de servicios web a Jakarta REST (`:7072`) con SQLite, ampliación de la interfaz web, pruebas QA y actas. |
| Lautaro Nelson Salinas Alvear | Integrador | Integración de los 2 External Workers con Flowable OSS y Jakarta REST, cierre del recorrido de seguimiento y organización del monorepo. |

## Estructura del monorepo

El repositorio mantiene una única versión vigente por componente (el historial de entregas previas está etiquetado con los tags `entrega-1` y `entrega-2`):

```text
integracion-plataformas-equipo/
├── README.md
├── Actas/                         # Actas de reuniones y acuerdos del equipo
├── Arquitectura/                  # Diagrama de arquitectura (.mmd y .png)
├── BPMN/                          # Modelos AS-IS, TO-BE ejecutable (.bpmn20.xml) e imagen .png
├── interfaz-peluditos/            # Aplicación web propia (HTML, CSS y JS)
├── servicio-peluditos/            # Web Services con Jakarta REST (Jersey/Grizzly :7072) y SQLite
├── external-worker-peluditos/     # External Workers Java (guardar-cliente y actualizar-seguimiento)
├── Documentacion/
│   ├── Analisis/                  # Requisitos, Matriz de Trazabilidad y Modelo Entidad-Relación
│   └── GUIA-EJECUCION.md          # Instrucciones paso a paso para levantar y probar la solución
├── Evidencias/
│   ├── BPMN/                      # Resultados y capturas de ejecución de las rutas SI y NO
│   ├── Implementacion/            # Capturas de funcionamiento de la implementación
│   └── QA/                        # Informe de pruebas QA/Tester y evidencias
└── Videos/                        # Enlaces a los videos de presentación (Emprendedora y Técnico)
```

## Cómo revisar la documentación y artefactos

- **Arquitectura y Modelos:**
  - [Diagrama de arquitectura de la solución](Arquitectura/README.md).
  - [Modelos BPMN (AS-IS, TO-BE ejecutable e imagen)](BPMN/README.md).
- **Análisis y Base de Datos:**
  - [Análisis de requisitos — Entrega 3](Documentacion/Analisis/ANALISIS-REQUISITOS-ENTREGA3.md).
  - [Matriz de trazabilidad de requisitos](Documentacion/Analisis/MATRIZ-TRAZABILIDAD-ENTREGA3.md).
  - [Diagrama Entidad-Relación y tablas SQLite](Documentacion/Analisis/DIAGRAMA-ENTIDAD-RELACION.MD).
- **Guía de ejecución y Evidencias:**
  - [Guía paso a paso de ejecución y pruebas](Documentacion/GUIA-EJECUCION.md).
  - [Evidencias de pruebas de rutas BPMN](Evidencias/BPMN/RESULTADO-PRUEBAS-BPMN.txt).
  - [Informe de pruebas QA / Tester](Evidencias/QA/Informe-Pruebas-QA.md).
- **Coordinación del equipo:**
  - [Actas de coordinación](Actas/).

## Catálogo de Web Services (Jakarta REST — `servicio-peluditos` `:7072`)

| Método y Ruta | Recurso Jakarta REST | Propósito | Entrada / Salida | Actividad BPMN / Consumidor |
| --- | --- | --- | --- | --- |
| `POST /api/consultas/{referencia}` | `ConsultasResource` | Inicia instancia `peluditosPetChileTOBE12` en Flowable OSS y completa `FormTask_1`. | JSON con datos del formulario web $\rightarrow$ `202 Accepted` (`{"casoId": "..."}`) | Interfaz web (`/interfaz/index.html`) |
| `POST /api/consultas/{casoId}/seguimiento` | `ConsultasResource` | Completa en Flowable OSS la atención (`FormTask_3`) y el seguimiento (`FormTask_5`) con la decisión de compra. | `{"compraRealizada": true/false, "clienteObservaciones": "..."}` $\rightarrow$ `200 OK` | Interfaz web (`/interfaz/seguimiento.html`) |
| `POST /api/clientes` | `ClientesResource` | Persiste al potencial cliente y su mascota en `TABLA_CLIENTE` y `TABLA_MASCOTA` (SQLite). | JSON del cliente y mascota $\rightarrow$ `201 Created` (`{"estado":"OK","clienteId":...,"mascotaId":...}`) | **External Worker 1** (`topic: guardar-cliente-peluditos`) |
| `PUT /api/clientes/{id}/seguimiento` | `ClientesResource` | Actualiza en `TABLA_CLIENTE` el estado final (`"Compra realizada"` o `"Sin compra"`) y observaciones. | `{"compraRealizada": true/false, "clienteEstadoCompra": "...", "clienteObservaciones": "..."}` $\rightarrow$ `200 OK` | **External Worker 2** (`topic: actualizar-seguimiento-peluditos`) |
| `GET /api/clientes` | `ClientesResource` | Lista todos los clientes y mascotas almacenados en SQLite (`peluditos.db`). | `200 OK` — Arreglo JSON de clientes y mascotas | Interfaz web (`/interfaz/clientes.html`) |
| `GET /api/seguimiento` | `EstadoResource` | Verifica la disponibilidad del servidor Jakarta REST. | `200 OK` — `{"estado":"OK","mensaje":"..."}` | Verificación de estado / QA |

## Ejecución rápida del recorrido integrado

1. **Desplegar el BPMN en Flowable OSS (`:8080`):**
   ```powershell
   curl.exe -i -u rest-admin:test -F "file=@BPMN/peluditos-integracion-oss-entrega2.bpmn20.xml" "http://127.0.0.1:8080/flowable-rest/service/repository/deployments"
   ```
2. **Iniciar el Servidor Jakarta REST y SQLite (`:7072`):**
   ```powershell
   cd servicio-peluditos
   mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.ServidorJakarta"
   ```
   *(La interfaz web queda disponible en `http://localhost:7072/interfaz/index.html` y `http://localhost:7072/interfaz/emprendedora.html`).*
3. **Ejecutar los External Workers (Worker 1 y Worker 2):**
   ```powershell
   cd external-worker-peluditos
   mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
   ```
