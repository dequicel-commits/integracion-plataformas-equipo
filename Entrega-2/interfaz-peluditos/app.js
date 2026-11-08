const formulario = document.querySelector("#registro");
const botonRevisar = document.querySelector("#revisar");
const resumen = document.querySelector("#resumen");
const listaDatos = document.querySelector("#datos");
const botonEnviar = document.querySelector("#enviar");
const botonLimpiar = formulario.querySelector('[type="reset"]');
const estado = document.querySelector("#estado-registro");

let datosRevisados = false;
let referencia;

const etiquetas = {
  clienteNombre: "Nombre del cliente",
  clienteTelefono: "Teléfono o WhatsApp",
  clienteCorreo: "Correo electrónico",
  clienteCanalContacto: "Canal de contacto",
  clienteConsultaRealizada: "Consulta",
  clienteAutoriza: "Autoriza recibir promociones",
  mascotaNombre: "Nombre de la mascota",
  mascotaCumpleano: "Cumpleaños de la mascota",
  mascotaObservaciones: "Observaciones",
};

formulario.addEventListener("submit", (evento) => {
  evento.preventDefault();

  const datos = Object.fromEntries(new FormData(formulario));
  datos.clienteAutoriza = formulario.elements.clienteAutoriza.checked;
  listaDatos.replaceChildren();

  for (const [campo, etiqueta] of Object.entries(etiquetas)) {
    const valor = datos[campo];
    const texto = typeof valor === "boolean"
      ? (valor ? "Sí" : "No")
      : (valor ?? "").trim();

    if (!texto) continue;

    const nombre = document.createElement("dt");
    const detalle = document.createElement("dd");
    nombre.textContent = etiqueta;
    detalle.textContent = texto;
    listaDatos.append(nombre, detalle);
  }

  datosRevisados = true;
  resumen.hidden = false;
  resumen.scrollIntoView({ block: "nearest" });
});

formulario.addEventListener("input", () => {
  datosRevisados = false;
  resumen.hidden = true;
});

formulario.addEventListener("reset", () => {
  datosRevisados = false;
  referencia = undefined;
  estado.textContent = "";
  listaDatos.replaceChildren();
  resumen.hidden = true;
});

botonEnviar.addEventListener("click", async () => {
  if (!datosRevisados || !formulario.reportValidity()) return;

  const datos = Object.fromEntries(new FormData(formulario));
  datos.clienteAutoriza = formulario.elements.clienteAutoriza.checked;
  referencia ??= crypto.randomUUID();

  botonEnviar.disabled = true;
  botonRevisar.disabled = true;
  botonLimpiar.disabled = true;
  estado.textContent = "Enviando a Flowable...";

  try {
    const respuesta = await fetch(`/api/consultas/${referencia}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(datos),
    });
    const resultado = await respuesta.json();
    if (!respuesta.ok) throw new Error(resultado.mensaje || "No se pudo enviar el registro.");

    formulario.reset();
    estado.textContent = `Registro enviado a Flowable. Caso ${resultado.casoId}. El guardado queda pendiente del worker.`;
  } catch (error) {
    estado.textContent = error.message || "No se pudo conectar con el servicio.";
  } finally {
    botonEnviar.disabled = false;
    botonRevisar.disabled = false;
    botonLimpiar.disabled = false;
  }
});

botonRevisar.disabled = false;
