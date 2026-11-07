## Modelo de Base de Datos — Peluditos Pet Chile (Entrega 2)
```mermaid
erDiagram
    TABLA_CLIENTE ||--o{ TABLA_MASCOTA : "es dueno de"
    TABLA_TIPO_MASCOTA ||--o{ TABLA_MASCOTA : "clasifica a"
    TABLA_TIPO_MASCOTA ||--o{ TABLA_PRODUCTO : "orientado a"
    TABLA_CLIENTE {
        int CLIENTE_ID PK "Obligatorio (Autoincremental)"
        string CLIENTE_NOMBRE "Obligatorio"
        string CLIENTE_TELEFONO "Obligatorio"
        string CLIENTE_CORREO "Opcional"
        string CLIENTE_CANAL_CONTACTO "Obligatorio"
        string CLIENTE_FECHA_CONSULTA "Obligatorio"
        string CLIENTE_CONSULTA_REALIZADA "Obligatorio"
        string CLIENTE_ESTADO_COMPRA "Obligatorio"
        string CLIENTE_OBSERVACIONES "Opcional"
    }
    TABLA_MASCOTA {
        int MASCOTA_ID PK "Obligatorio (Autoincremental)"
        int CLIENTE_ID FK "Obligatorio (Relacion con TABLA_CLIENTE)"
        int MASCOTA_TIPO_ID FK "Obligatorio (Relacion con TABLA_TIPO_MASCOTA)"
        string MASCOTA_NOMBRE "Obligatorio"
        string MASCOTA_CUMPLEANO "Opcional"
        string MASCOTA_OBSERVACIONES "Opcional"
    }
    TABLA_TIPO_MASCOTA {
        int MASCOTA_TIPO_ID PK "Obligatorio"
        string MASCOTA_ESPECIE "Obligatorio (Perro, Gato, Otro)"
        string MASCOTA_TAMANO "Obligatorio (Pequeno, Mediano, Grande)"
    }
    TABLA_PRODUCTO {
        int PRODUCTO_ID PK "Obligatorio"
        string PRODUCTO_NOMBRE "Obligatorio"
        string PRODUCTO_TIPO "Obligatorio (Torta, Galletas, Snack, etc.)"
        int MASCOTA_TIPO_ID FK "Obligatorio (Relacion con TABLA_TIPO_MASCOTA)"
    }
```
### Detalle de Tablas y Campos
#### 1. `TABLA_CLIENTE`
| Dato | Tipo sugerido | Obligatoriedad | Rol / Clave |
| :--- | :--- | :--- | :--- |
| `CLIENTE_ID` | `INTEGER` | Obligatorio | Primary Key (PK) |
| `CLIENTE_NOMBRE` | `TEXT` | Obligatorio | Nombre del potencial cliente |
| `CLIENTE_TELEFONO` | `TEXT` | Obligatorio | Teléfono o WhatsApp |
| `CLIENTE_CORREO` | `TEXT` | Opcional | Correo electrónico |
| `CLIENTE_CANAL_CONTACTO` | `TEXT` | Obligatorio | WhatsApp, Instagram, Facebook, TikTok u otro |
| `CLIENTE_FECHA_CONSULTA` | `TEXT` | Obligatorio | Fecha del primer contacto |
| `CLIENTE_CONSULTA_REALIZADA` | `TEXT` | Obligatorio | Producto o información consultada |
| `CLIENTE_ESTADO_COMPRA` | `TEXT` | Obligatorio | Pendiente / Compra realizada / Sin compra |
| `CLIENTE_OBSERVACIONES` | `TEXT` | Opcional | Notas del seguimiento comercial |
#### 2. `TABLA_MASCOTA`
| Dato | Tipo sugerido | Obligatoriedad | Rol / Clave |
| :--- | :--- | :--- | :--- |
| `MASCOTA_ID` | `INTEGER` | Obligatorio | Primary Key (PK) |
| `CLIENTE_ID` | `INTEGER` | Obligatorio | Foreign Key (FK → `TABLA_CLIENTE`) |
| `MASCOTA_TIPO_ID` | `INTEGER` | Obligatorio | Foreign Key (FK → `TABLA_TIPO_MASCOTA`) |
| `MASCOTA_NOMBRE` | `TEXT` | Obligatorio | Nombre de la mascota |
| `MASCOTA_CUMPLEANO` | `TEXT` | Opcional | Fecha de cumpleaños para campañas |
| `MASCOTA_OBSERVACIONES` | `TEXT` | Opcional | Alergias, preferencias o notas |
#### 3. `TABLA_TIPO_MASCOTA`
| Dato | Tipo sugerido | Obligatoriedad | Rol / Clave |
| :--- | :--- | :--- | :--- |
| `MASCOTA_TIPO_ID` | `INTEGER` | Obligatorio | Primary Key (PK) |
| `MASCOTA_ESPECIE` | `TEXT` | Obligatorio | Perro, Gato u otro |
| `MASCOTA_TAMANO` | `TEXT` | Obligatorio | Pequeño, Mediano, Grande |
#### 4. `TABLA_PRODUCTO`
| Dato | Tipo sugerido | Obligatoriedad | Rol / Clave |
| :--- | :--- | :--- | :--- |
| `PRODUCTO_ID` | `INTEGER` | Obligatorio | Primary Key (PK) |
| `PRODUCTO_NOMBRE` | `TEXT` | Obligatorio | Nombre del producto |
| `PRODUCTO_TIPO` | `TEXT` | Obligatorio | Categoría (Pastelería, Snack, etc.) |
| `MASCOTA_TIPO_ID` | `INTEGER` | Obligatorio | Foreign Key (FK → `TABLA_TIPO_MASCOTA`) |
| `PRODUCTO_NOMBRE` | `TEXT` | Obligatorio | Nombre del producto |
| `PRODUCTO_TIPO` | `TEXT` | Obligatorio | Categoría (Pastelería, Snack, etc.) |
| `MASCOTA_TIPO_ID` | `INTEGER` | Obligatorio | Foreign Key (FK → `TABLA_TIPO_MASCOTA`) |
