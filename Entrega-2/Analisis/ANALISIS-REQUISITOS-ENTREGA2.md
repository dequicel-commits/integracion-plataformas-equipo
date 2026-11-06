# Análisis de Requisitos — Peluditos Pet Chile

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Evaluación:** Entrega 2  
**Rol:** Analista

## 1. Descripción del proyecto

Peluditos Pet Chile es un emprendimiento dedicado a la elaboración y venta de productos de pastelería para mascotas, principalmente perros y gatos.

Actualmente, la emprendedora recibe consultas mediante WhatsApp, Instagram, Facebook, TikTok y otros canales digitales. Esto dificulta llevar un registro ordenado de los potenciales clientes y realizar seguimiento a quienes consultan por sus productos.

El proyecto busca mejorar esta situación mediante una solución que permita registrar potenciales clientes, guardar su información, realizar seguimiento comercial y conocer si finalmente concretaron una compra.

También se contempla incorporar campañas promocionales relacionadas con cumpleaños de mascotas y fechas especiales.

## 2. Objetivo del sistema

Desarrollar una solución que permita organizar la información de los potenciales clientes de Peluditos Pet Chile y mejorar el seguimiento comercial, utilizando BPMN, Flowable y servicios web desarrollados en Java.

El objetivo es facilitar la atención de consultas y mantener información que posteriormente pueda utilizarse para generar nuevas oportunidades comerciales.

## 3. Requisitos funcionales — Entrega 2

Los requisitos funcionales son las acciones que el sistema debe permitir realizar.

| Requisito | Descripción |
|---|---|
| Registrar potenciales clientes | Permitir ingresar la información de las personas que consultan por los productos. |
| Guardar datos del cliente | Almacenar la información registrada para utilizarla posteriormente. |
| Atender consultas | Permitir gestionar consultas sobre productos, precios, personalizaciones y fechas de entrega. |
| Realizar seguimiento comercial | Mantener un registro de las acciones de seguimiento realizadas a los potenciales clientes. |
| Registrar resultado de compra | Identificar si el potencial cliente concretó una compra o no. |

Estos requisitos están representados en el BPMN TO-BE de la Entrega 2. Su funcionamiento completo deberá comprobarse mediante las integraciones y pruebas correspondientes.

## 4. Datos importantes de los clientes

Para realizar un seguimiento adecuado, se propone registrar la siguiente información:

| Dato | Descripción | Importancia |
|---|---|---|
| ID del cliente | Identificador único generado por el sistema. | Diferenciar los registros de clientes. |
| Nombre del cliente | Nombre de la persona interesada. | Identificar al cliente. |
| Teléfono o WhatsApp | Número de contacto. | Facilitar la comunicación. |
| Correo electrónico | Medio de contacto adicional, cuando esté disponible. | Mantener otra opción de contacto. |
| Canal de contacto | WhatsApp, Instagram, Facebook, TikTok u otro. | Identificar de dónde provienen las consultas. |
| Fecha de consulta | Fecha del primer contacto. | Mantener un orden cronológico. |
| Consulta realizada | Información o producto solicitado. | Conocer el interés del cliente. |
| Estado de compra | Indica si realizó una compra o no. | Registrar el resultado comercial. |
| Observaciones | Comentarios importantes de la atención. | Facilitar el seguimiento posterior. |

### 4.1. Datos adicionales de las mascotas

Pensando en las futuras campañas, se propone considerar:

| Dato | Descripción |
|---|---|
| Nombre de la mascota | Nombre del perro, gato u otra mascota. |
| Tipo de mascota | Perro, gato u otro. |
| Fecha de cumpleaños | Fecha importante para futuras promociones. |
| Preferencias o necesidades | Información relevante para personalizar los productos ofrecidos. |

Estos campos son una **propuesta inicial del análisis**. Los datos definitivos deberán revisarse con el equipo antes de implementar la base de datos.

También será necesario considerar la autorización del cliente para recibir promociones y el tratamiento adecuado de sus datos personales.

## 5. Requisitos funcionales planificados — Entrega 3

Para la Entrega 3 se contempla desarrollar la gestión de campañas y fechas especiales.

| Requisito | Descripción |
|---|---|
| Identificar fechas especiales | Reconocer cumpleaños de mascotas y fechas comerciales relevantes. |
| Seleccionar clientes | Identificar los clientes que participarán en una campaña. |
| Preparar y enviar promociones | Gestionar promociones dirigidas a los clientes seleccionados. |
| Registrar respuestas | Mantener un registro de las respuestas recibidas. |
| Realizar seguimiento de campañas | Dar seguimiento a los clientes interesados en las promociones. |

Estas funcionalidades están representadas en el BPMN TO-BE, pero su implementación está planificada para la Entrega 3.

La idea es que posteriormente puedan utilizar la información almacenada durante el registro y seguimiento de clientes.

## 6. Relación con el BPMN TO-BE

El modelo TO-BE considera dos recorridos:

**Seguimiento comercial:** incluye el registro del potencial cliente, el guardado de información mediante una tarea automática, la atención de consultas, el seguimiento comercial y el registro del resultado de la compra.

**Campañas y fechas especiales:** incluye la identificación de fechas importantes, selección de clientes, preparación de promociones, registro de respuestas y seguimiento de campañas.

Ambos recorridos están representados en un mismo archivo BPMN, pero su integración funcional se contempla para una etapa posterior.

## 7. Aspectos pendientes de confirmar con el equipo

Antes de finalizar la implementación, será necesario acordar:

- Los campos definitivos de la base de datos.
- Cuáles datos serán obligatorios y cuáles opcionales.
- Cómo se registrarán las acciones de seguimiento comercial.
- Cómo se relacionará cada cliente con sus mascotas.
- Cómo se utilizará la información registrada para las campañas futuras.
- La integración entre Flowable, el External Worker, los servicios REST y la base de datos.

## 8. Conclusión

El análisis permitió identificar las principales necesidades de Peluditos Pet Chile y relacionarlas con las actividades representadas en el BPMN TO-BE.

Para la Entrega 2, el trabajo se concentra en el registro de potenciales clientes y su seguimiento comercial. La gestión de campañas y fechas especiales queda contemplada para la Entrega 3.

Este documento servirá como referencia para que el equipo pueda definir los datos necesarios, desarrollar las integraciones y realizar las pruebas correspondientes.

Los requisitos propuestos podrán actualizarse de acuerdo con las observaciones y decisiones del grupo.
