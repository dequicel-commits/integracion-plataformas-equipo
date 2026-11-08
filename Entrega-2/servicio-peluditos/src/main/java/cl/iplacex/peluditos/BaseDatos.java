package cl.iplacex.peluditos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseDatos {

    private static final String DB_URL = "jdbc:sqlite:peluditos.db";

    public static Connection conectar() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void inicializar() {
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {

            // 1. TABLA_CLIENTE
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS TABLA_CLIENTE (
                    CLIENTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    CLIENTE_NOMBRE TEXT NOT NULL,
                    CLIENTE_TELEFONO TEXT NOT NULL,
                    CLIENTE_CORREO TEXT,
                    CLIENTE_CANAL_CONTACTO TEXT NOT NULL,
                    CLIENTE_FECHA_CONSULTA TEXT NOT NULL,
                    CLIENTE_CONSULTA_REALIZADA TEXT NOT NULL,
                    CLIENTE_ESTADO_COMPRA TEXT NOT NULL,
                    CLIENTE_AUTORIZA INTEGER NOT NULL,
                    CLIENTE_OBSERVACIONES TEXT
                );
            """);

            // 2. TABLA_TIPO_MASCOTA
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS TABLA_TIPO_MASCOTA (
                    MASCOTA_TIPO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    MASCOTA_ESPECIE TEXT NOT NULL,
                    MASCOTA_TAMANO TEXT NOT NULL
                );
            """);

            // 3. TABLA_MASCOTA
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS TABLA_MASCOTA (
                    MASCOTA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    CLIENTE_ID INTEGER NOT NULL,
                    MASCOTA_TIPO_ID INTEGER NOT NULL,
                    MASCOTA_NOMBRE TEXT NOT NULL,
                    MASCOTA_CUMPLEANO TEXT,
                    MASCOTA_OBSERVACIONES TEXT,
                    FOREIGN KEY (CLIENTE_ID) REFERENCES TABLA_CLIENTE(CLIENTE_ID),
                    FOREIGN KEY (MASCOTA_TIPO_ID) REFERENCES TABLA_TIPO_MASCOTA(MASCOTA_TIPO_ID)
                );
            """);

            // 4. TABLA_PRODUCTO
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS TABLA_PRODUCTO (
                    PRODUCTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    PRODUCTO_NOMBRE TEXT NOT NULL,
                    PRODUCTO_TIPO TEXT NOT NULL,
                    MASCOTA_TIPO_ID INTEGER NOT NULL,
                    FOREIGN KEY (MASCOTA_TIPO_ID) REFERENCES TABLA_TIPO_MASCOTA(MASCOTA_TIPO_ID)
                );
            """);

            cargarDatosBaseSiVacio(conn);
            System.out.println("Base de datos SQLite inicializada correctamente (peluditos.db).");

        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la base de datos SQLite", e);
        }
    }

    private static void cargarDatosBaseSiVacio(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM TABLA_TIPO_MASCOTA")) {
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO TABLA_TIPO_MASCOTA (MASCOTA_ESPECIE, MASCOTA_TAMANO) VALUES ('Perro', 'Pequeno');");
                stmt.execute("INSERT INTO TABLA_TIPO_MASCOTA (MASCOTA_ESPECIE, MASCOTA_TAMANO) VALUES ('Perro', 'Mediano');");
                stmt.execute("INSERT INTO TABLA_TIPO_MASCOTA (MASCOTA_ESPECIE, MASCOTA_TAMANO) VALUES ('Perro', 'Grande');");
                stmt.execute("INSERT INTO TABLA_TIPO_MASCOTA (MASCOTA_ESPECIE, MASCOTA_TAMANO) VALUES ('Gato', 'Mediano');");

                stmt.execute("INSERT INTO TABLA_PRODUCTO (PRODUCTO_NOMBRE, PRODUCTO_TIPO, MASCOTA_TIPO_ID) VALUES ('Torta Cumpleanos Canina Pollo y Zanahoria', 'Torta', 2);");
                stmt.execute("INSERT INTO TABLA_PRODUCTO (PRODUCTO_NOMBRE, PRODUCTO_TIPO, MASCOTA_TIPO_ID) VALUES ('Galletas Naturales de Higado', 'Galletas', 1);");
                stmt.execute("INSERT INTO TABLA_PRODUCTO (PRODUCTO_NOMBRE, PRODUCTO_TIPO, MASCOTA_TIPO_ID) VALUES ('Muffin Felino de Salmon', 'Snack', 4);");
            }
        }
    }

    public static Map<String, Object> guardarClienteYMascota(RegistroClienteRequest req) throws SQLException {
        try (Connection conn = conectar()) {
            conn.setAutoCommit(false);
            try {
                int clienteId;
                String sqlCliente = """
                    INSERT INTO TABLA_CLIENTE (
                        CLIENTE_NOMBRE, CLIENTE_TELEFONO, CLIENTE_CORREO,
                        CLIENTE_CANAL_CONTACTO, CLIENTE_FECHA_CONSULTA,
                        CLIENTE_CONSULTA_REALIZADA, CLIENTE_ESTADO_COMPRA,
                        CLIENTE_AUTORIZA, CLIENTE_OBSERVACIONES
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

                try (PreparedStatement ps = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, valorPorDefecto(req.clienteNombre(), "Cliente Peluditos"));
                    ps.setString(2, valorPorDefecto(req.clienteTelefono(), "+56912345678"));
                    ps.setString(3, req.clienteCorreo());
                    ps.setString(4, valorPorDefecto(req.clienteCanalContacto(), "WhatsApp"));
                    ps.setString(5, valorPorDefecto(req.clienteFechaConsulta(), "2026-11-06"));
                    ps.setString(6, valorPorDefecto(req.clienteConsultaRealizada(), "Consulta por torta de cumpleanos para mascota"));
                    ps.setString(7, valorPorDefecto(req.clienteEstadoCompra(), "En seguimiento"));
                    ps.setInt(8, Boolean.TRUE.equals(req.clienteAutoriza()) ? 1 : 0);
                    ps.setString(9, req.clienteObservaciones());
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        clienteId = keys.getInt(1);
                    }
                }

                Integer mascotaId = null;
                if (req.mascotaNombre() != null && !req.mascotaNombre().isBlank()) {
                    String sqlMascota = """
                        INSERT INTO TABLA_MASCOTA (
                            CLIENTE_ID, MASCOTA_TIPO_ID, MASCOTA_NOMBRE,
                            MASCOTA_CUMPLEANO, MASCOTA_OBSERVACIONES
                        ) VALUES (?, ?, ?, ?, ?)
                    """;
                    int tipoId = (req.mascotaTipoId() != null && req.mascotaTipoId() > 0) ? req.mascotaTipoId() : 1;
                    try (PreparedStatement psMascota = conn.prepareStatement(sqlMascota, Statement.RETURN_GENERATED_KEYS)) {
                        psMascota.setInt(1, clienteId);
                        psMascota.setInt(2, tipoId);
                        psMascota.setString(3, req.mascotaNombre());
                        psMascota.setString(4, req.mascotaCumpleano());
                        psMascota.setString(5, req.mascotaObservaciones());
                        psMascota.executeUpdate();

                        try (ResultSet keys = psMascota.getGeneratedKeys()) {
                            if (keys.next()) {
                                mascotaId = keys.getInt(1);
                            }
                        }
                    }
                }

                conn.commit();

                Map<String, Object> resultado = new LinkedHashMap<>();
                resultado.put("estado", "OK");
                resultado.put("mensaje", "Potencial cliente guardado correctamente en la base de datos");
                resultado.put("clienteId", clienteId);
                resultado.put("mascotaId", mascotaId);
                return resultado;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public static List<Map<String, Object>> listarClientes() throws SQLException {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = """
            SELECT c.CLIENTE_ID, c.CLIENTE_NOMBRE, c.CLIENTE_TELEFONO, c.CLIENTE_CORREO,
                   c.CLIENTE_CANAL_CONTACTO, c.CLIENTE_FECHA_CONSULTA, c.CLIENTE_CONSULTA_REALIZADA,
                   c.CLIENTE_ESTADO_COMPRA, c.CLIENTE_AUTORIZA, c.CLIENTE_OBSERVACIONES,
                   m.MASCOTA_ID, m.MASCOTA_NOMBRE, m.MASCOTA_CUMPLEANO, m.MASCOTA_OBSERVACIONES,
                   t.MASCOTA_ESPECIE, t.MASCOTA_TAMANO
            FROM TABLA_CLIENTE c
            LEFT JOIN TABLA_MASCOTA m ON c.CLIENTE_ID = m.CLIENTE_ID
            LEFT JOIN TABLA_TIPO_MASCOTA t ON m.MASCOTA_TIPO_ID = t.MASCOTA_TIPO_ID
            ORDER BY c.CLIENTE_ID DESC
        """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("CLIENTE_ID", rs.getInt("CLIENTE_ID"));
                fila.put("CLIENTE_NOMBRE", rs.getString("CLIENTE_NOMBRE"));
                fila.put("CLIENTE_TELEFONO", rs.getString("CLIENTE_TELEFONO"));
                fila.put("CLIENTE_CORREO", rs.getString("CLIENTE_CORREO"));
                fila.put("CLIENTE_CANAL_CONTACTO", rs.getString("CLIENTE_CANAL_CONTACTO"));
                fila.put("CLIENTE_FECHA_CONSULTA", rs.getString("CLIENTE_FECHA_CONSULTA"));
                fila.put("CLIENTE_CONSULTA_REALIZADA", rs.getString("CLIENTE_CONSULTA_REALIZADA"));
                fila.put("CLIENTE_ESTADO_COMPRA", rs.getString("CLIENTE_ESTADO_COMPRA"));
                fila.put("CLIENTE_AUTORIZA", rs.getInt("CLIENTE_AUTORIZA") == 1);
                fila.put("CLIENTE_OBSERVACIONES", rs.getString("CLIENTE_OBSERVACIONES"));
                fila.put("MASCOTA_ID", rs.getObject("MASCOTA_ID"));
                fila.put("MASCOTA_NOMBRE", rs.getString("MASCOTA_NOMBRE"));
                fila.put("MASCOTA_CUMPLEANO", rs.getString("MASCOTA_CUMPLEANO"));
                fila.put("MASCOTA_ESPECIE", rs.getString("MASCOTA_ESPECIE"));
                fila.put("MASCOTA_TAMANO", rs.getString("MASCOTA_TAMANO"));
                lista.add(fila);
            }
        }
        return lista;
    }

    public static List<Map<String, Object>> listarProductos() throws SQLException {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = """
            SELECT p.PRODUCTO_ID, p.PRODUCTO_NOMBRE, p.PRODUCTO_TIPO,
                   t.MASCOTA_TIPO_ID, t.MASCOTA_ESPECIE, t.MASCOTA_TAMANO
            FROM TABLA_PRODUCTO p
            JOIN TABLA_TIPO_MASCOTA t ON p.MASCOTA_TIPO_ID = t.MASCOTA_TIPO_ID
            ORDER BY p.PRODUCTO_ID ASC
        """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("PRODUCTO_ID", rs.getInt("PRODUCTO_ID"));
                fila.put("PRODUCTO_NOMBRE", rs.getString("PRODUCTO_NOMBRE"));
                fila.put("PRODUCTO_TIPO", rs.getString("PRODUCTO_TIPO"));
                fila.put("MASCOTA_TIPO_ID", rs.getInt("MASCOTA_TIPO_ID"));
                fila.put("MASCOTA_ESPECIE", rs.getString("MASCOTA_ESPECIE"));
                fila.put("MASCOTA_TAMANO", rs.getString("MASCOTA_TAMANO"));
                lista.add(fila);
            }
        }
        return lista;
    }

    private static String valorPorDefecto(String valor, String porDefecto) {
        return (valor != null && !valor.isBlank()) ? valor : porDefecto;
    }

    public record RegistroClienteRequest(
            String clienteNombre,
            String clienteTelefono,
            String clienteCorreo,
            String clienteCanalContacto,
            String clienteFechaConsulta,
            String clienteConsultaRealizada,
            String clienteEstadoCompra,
            Boolean clienteAutoriza,
            String clienteObservaciones,
            String mascotaNombre,
            Integer mascotaTipoId,
            String mascotaCumpleano,
            String mascotaObservaciones
    ) {}
}
