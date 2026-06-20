package controlador;

import modelo.LineaTransmision;
import modelo.Subestacion;
import modelo.Generador;
import modelo.SistemaElectrico;
import persistencia.GestorCSV;
import java.io.IOException;
import java.util.List;

public class Controlador {
    private SistemaElectrico sistema;
    private GestorCSV gestorCSV;

    public Controlador() {
        this.sistema = new SistemaElectrico();
        this.gestorCSV = new GestorCSV();
    }

    // ── Inicialización ────────────────────────────────────────────
    public void cargarDatosIniciales(String rutaLineas, String rutaSubestaciones, String rutaGeneradores) {
        try {
            List<LineaTransmision> lineas = gestorCSV.cargarLineas(rutaLineas);
            sistema.setLineas(lineas);
        } catch (IOException e) {
            System.err.println("No se encontró archivo de líneas: " + e.getMessage());
        }
        try {
            List<Subestacion> subs = gestorCSV.cargarSubestaciones(rutaSubestaciones);
            sistema.setSubestaciones(subs);
        } catch (IOException e) {
            System.err.println("No se encontró archivo de subestaciones: " + e.getMessage());
        }
        try {
        List<Generador> gens = gestorCSV.cargarGeneradores(rutaGeneradores);
        sistema.setGeneradores(gens);
    } catch (IOException e) {
        System.err.println("No se encontró archivo de generadores: " + e.getMessage());
    }      
    }

    // ── Líneas CRUD-S ─────────────────────────────────────────────
    public String agregarLinea(String nombre, String operador, String estado,
                               String fecha, String tipoUso, String subOrigen,
                               String subDestino, double voltaje, double corriente,
                               double longitud, String municipio,
                               String departamento, String subarea) {
        // Validaciones
        if (nombre.trim().isEmpty()) return "ERROR: El nombre no puede estar vacío.";
        if (operador.trim().isEmpty()) return "ERROR: El operador no puede estar vacío.";
        if (voltaje <= 0) return "ERROR: El voltaje debe ser mayor a 0.";
        if (corriente <= 0) return "ERROR: La corriente debe ser mayor a 0.";
        if (longitud <= 0) return "ERROR: La longitud debe ser mayor a 0.";

        LineaTransmision l = new LineaTransmision(0, nombre.trim(), operador.trim(),
            estado.trim(), fecha.trim(), tipoUso.trim(), subOrigen.trim(),
            subDestino.trim(), voltaje, corriente, longitud,
            municipio.trim(), departamento.trim(), subarea.trim());
        sistema.agregarLinea(l);
        return "OK";
    }

    public String actualizarLinea(int id, String nombre, String operador, String estado,
                                  String fecha, String tipoUso, String subOrigen,
                                  String subDestino, double voltaje, double corriente,
                                  double longitud, String municipio,
                                  String departamento, String subarea) {
        if (nombre.trim().isEmpty()) return "ERROR: El nombre no puede estar vacío.";
        if (voltaje <= 0) return "ERROR: El voltaje debe ser mayor a 0.";
        if (corriente <= 0) return "ERROR: La corriente debe ser mayor a 0.";
        if (longitud <= 0) return "ERROR: La longitud debe ser mayor a 0.";

        LineaTransmision l = new LineaTransmision(id, nombre.trim(), operador.trim(),
            estado.trim(), fecha.trim(), tipoUso.trim(), subOrigen.trim(),
            subDestino.trim(), voltaje, corriente, longitud,
            municipio.trim(), departamento.trim(), subarea.trim());
        boolean ok = sistema.actualizarLinea(l);
        return ok ? "OK" : "ERROR: Línea no encontrada.";
    }

    public String eliminarLinea(int id) {
        boolean ok = sistema.eliminarLinea(id);
        return ok ? "OK" : "ERROR: Línea no encontrada.";
    }

    public List<LineaTransmision> obtenerTodasLineas() {
        return sistema.getLineas();
    }

    public List<LineaTransmision> buscarLineas(String texto) {
        if (texto == null || texto.trim().isEmpty()) return sistema.getLineas();
        return sistema.buscarLineas(texto.trim());
    }

    public List<LineaTransmision> filtrarPorVoltaje(double voltaje) {
        if (voltaje <= 0) return sistema.getLineas();
        return sistema.filtrarPorVoltaje(voltaje);
    }

    public List<LineaTransmision> filtrarPorOperador(String operador) {
        if (operador == null || operador.isEmpty() || operador.equals("Todos")) return sistema.getLineas();
        return sistema.filtrarPorOperador(operador);
    }

    // ── Persistencia ──────────────────────────────────────────────
    public String guardarLineas(String ruta) {
        try {
            gestorCSV.guardarLineas(sistema.getLineas(), ruta);
            return "OK: Guardado exitosamente en " + ruta;
        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    public String cargarLineas(String ruta) {
        try {
            List<LineaTransmision> lineas = gestorCSV.cargarLineas(ruta);
            sistema.setLineas(lineas);
            return "OK: " + lineas.size() + " líneas cargadas.";
        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    // ── Subestaciones ─────────────────────────────────────────────
    public List<Subestacion> obtenerSubestaciones() {
        return sistema.getSubestaciones();
    }
     // ── Generadores ─────────────────────────────────────────────
    public List<Generador> obtenerGeneradores() {
    return sistema.getGeneradores();
}

    // ── Estadísticas ──────────────────────────────────────────────
    public double getCapacidadTotal() { return sistema.calcularCapacidadTotal(); }
    public double getLongitudTotal() { return sistema.calcularLongitudTotal(); }
    public List<String> getOperadores() { return sistema.getOperadoresUnicos(); }
    public List<Double> getVoltajes() { return sistema.getVoltajesUnicos(); }
    public int getTotalLineas() { return sistema.getLineas().size(); }
}
