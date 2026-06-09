package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaElectrico {
    private List<LineaTransmision> lineas;
    private List<Subestacion> subestaciones;
    private int nextId = 1;

    public SistemaElectrico() {
        this.lineas = new ArrayList<>();
        this.subestaciones = new ArrayList<>();
    }

    // ── CRUD Líneas ──────────────────────────────────────────────
    public void agregarLinea(LineaTransmision linea) {
        linea.setId(nextId++);
        lineas.add(linea);
    }

    public boolean eliminarLinea(int id) {
        return lineas.removeIf(l -> l.getId() == id);
    }

    public boolean actualizarLinea(LineaTransmision actualizada) {
        for (int i = 0; i < lineas.size(); i++) {
            if (lineas.get(i).getId() == actualizada.getId()) {
                lineas.set(i, actualizada);
                return true;
            }
        }
        return false;
    }

    public LineaTransmision buscarLineaPorId(int id) {
        return lineas.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
    }

    public List<LineaTransmision> buscarLineas(String texto) {
        String t = texto.toLowerCase();
        return lineas.stream()
            .filter(l -> l.getNombre().toLowerCase().contains(t)
                      || l.getOperador().toLowerCase().contains(t)
                      || l.getMunicipio().toLowerCase().contains(t)
                      || l.getSubestacionOrigen().toLowerCase().contains(t)
                      || l.getSubestacionDestino().toLowerCase().contains(t))
            .collect(Collectors.toList());
    }

    public List<LineaTransmision> filtrarPorVoltaje(double voltaje) {
        return lineas.stream()
            .filter(l -> l.getVoltajeNominalKV() == voltaje)
            .collect(Collectors.toList());
    }

    public List<LineaTransmision> filtrarPorOperador(String operador) {
        return lineas.stream()
            .filter(l -> l.getOperador().equalsIgnoreCase(operador))
            .collect(Collectors.toList());
    }

    public List<LineaTransmision> getLineas() { return lineas; }

    public void setLineas(List<LineaTransmision> lineas) {
        this.lineas = lineas;
        // Recalcular nextId
        nextId = lineas.stream().mapToInt(LineaTransmision::getId).max().orElse(0) + 1;
    }

    // ── Subestaciones ─────────────────────────────────────────────
    public List<Subestacion> getSubestaciones() { return subestaciones; }

    public void setSubestaciones(List<Subestacion> subestaciones) {
        this.subestaciones = subestaciones;
    }

    public Subestacion buscarSubestacionPorNombre(String nombre) {
        return subestaciones.stream()
            .filter(s -> s.getNombre().equalsIgnoreCase(nombre))
            .findFirst().orElse(null);
    }

    // ── Estadísticas ──────────────────────────────────────────────
    public double calcularCapacidadTotal() {
        return lineas.stream().mapToDouble(LineaTransmision::getCapacidadMW).sum();
    }

    public double calcularLongitudTotal() {
        return lineas.stream().mapToDouble(LineaTransmision::getLongitudKm).sum();
    }

    public List<String> getOperadoresUnicos() {
        return lineas.stream()
            .map(LineaTransmision::getOperador)
            .distinct().sorted()
            .collect(Collectors.toList());
    }

    public List<Double> getVoltajesUnicos() {
        return lineas.stream()
            .map(LineaTransmision::getVoltajeNominalKV)
            .distinct().sorted()
            .collect(Collectors.toList());
    }
}
