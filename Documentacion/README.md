# Documentación — Peluditos Pet Chile

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Etapa:** Entrega 3

## Objetivo

Centralizar el análisis de requisitos, el diseño de datos y la guía de ejecución de la solución Peluditos Pet Chile para la Entrega 3.

## Organización del equipo (Entrega 3)

- **Elvis Gutiérrez:** Analista.
- **Felipe Quicel:** Modelador BPMN.
- **Geraldine González:** Implementadora y QA/Tester (y registro de actas).
- **Lautaro Salinas:** Integrador.

## Contenido de esta carpeta

- **`Analisis/`**:
  - [Análisis de Requisitos — Entrega 3](Analisis/ANALISIS-REQUISITOS-ENTREGA3.md)
  - [Matriz de Trazabilidad de Requisitos](Analisis/MATRIZ-TRAZABILIDAD-ENTREGA3.md)
  - [Diagrama Entidad-Relación (SQLite)](Analisis/DIAGRAMA-ENTIDAD-RELACION.MD)
- **[Implementación del sistema — Entrega 3](IMPLEMENTACION-ENTREGA3.md)**: detalle de las interfaces web, servicio Jakarta REST, External Workers y almacenamiento.
- **[Informe de Pruebas QA — Entrega 3](Informe-Pruebas-QA-Entrega3.md)**: registro completo de verificaciones QA, manejo de errores e integración.
- **[Guía de Ejecución y Pruebas](GUIA-EJECUCION.md)**: pasos para levantar Flowable OSS, el servidor Jakarta REST (`:7072`), la interfaz web y los 2 External Workers.

## Organización del monorepo

El repositorio está organizado por componentes y responsabilidades (las versiones históricas de las entregas anteriores se conservan mediante los tags de Git `entrega-1` y `entrega-2`):

```text
integracion-plataformas-equipo/
├── README.md
├── Actas/
├── Arquitectura/
├── BPMN/
├── interfaz-peluditos/
├── servicio-peluditos/
├── external-worker-peluditos/
├── Documentacion/
│   ├── Analisis/
│   └── GUIA-EJECUCION.md
├── Evidencias/
│   ├── BPMN/
│   ├── Implementacion/
│   └── QA/
└── Videos/
```
