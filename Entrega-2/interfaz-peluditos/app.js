const formulario = document.querySelector("#registro");
const botonRevisar = document.querySelector("#revisar");
const resumen = document.querySelector("#resumen");
const listaDatos = document.querySelector("#datos");

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

  resumen.hidden = false;
  resumen.scrollIntoView({ block: "nearest" });
});

formulario.addEventListener("reset", () => {
  listaDatos.replaceChildren();
  resumen.hidden = true;
});

botonRevisar.disabled = false;
