package modelo;

public class Subestacion {
    private int id;
    private String nombre;
    private String municipio;
    private String departamento;
    private double latitud;
    private double longitud;

    public Subestacion() {}

    public Subestacion(int id, String nombre, String municipio,
                       String departamento, double latitud, double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.municipio = municipio;
        this.departamento = departamento;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    @Override
    public String toString() { return nombre + " (" + municipio + ")"; }
}
