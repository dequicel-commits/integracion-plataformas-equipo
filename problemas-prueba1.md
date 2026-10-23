## Hallazgos y Ajustes durante las Pruebas

### 1. Conflicto con el puerto `7070` (`BindException`)
* **Problema detectado:** En mi equipo utilizo **AnyDesk**, el cual ocupa por defecto el puerto `7070`. Como el proyecto `servicio-peluditos` tenía configurado de forma fija ese mismo puerto, al iniciarlo arrojaba un error de puerto en uso (`BindException: Address already in use`).
* **Solución aplicada:** Cambié la configuración al puerto **`7071`** (`http://localhost:7071/api/seguimiento`) tanto en el servicio REST (`servicio-peluditos`) como en el cliente del Worker (`external-worker-peluditos`).

---

### 2. Variable `compraRealizada` :
* **Problema detectado:** Después de pasar por el External Worker (`ExternalWorkerTask_21`), el proceso llega a la compuerta exclusiva que evalúa `${compraRealizada == true}` / `${compraRealizada == false}`. Originalmente, el Worker completaba el trabajo sin enviar esta variable:
  ```java
  worker.createCompletionBuilder(trabajo).complete();
  ```
  Esto produce un error en el motor si el proceso se inicia desde la interfaz sin haber inyectado previamente la variable por JSON.
* **Solución aplicada:** Ajusté `external-worker-peluditos` para que envíe la variable al completar el trabajo:
  ```java
  worker.createCompletionBuilder(trabajo)
          .variable("compraRealizada", true)
          .complete();
  ```

---

### 3. Formulario faltante en la Tarea 3 (`form_seguimiento_comercial`)
* **Problema detectado:** Al ejecutar el modelo original en `flowable-ui`, la tarea **`Realizar seguimiento comercial`** (`FormTask_5`) mantenía la referencia `formKey="form_seguimiento_comercial"`, pero el archivo `.form` correspondiente no estaba incluido en el repositorio. Esto generaba la excepción:
  ```text
  FlowableObjectNotFoundException: No form definition found for key 'form_seguimiento_comercial'
  ```
* **Solución aplicada:** Se completó la tarea directamente a través de la API REST de Flowable (`POST /process-api/runtime/tasks/{id}`) enviando la variable `compraRealizada = true`, permitiendo que la instancia avanzara hacia el External Worker y finalizara el flujo correctamente.
