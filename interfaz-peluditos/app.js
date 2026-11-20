const formulario = document.querySelector("#registro");
const botonRevisar = document.querySelector("#revisar");
const resumen = document.querySelector("#resumen");
const botonVolver = document.querySelector("#volver");
const listaDatos = document.querySelector("#datos");
const botonEnviar = document.querySelector("#enviar");
const botonLimpiar = formulario?.querySelector('[type="reset"]');
const estado = document.querySelector("#estado-registro");

const formSeguimiento = document.querySelector("#form-seguimiento");
const inputCasoId = document.querySelector("#casoIdSeguimiento");
const selectCompra = document.querySelector("#compraRealizada");
const inputObsSeguimiento = document.querySelector("#observacionesSeguimiento");
const botonSeguimiento = document.querySelector("#enviar-seguimiento");
const estadoSeguimiento = document.querySelector("#estado-seguimiento");

const botonCargarClientes = document.querySelector("#cargar-clientes");
const contenedorClientes = document.querySelector("#contenedor-clientes");

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

if (formulario) {

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
  formulario.hidden = true;
  resumen.hidden = false;
  resumen.scrollIntoView({ block: "start" });
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
  formulario.hidden = false;
});
}

if (botonVolver) {
  botonVolver.addEventListener("click", () => {
    resumen.hidden = true;
    formulario.hidden = false;
    formulario.elements.clienteNombre.focus();
  });
}

if (botonEnviar) {

botonEnviar.addEventListener("click", async () => {
  if (!datosRevisados || !formulario.reportValidity()) return;

  const datos = Object.fromEntries(new FormData(formulario));
  datos.clienteAutoriza = formulario.elements.clienteAutoriza.checked;
  referencia ??= crypto.randomUUID();

  botonEnviar.disabled = true;
  botonRevisar.disabled = true;
  botonLimpiar.disabled = true;
  estado.textContent = "Enviando tu consulta, por favor espera...";

  try {
    const respuesta = await fetch(`/api/consultas/${referencia}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(datos),
    });
    const resultado = await respuesta.json();
    if (!respuesta.ok) throw new Error(resultado.mensaje || "No se pudo enviar el registro.");

    formulario.reset();
    if (inputCasoId && resultado.casoId) {
      inputCasoId.value = resultado.casoId;
    }
    estado.textContent = "Tu consulta fue recibida correctamente. Gracias por comunicarte con Peluditos Pet Chile.";
    setTimeout(cargarClientes, 2500);
  } catch (error) {
    estado.textContent = error.message || "No se pudo conectar con el servicio.";
  } finally {
    botonEnviar.disabled = false;
    botonRevisar.disabled = false;
    botonLimpiar.disabled = false;
  }
});
}

if (formSeguimiento) {
  formSeguimiento.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    if (!formSeguimiento.reportValidity()) return;

    const casoId = inputCasoId.value.trim();
    const compraRealizada = selectCompra.value === "true";
    const clienteObservaciones = inputObsSeguimiento.value.trim();

    botonSeguimiento.disabled = true;
    estadoSeguimiento.textContent = "Guardando el resultado del seguimiento...";

    try {
      const respuesta = await fetch(`/api/consultas/${encodeURIComponent(casoId)}/seguimiento`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          compraRealizada,
          clienteObservaciones,
        }),
      });
      const resultado = await respuesta.json();
      if (!respuesta.ok) throw new Error(resultado.mensaje || "No se pudo avanzar el seguimiento.");

      estadoSeguimiento.textContent = "El resultado del seguimiento fue registrado correctamente. El estado del cliente se actualizará en el sistema.";
      setTimeout(cargarClientes, 2500);
    } catch (error) {
      estadoSeguimiento.textContent = error.message || "No se pudo conectar con el servicio.";
    } finally {
      botonSeguimiento.disabled = false;
    }
  });
}

async function cargarClientes() {
  if (!contenedorClientes) return;
  contenedorClientes.textContent = "Cargando clientes registrados...";
  try {
    const respuesta = await fetch("/api/clientes");
    if (!respuesta.ok) throw new Error("No se pudo obtener la lista de clientes.");
    const clientes = await respuesta.json();

    contenedorClientes.replaceChildren();
    if (!Array.isArray(clientes) || clientes.length === 0) {
      const vacio = document.createElement("p");
      vacio.textContent = "Aún no hay clientes registrados en la base de datos.";
      contenedorClientes.append(vacio);
      return;
    }

    const tabla = document.createElement("table");
    tabla.className = "tabla-clientes";
    const thead = document.createElement("thead");
    thead.innerHTML = `
      <tr>
        <th>ID</th>
        <th>Cliente</th>
        <th>Canal</th>
        <th>Mascota</th>
        <th>Estado Compra</th>
        <th>Observaciones</th>
      </tr>
    `;
    tabla.append(thead);

    const tbody = document.createElement("tbody");
    for (const c of clientes) {
      const tr = document.createElement("tr");
      const celdas = [
        c.CLIENTE_ID ?? "",
        `${c.CLIENTE_NOMBRE ?? ""} (${c.CLIENTE_TELEFONO ?? ""})`,
        c.CLIENTE_CANAL_CONTACTO ?? "",
        `${c.MASCOTA_NOMBRE ?? "-"} (${c.MASCOTA_ESPECIE ?? ""})`,
        c.CLIENTE_ESTADO_COMPRA ?? "",
        c.CLIENTE_OBSERVACIONES ?? "",
      ];
      for (const texto of celdas) {
        const td = document.createElement("td");
        td.textContent = String(texto);
        tr.append(td);
      }
      tbody.append(tr);
    }
    tabla.append(tbody);
    contenedorClientes.append(tabla);
  } catch (error) {
    contenedorClientes.textContent = error.message || "Error al cargar clientes.";
  }
}

if (botonCargarClientes) {
  botonCargarClientes.addEventListener("click", cargarClientes);
  cargarClientes();
}

if (botonRevisar) {
  botonRevisar.disabled = false;
}
