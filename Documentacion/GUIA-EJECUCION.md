# Guía de ejecución y configuración — Peluditos Pet Chile (Entrega 3)

**Asignatura:** Integración de Plataformas — IPLACEX  
**Grupo:** 3  
**Entrega:** Tercera evaluación

## 1. Objetivo

Esta guía tiene como finalidad explicar los pasos necesarios para configurar y ejecutar el sistema Peluditos Pet Chile, utilizando los componentes desarrollados durante la Entrega 3.

La solución está formada por una interfaz web desarrollada con HTML, CSS y JavaScript, servicios REST implementados en Java con Jakarta REST, dos External Workers y el motor de procesos Flowable OSS.

Para almacenar la información se utilizan dos bases de datos: SQLite, que guarda los datos de los clientes, sus mascotas y el seguimiento comercial, y H2, que permite almacenar la información correspondiente al funcionamiento del motor Flowable.

También se incluyen las variables de entorno, los comandos de ejecución y las pruebas principales, con el propósito de facilitar la preparación del sistema en otro computador.

Las pruebas realizadas hasta esta entrega corresponden principalmente al entorno local utilizado por el equipo. La instalación completa en otro computador todavía queda pendiente de comprobación.

---

## 2. Programas y herramientas utilizadas

| Programa | Versión o configuración | Uso |
|---|---|---|
| Java JDK | 17 (Amazon Corretto en las pruebas finales) | Compilación y ejecución de los componentes Java |
| Apache Maven | 3.10.0 en el entorno de trabajo | Gestión de dependencias y compilación |
| Jakarta REST | Jersey y Grizzly | Implementación y ejecución de servicios REST |
| SQLite JDBC | 3.47.1.0 | Almacenamiento de los datos comerciales |
| Flowable OSS | 7.2.0 | Ejecución del proceso BPMN |
| H2 | Base configurada con Flowable | Persistencia del motor de procesos |
| Visual Studio Code | Entorno de desarrollo | Revisión y modificación del código |
| Git y GitHub | Control de versiones | Organización del código y documentación |
| PowerShell | Terminal utilizada en Windows | Ejecución de comandos y pruebas |

Los dos proyectos Java utilizan maven.compiler.release=17 en sus archivos pom.xml. Durante las comprobaciones finales de la Entrega 3 se utilizó Amazon Corretto JDK 17, obteniendo resultados correctos en la compilación y ejecución de los componentes.

Para comprobar las versiones de Java y Maven se pueden ejecutar los siguientes comandos:

```powershell
java -version
mvn -version
```

### 2.1. Consideraciones sobre el sistema operativo y la terminal

Durante el desarrollo y las pruebas de la Entrega 3 se utilizó Windows con PowerShell y la terminal integrada de Visual Studio Code.

Los comandos incluidos en esta guía corresponden a ese entorno de trabajo. Sin embargo, como la solución está desarrollada principalmente en Java, también podría ejecutarse en otros sistemas operativos compatibles, siempre que se encuentren instaladas las herramientas y dependencias necesarias.

En caso de utilizar otro sistema operativo o terminal, será necesario adaptar los comandos de configuración según corresponda.

La ejecución completa del sistema en otros entornos todavía no ha sido comprobada por el equipo.

---

## 3. Estructura del repositorio

La solución se encuentra organizada en un monorepositorio de GitHub, donde cada carpeta contiene una parte del proyecto.

| Carpeta | Contenido |
|---|---|
| `BPMN/` | Modelos del proceso, formularios y archivo ejecutable |
| `interfaz-peluditos/` | Interfaces web desarrolladas en HTML, CSS y JavaScript |
| `servicio-peluditos/` | Servicios Jakarta REST y base de datos SQLite |
| `external-worker-peluditos/` | Aplicación Java con los dos External Workers |
| `Arquitectura/` | Diagrama actualizado de los componentes |
| `Documentacion/` | Análisis, implementación, pruebas e instrucciones |
| `Evidencias/` | Capturas y resultados de las pruebas realizadas |
| `Actas/` | Registros de coordinación del equipo |
| `Videos/` | Presentaciones de las entregas |

La versión actual de la Entrega 3 se encuentra en la rama `main`.

### 3.1. Descargar el proyecto

Abrir una terminal de PowerShell y ejecutar:

```powershell
git clone https://github.com/dequicel-commits/integracion-plataformas-equipo.git
cd integracion-plataformas-equipo
```

Estos comandos descargan el repositorio y permiten trabajar desde su carpeta principal.

Si el repositorio ya se encuentra descargado, primero deben revisarse los cambios locales antes de actualizarlo para no sobrescribir trabajo pendiente.

---

## 4. Componentes de la implementación

### 4.1. Interfaz web

La interfaz fue desarrollada con HTML, CSS y JavaScript.

Incluye las pantallas de registro de consultas, administración de clientes y seguimiento comercial.

JavaScript permite enviar solicitudes a los servicios REST y mostrar la información que recibe como respuesta.

### 4.2. Servicios Jakarta REST

Los servicios fueron desarrollados en Java utilizando Jakarta REST, con Jersey y Grizzly.

El servidor se inicia mediante la clase:

`cl.iplacex.peluditos.ServidorJakarta`

En el entorno de pruebas se utilizó el puerto `7072`.

Los servicios permiten registrar clientes, consultar información almacenada y actualizar el seguimiento comercial.

### 4.3. Flowable OSS

Flowable OSS 7.2.0 permite ejecutar el proceso BPMN de Peluditos Pet Chile.

El proceso utilizado por los servicios REST tiene la clave:

`peluditosPetChileTOBE12`

El motor se ejecutó localmente en el puerto `8080`.

### 4.4. External Workers

La aplicación `external-worker-peluditos` ejecuta dos tipos de tareas:

**Worker 1 — `guardar-cliente-peluditos`**

Recibe los datos del cliente y su mascota desde Flowable, los envía al servicio Jakarta REST y permite almacenarlos en SQLite.

**Worker 2 — `actualizar-seguimiento-peluditos`**

Recibe el resultado del seguimiento comercial y solicita la actualización del estado del cliente en SQLite.

### 4.5. Bases de datos

La solución utiliza dos bases de datos:

- **SQLite:** almacena clientes, mascotas y datos del seguimiento comercial.
- **H2:** almacena información correspondiente al funcionamiento de Flowable OSS.

Estas bases cumplen funciones diferentes dentro del proyecto.

---

## 5. Configuración externa y portabilidad

Para facilitar la ejecución en distintos entornos, los componentes Java utilizan variables de entorno.

Estas variables permiten modificar direcciones, puertos y otros parámetros sin cambiar directamente el código fuente.

### 5.1. Variables del servicio Jakarta REST

| Variable | Función | Ejemplo local |
|---|---|---|
| `PELU_HOST` | Dirección del servidor | `localhost` |
| `PELU_PORT` | Puerto del servidor | `7072` |
| `PELU_DB_PATH` | Ruta del archivo SQLite | `peluditos.db` |
| `FLOWABLE_REST_URL` | URL REST del motor Flowable | `http://127.0.0.1:8080/flowable-rest/service` |
| `FLOWABLE_USER` | Usuario de Flowable | `rest-admin` |
| `FLOWABLE_PASSWORD` | Contraseña de Flowable | Configurada durante la ejecución |

Ejemplo de configuración en PowerShell:

```powershell
$env:PELU_HOST = "localhost"
$env:PELU_PORT = "7072"
$env:PELU_DB_PATH = "peluditos.db"
$env:FLOWABLE_REST_URL = "http://127.0.0.1:8080/flowable-rest/service"
$env:FLOWABLE_USER = "rest-admin"
$env:FLOWABLE_PASSWORD = Read-Host "Ingresa la contraseña REST de Flowable"
```

Estos valores corresponden a un ejemplo local. En otro entorno se deben modificar según la ubicación real de los servicios.

### 5.2. Variables de los External Workers

| Variable | Función |
|---|---|
| `FLOWABLE_API_URL` | URL utilizada para consultar las tareas externas de Flowable |
| `FLOWABLE_USER` | Usuario de conexión con Flowable |
| `FLOWABLE_PASSWORD` | Contraseña correspondiente al usuario |
| `SERVICIO_PELUDITOS_URL` | Dirección base del servicio Jakarta REST |
| `WORKER_LOOP` | Permite mantener los Workers consultando nuevas tareas |

Ejemplo de configuración en PowerShell:

```powershell
$env:FLOWABLE_API_URL = "http://127.0.0.1:8080/flowable-rest/external-job-api"
$env:FLOWABLE_USER = "rest-admin"
$env:FLOWABLE_PASSWORD = Read-Host "Ingresa la contraseña REST de Flowable"
$env:SERVICIO_PELUDITOS_URL = "http://localhost:7072"
$env:WORKER_LOOP = "true"
```

Cada terminal debe tener configuradas las variables necesarias para el componente que se ejecutará en ella.

Las direcciones `localhost` y `127.0.0.1` corresponden al mismo equipo. Si Flowable o Jakarta REST se ejecutan en computadores diferentes, deberán reemplazarse por direcciones accesibles entre ellos.

### 5.3. Consideraciones de seguridad

No se deben publicar contraseñas, tokens ni credenciales reales en GitHub.

Los valores de ejemplo permiten mostrar cómo establecer la configuración sin entregar información privada.

Durante la revisión de la Entrega 3 se modificó la configuración del External Worker para eliminar la contraseña de prueba que se utilizaba automáticamente y las direcciones alternativas de los puertos antiguos. Actualmente, el Worker requiere configurar las variables de entorno necesarias para conectarse con Flowable y el servicio Jakarta REST. Esto permite trabajar con las direcciones y credenciales definidas para cada entorno, sin tener que modificar directamente el código.

---
## 6. Iniciar Flowable OSS

Para ejecutar la solución se necesita tener Flowable OSS 7.2.0 instalado y funcionando.

Durante las pruebas se utilizó Flowable REST en el puerto `8080` y una base H2 persistente.

El procedimiento exacto para iniciar Flowable depende de la instalación disponible en el computador.

En el entorno utilizado durante el desarrollo se trabajó con el archivo `flowable-rest.war`.

Antes de continuar, comprobar que el servicio REST de Flowable esté disponible y que se conozcan las credenciales correspondientes.

### 6.1. Desplegar el BPMN

Desde la carpeta principal del repositorio, abrir PowerShell.

Configurar las credenciales de la instalación local y ejecutar:

```powershell
$usuario = "rest-admin"
$clave = Read-Host "Contraseña REST de Flowable"

curl.exe -i -u "${usuario}:${clave}" -F "file=@BPMN/peluditos-integracion-oss-entrega2.bpmn20.xml" "http://127.0.0.1:8080/flowable-rest/service/repository/deployments"
```

Una vez ejecutado, se debe comprobar que Flowable responda correctamente y que el proceso quede desplegado.

**Importante sobre el nombre del archivo:**

El archivo ejecutable todavía conserva `entrega2` en su nombre, pero contiene las tareas automáticas correspondientes al recorrido integrado utilizado en la Entrega 3.

No debe reemplazarse automáticamente por `TO-BE.bpmn`, ya que ambos archivos presentan diferencias en sus actividades.

El archivo ejecutable contiene los temas:

- `guardar-cliente-peluditos`
- `actualizar-seguimiento-peluditos`

La parte relacionada con campañas y fechas especiales se mantiene como funcionalidad prevista para la Entrega 4.

---

## 7. Ejecutar Jakarta REST y SQLite

Después de iniciar Flowable, abrir otra terminal de PowerShell desde la carpeta principal del repositorio.

Ingresar al proyecto:

```powershell
cd servicio-peluditos
```

Configurar las variables necesarias utilizando los comandos indicados en el apartado 5.1.

### 7.1. Compilar el servicio

```powershell
mvn clean package
```

Durante las verificaciones de la Entrega 3 se obtuvo el resultado `BUILD SUCCESS` al compilar los componentes Java.

### 7.2. Iniciar Jakarta REST

```powershell
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.ServidorJakarta"
```

El servidor utiliza el puerto `7072` si no se establece otro mediante `PELU_PORT`.

La terminal debe mantenerse abierta durante las pruebas.

### 7.3. Comprobar el servicio REST

Desde otra ventana de PowerShell:

```powershell
curl.exe -i http://localhost:7072/api/seguimiento
```

Una respuesta HTTP 200 permite comprobar que el servidor está disponible.

También se puede consultar:

```powershell
curl.exe -i http://localhost:7072/api/clientes
```

Esta solicitud devuelve los registros almacenados en la base SQLite utilizada por el servidor.

Si la base todavía no contiene clientes, puede devolver:

```json
[]
```

Esto no necesariamente representa un error, ya que puede tratarse de una base de datos nueva.

---

## 8. Ejecutar los External Workers

Abrir otra terminal de PowerShell desde la carpeta principal del repositorio.

Ingresar al proyecto:

```powershell
cd external-worker-peluditos
```

Configurar las variables indicadas en el apartado 5.2.

### 8.1. Compilar

```powershell
mvn clean package
```

### 8.2. Ejecutar

```powershell
mvn compile exec:java "-Dexec.mainClass=cl.iplacex.peluditos.App"
```

El programa queda preparado para consultar las tareas externas de Flowable.

En el modo continuo, realiza consultas periódicas mientras se mantiene en ejecución.

El Worker 1 permite guardar la información comercial mediante el servicio REST.

El Worker 2 permite actualizar el resultado del seguimiento comercial, registrando si la compra fue realizada o no.

Durante las pruebas se observaron respuestas HTTP 201 para el registro de clientes y HTTP 200 para la actualización de seguimiento.

---

## 9. Abrir la interfaz web

Con Jakarta REST funcionando en el puerto `7072`, las pantallas se pueden abrir desde un navegador.

**Registro de consultas:**

`http://localhost:7072/interfaz/index.html`

**Panel de la emprendedora:**

`http://localhost:7072/interfaz/emprendedora.html`

**Listado de clientes:**

`http://localhost:7072/interfaz/clientes.html`

**Seguimiento comercial:**

`http://localhost:7072/interfaz/seguimiento.html`

Estas pantallas se comunican con los servicios REST mediante solicitudes realizadas con JavaScript.

La carpeta `interfaz-peluditos` debe conservar su ubicación junto a la carpeta `servicio-peluditos` para que el servidor pueda encontrar los archivos web.

Si se modifica el puerto del servicio, también se debe utilizar el nuevo puerto para acceder a las interfaces.

---

## 10. Pruebas de los recorridos principales

### 10.1. Registro de un cliente

Para comprobar el funcionamiento del registro:

1. Abrir el formulario de consultas.
2. Ingresar datos ficticios de un cliente y su mascota.
3. Completar los campos obligatorios.
4. Enviar la información.
5. Comprobar el mensaje entregado por la interfaz.
6. Revisar que Flowable inicie el proceso.
7. Comprobar que el Worker 1 procese el registro.
8. Consultar el cliente almacenado mediante `GET /api/clientes`.

El registro debe quedar relacionado con su correspondiente caso Flowable.

### 10.2. Seguimiento comercial

Para verificar el seguimiento:

1. Abrir la pantalla de seguimiento comercial.
2. Seleccionar un cliente registrado que tenga un caso Flowable pendiente.
3. Indicar si realizó o no la compra.
4. Agregar una observación.
5. Enviar el seguimiento.
6. Comprobar que Flowable avance por la ruta correspondiente.
7. Revisar que el Worker 2 actualice el estado.
8. Consultar nuevamente la información almacenada.

Durante las pruebas se verificaron las rutas:

- **Compra realizada**
- **Sin compra**

Ambas fueron registradas en las evidencias BPMN del proyecto.

### 10.3. Validación de solicitudes incompletas

También se verificó la respuesta del servicio cuando recibe un registro sin los datos obligatorios.

Desde PowerShell:

```powershell
curl.exe -i -X POST http://localhost:7072/api/clientes -H "Content-Type: application/json" --data-raw "{}"
```

Durante la prueba QA documentada se obtuvo una respuesta HTTP 400, con el mensaje:

`El nombre del cliente es obligatorio`

Este resultado permitió comprobar que el servicio rechazaba la solicitud incompleta en el entorno utilizado.

---

## 11. Persistencia y recuperación de datos

### 11.1. SQLite

El archivo SQLite almacena información relacionada con los clientes, sus mascotas y el seguimiento comercial.

La base contiene las siguientes tablas principales:

- `TABLA_CLIENTE`
- `TABLA_MASCOTA`
- `TABLA_TIPO_MASCOTA`
- `TABLA_PRODUCTO`

La clase `BaseDatos.java` contiene las instrucciones para inicializar las tablas necesarias.

También contempla la incorporación de la columna `FLOWABLE_CASO_ID` cuando se trabaja con una base anterior que todavía no contiene ese campo.

La ubicación de SQLite depende del valor de la variable `PELU_DB_PATH`.

Cuando se utiliza una base nueva, los registros creados en pruebas anteriores no aparecen automáticamente.

### 11.2. H2

Flowable utiliza H2 para almacenar información del motor de procesos.

Durante las pruebas se comprobó la existencia de los archivos de la base de datos y se revisó la configuración utilizada.

Para una instalación definitiva es necesario comprobar que H2 esté configurada con almacenamiento persistente y que la información se conserve después de reiniciar Flowable.

### 11.3. Responsabilidad de los componentes

SQLite guarda la información comercial.

H2 guarda la información interna de Flowable.

Los servicios REST y los External Workers permiten relacionar ambos componentes durante la ejecución de los procesos.

---

## 12. Problemas frecuentes

### Jakarta REST no inicia

Comprobar que Java y Maven estén correctamente configurados.

Revisar que el puerto `7072` se encuentre disponible o modificar `PELU_PORT` para utilizar otro puerto.

### Flowable no responde

Comprobar que Flowable REST esté ejecutándose en el puerto correspondiente.

Revisar las variables `FLOWABLE_REST_URL`, `FLOWABLE_API_URL`, `FLOWABLE_USER` y `FLOWABLE_PASSWORD`.

### Los Workers no procesan tareas

Comprobar que el BPMN esté desplegado.

Revisar que los Workers estén ejecutándose y que tengan configuradas las direcciones correctas de Flowable y Jakarta REST.

### No aparecen los clientes registrados

Comprobar que SQLite esté utilizando el archivo esperado mediante `PELU_DB_PATH`.

Si se creó una base nueva, será necesario ingresar registros ficticios para probar el sistema.

### No se muestra la interfaz web

Revisar que la carpeta `interfaz-peluditos` se encuentre en la estructura esperada del repositorio.

Comprobar también que Jakarta REST esté ejecutándose.

### No aparecen clientes para realizar seguimiento

Revisar que los clientes registrados tengan un identificador de caso Flowable y se encuentren en el estado correspondiente al seguimiento.

### Los comandos no funcionan

Comprobar que se estén utilizando desde la carpeta correspondiente.

Revisar que Java, Maven y Git estén correctamente instalados.

Si se utiliza una terminal distinta a la presentada en esta guía, será necesario adaptar los comandos de configuración.

---

## 13. Limitaciones y tareas pendientes

Las pruebas realizadas durante la Entrega 3 permitieron comprobar los principales recorridos en el entorno local del equipo.

Sin embargo, todavía quedan actividades que deberán revisarse para la Entrega 4.

### Instalación en otro computador

Se debe comprobar el procedimiento completo en un entorno distinto del utilizado durante el desarrollo.

### Configuración externa

Durante la Entrega 3 se corrigió la configuración externa del Worker y se realizaron nuevas pruebas para comprobar su funcionamiento. Se obtuvo una compilación exitosa mediante Maven y se verificó la conexión con Flowable y Jakarta REST. El Worker 1 registró correctamente un cliente en SQLite mediante una respuesta HTTP 201, mientras que el Worker 2 actualizó el seguimiento comercial con una respuesta HTTP 200. Estas pruebas permitieron comprobar el funcionamiento de la configuración externa en el entorno local utilizado por el equipo. Queda pendiente verificar la instalación completa en otro computador.

### Manejo de errores

Es necesario ampliar las pruebas frente a problemas de conexión, interrupciones de servicios y otros datos incorrectos.

### Respaldo y recuperación

Se debe verificar un procedimiento completo para respaldar y recuperar la información almacenada en SQLite y H2.

### Pruebas automáticas

Se recomienda ampliar las pruebas automatizadas de los servicios y de los recorridos principales.

### Funcionalidades pendientes

La gestión de campañas, cumpleaños de mascotas y fechas especiales se mantiene como funcionalidad prevista para la Entrega 4.

---

## 14. Evidencias y documentación relacionada

Los resultados de implementación y pruebas se encuentran organizados en las siguientes rutas:

**Implementación:**

`Documentacion/IMPLEMENTACION-ENTREGA3.md`

`Evidencias/Implementacion/Entrega-3/`

**QA/Tester:**

`Documentacion/Informe-Pruebas-QA-Entrega3.md`

`Evidencias/QA/Entrega-3/`

**BPMN:**

`BPMN/`

`Evidencias/BPMN/RESULTADO-PRUEBAS-BPMN.txt`

**Arquitectura:**

`Arquitectura/Arquitectura_Peluditos_Entrega3.png`

`Arquitectura/Arquitectura_Peluditos_Entrega3.mmd`

**Análisis:**

`Documentacion/Analisis/`

Estas evidencias respaldan las pruebas realizadas y permiten identificar los avances del proyecto.

---

## 15. Conclusión

Durante la Entrega 3 se trabajó en la implementación y comprobación de los principales componentes del sistema Peluditos Pet Chile.

Se utilizaron servicios Jakarta REST, una interfaz web propia, Flowable OSS, dos External Workers y bases de datos para mantener la información correspondiente al proceso.

Las pruebas realizadas permitieron comprobar los recorridos de registro y seguimiento comercial, además de identificar aspectos que necesitan mayor revisión antes de una instalación definitiva.

Con esta guía se busca dejar documentada la configuración y ejecución de los componentes, facilitando la continuidad del proyecto y la preparación de la Entrega 4.


