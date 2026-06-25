package persistencia;

import modelo.LineaTransmision;
import modelo.Subestacion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorCSV {

    // ── LÍNEAS ────────────────────────────────────────────────────
    private static final String HEADER_LINEAS =
        "id,nombre,operador,estado,fechaPuestaOperacion,tipoUso," +
        "subestacionOrigen,subestacionDestino,voltajeNominalKV," +
        "corrienteNominalA,capacidadMW,longitudKm,municipio,departamento,subarea";

    public void guardarLineas(List<LineaTransmision> lineas, String ruta) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(ruta), "UTF-8"))) {
            pw.println(HEADER_LINEAS);
            for (LineaTransmision l : lineas) {
                pw.println(String.join(",",
                    String.valueOf(l.getId()),
                    escapar(l.getNombre()),
                    escapar(l.getOperador()),
                    escapar(l.getEstado()),
                    escapar(l.getFechaPuestaOperacion()),
                    escapar(l.getTipoUso()),
                    escapar(l.getSubestacionOrigen()),
                    escapar(l.getSubestacionDestino()),
                    String.valueOf(l.getVoltajeNominalKV()),
                    String.valueOf(l.getCorrienteNominalA()),
                    String.valueOf(l.getCapacidadMW()),
                    String.valueOf(l.getLongitudKm()),
                    escapar(l.getMunicipio()),
                    escapar(l.getDepartamento()),
                    escapar(l.getSubarea())
                ));
            }
        }
    }

    public List<LineaTransmision> cargarLineas(String ruta) throws IOException {
        List<LineaTransmision> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), "UTF-8"))) {
            String lineaTexto;
            String[] header = null;
            int idAuto = 1;
            while ((lineaTexto = br.readLine()) != null) {
                if (lineaTexto.trim().isEmpty()) continue;
                if (header == null) {
                    header = parsearCSV(lineaTexto);
                    continue;
                }
                String[] p = parsearCSV(lineaTexto);
                try {
                    // Detectar si el CSV tiene columna id o no
                    boolean tieneId = header[0].equalsIgnoreCase("id");
                    int offset = tieneId ? 1 : 0;
                    if (p.length < 14 + offset) continue;

                    LineaTransmision l = new LineaTransmision();
                    l.setId(tieneId ? parsearInt(p[0], idAuto) : idAuto);
                    idAuto++;
                    l.setNombre(p[offset]);
                    l.setOperador(p[offset + 1]);
                    l.setEstado(p[offset + 2]);
                    l.setFechaPuestaOperacion(p[offset + 3]);
                    l.setTipoUso(p[offset + 4]);
                    l.setSubestacionOrigen(p[offset + 5]);
                    l.setSubestacionDestino(p[offset + 6]);
                    l.setVoltajeNominalKV(parsearDouble(p[offset + 7]));
                    l.setCorrienteNominalA(parsearDouble(p[offset + 8]));
                    l.setCapacidadMW(parsearDouble(p[offset + 9]));
                    l.setLongitudKm(parsearDouble(p[offset + 10]));
                    l.setMunicipio(p[offset + 11]);
                    l.setDepartamento(p[offset + 12]);
                    l.setSubarea(p.length > offset + 13 ? p[offset + 13] : "SubArea Santander");
                    lista.add(l);
                } catch (Exception e) {
                    System.err.println("Error al parsear línea: " + lineaTexto + " -> " + e.getMessage());
                }
            }
        }
        return lista;
    }

    // ── SUBESTACIONES ─────────────────────────────────────────────
    private static final String HEADER_SUBS =
        "id,nombre,municipio,departamento,latitud,longitud";

    public void guardarSubestaciones(List<Subestacion> subs, String ruta) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(ruta), "UTF-8"))) {
            pw.println(HEADER_SUBS);
            for (Subestacion s : subs) {
                pw.println(String.join(",",
                    String.valueOf(s.getId()),
                    escapar(s.getNombre()),
                    escapar(s.getMunicipio()),
                    escapar(s.getDepartamento()),
                    String.valueOf(s.getLatitud()),
                    String.valueOf(s.getLongitud())
                ));
            }
        }
    }

    public List<Subestacion> cargarSubestaciones(String ruta) throws IOException {
        List<Subestacion> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), "UTF-8"))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.trim().isEmpty()) continue;
                String[] p = parsearCSV(linea);
                if (p.length < 6) continue;
                try {
                    Subestacion s = new Subestacion(
                        parsearInt(p[0], 0), p[1], p[2], p[3],
                        parsearDouble(p[4]), parsearDouble(p[5])
                    );
                    lista.add(s);
                } catch (Exception e) {
                    System.err.println("Error al parsear subestación: " + linea);
                }
            }
        }
        return lista;
    }
    
    
    // ── Generadores ─────────────────────────────────────────────
    private static final String HEADER_GENS =
        "id,nombre,municipio,departamento,latitud,longitud,tipoGeneracion";
 
    public void guardarGeneradores(List<modelo.Generador> gens, String ruta) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(ruta), "UTF-8"))) {
            pw.println(HEADER_GENS);
            for (modelo.Generador g : gens) {
                pw.println(String.join(",",
                    String.valueOf(g.getId()),
                    escapar(g.getNombre()),
                    escapar(g.getMunicipio()),
                    escapar(g.getDepartamento()),
                    String.valueOf(g.getLatitud()),
                    String.valueOf(g.getLongitud()),
                    escapar(g.getTipoGeneracion())
                ));
            }
        }
    }
 
    public List<modelo.Generador> cargarGeneradores(String ruta) throws IOException {
        List<modelo.Generador> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), "UTF-8"))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.trim().isEmpty()) continue;
                String[] p = parsearCSV(linea);
                if (p.length < 6) continue;
                try {
                    modelo.Generador g = new modelo.Generador(
                        parsearInt(p[0], 0), p[1], p[4], p[5],
                        parsearDouble(p[6]), parsearDouble(p[7]),
                        p.length > 8 ? p[8] : ""
                    );
                    lista.add(g);
                } catch (Exception e) {
                    System.err.println("Error al parsear generador: " + linea);
                }
            }
        }
        return lista;
    }
    // ── Utilidades ────────────────────────────────────────────────
    private String escapar(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }

    private String[] parsearCSV(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean enComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (c == '"') {
                if (enComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    sb.append('"'); i++;
                } else {
                    enComillas = !enComillas;
                }
            } else if (c == ',' && !enComillas) {
                campos.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        campos.add(sb.toString().trim());
        return campos.toArray(new String[0]);
    }

    private double parsearDouble(String s) {
        try { return Double.parseDouble(s.trim()); }
        catch (Exception e) { return 0.0; }
    }

    private int parsearInt(String s, int defecto) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return defecto; }
    }
}
