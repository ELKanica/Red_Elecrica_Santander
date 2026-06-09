package modelo;

public class LineaTransmision {
    private int id;
    private String nombre;
    private String operador;
    private String estado;
    private String fechaPuestaOperacion;
    private String tipoUso;
    private String subestacionOrigen;
    private String subestacionDestino;
    private double voltajeNominalKV;
    private double corrienteNominalA;
    private double capacidadMW;
    private double longitudKm;
    private String municipio;
    private String departamento;
    private String subarea;

    public LineaTransmision() {}

    public LineaTransmision(int id, String nombre, String operador, String estado,
                            String fechaPuestaOperacion, String tipoUso,
                            String subestacionOrigen, String subestacionDestino,
                            double voltajeNominalKV, double corrienteNominalA,
                            double longitudKm, String municipio,
                            String departamento, String subarea) {
        this.id = id;
        this.nombre = nombre;
        this.operador = operador;
        this.estado = estado;
        this.fechaPuestaOperacion = fechaPuestaOperacion;
        this.tipoUso = tipoUso;
        this.subestacionOrigen = subestacionOrigen;
        this.subestacionDestino = subestacionDestino;
        this.voltajeNominalKV = voltajeNominalKV;
        this.corrienteNominalA = corrienteNominalA;
        this.longitudKm = longitudKm;
        this.municipio = municipio;
        this.departamento = departamento;
        this.subarea = subarea;
        this.capacidadMW = calcularCapacidadMW(voltajeNominalKV, corrienteNominalA, 0.95);
    }

    /**
     * Calcula la capacidad en MW usando: MW = √3 × V_kV × I_A / 1000 × pf
     */
    public double calcularCapacidadMW(double voltajeKV, double corrienteA, double pf) {
        if (voltajeKV <= 0 || corrienteA <= 0) return 0.0;
        return Math.round(Math.sqrt(3) * voltajeKV * corrienteA / 1000.0 * pf * 100.0) / 100.0;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaPuestaOperacion() { return fechaPuestaOperacion; }
    public void setFechaPuestaOperacion(String f) { this.fechaPuestaOperacion = f; }

    public String getTipoUso() { return tipoUso; }
    public void setTipoUso(String tipoUso) { this.tipoUso = tipoUso; }

    public String getSubestacionOrigen() { return subestacionOrigen; }
    public void setSubestacionOrigen(String s) { this.subestacionOrigen = s; }

    public String getSubestacionDestino() { return subestacionDestino; }
    public void setSubestacionDestino(String s) { this.subestacionDestino = s; }

    public double getVoltajeNominalKV() { return voltajeNominalKV; }
    public void setVoltajeNominalKV(double v) {
        this.voltajeNominalKV = v;
        this.capacidadMW = calcularCapacidadMW(v, corrienteNominalA, 0.95);
    }

    public double getCorrienteNominalA() { return corrienteNominalA; }
    public void setCorrienteNominalA(double c) {
        this.corrienteNominalA = c;
        this.capacidadMW = calcularCapacidadMW(voltajeNominalKV, c, 0.95);
    }

    public double getCapacidadMW() { return capacidadMW; }
    public void setCapacidadMW(double mw) { this.capacidadMW = mw; }

    public double getLongitudKm() { return longitudKm; }
    public void setLongitudKm(double l) { this.longitudKm = l; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String m) { this.municipio = m; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String d) { this.departamento = d; }

    public String getSubarea() { return subarea; }
    public void setSubarea(String s) { this.subarea = s; }

    @Override
    public String toString() {
        return nombre + " | " + voltajeNominalKV + " kV | " + longitudKm + " km";
    }
}
