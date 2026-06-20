package vista;

import modelo.LineaTransmision;
import modelo.Subestacion;
import modelo.Generador;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class PanelMapa extends JPanel {

    private JXMapViewer mapViewer;
    private List<Subestacion> subestaciones;
    private List<Generador> generadores;
    private List<LineaTransmision> lineas;

    // Colores por nivel de voltaje
    private static final Map<Double, Color> COLORES_VOLTAJE = new HashMap<>();
    static {
        COLORES_VOLTAJE.put(115.0, new Color(34, 139, 34));   // verde
        COLORES_VOLTAJE.put(230.0, new Color(0, 120, 215));   // azul
        COLORES_VOLTAJE.put(500.0, new Color(200, 30, 30));   // rojo
        COLORES_VOLTAJE.put(66.0,  new Color(255, 165, 0));   // naranja
        COLORES_VOLTAJE.put(57.5,  new Color(148, 0, 211));   // morado
    }
    private static final Map<String, Color> COLORES_GENERADOR = new HashMap<>();
    static {
        COLORES_GENERADOR.put("Térmica",       new Color(220, 50,  50));  // rojo
        COLORES_GENERADOR.put("Hidroeléctrica",new Color(30,  144, 255)); // azul
        COLORES_GENERADOR.put("Solar",         new Color(255, 200, 0));   // amarillo
        COLORES_GENERADOR.put("PCH",           new Color(0,   180, 100)); // verde
}
    public PanelMapa() {
        setLayout(new BorderLayout());
        subestaciones = new ArrayList<>();
        lineas = new ArrayList<>();
        inicializarMapa();
    }

    private void inicializarMapa() {
        mapViewer = new JXMapViewer();

        // Usar OpenStreetMap
TileFactoryInfo info = new TileFactoryInfo(
    1, 17, 17, 256, true, true,
    "https://tile.openstreetmap.org",
    "x", "y", "z") {
    public String getTileUrl(int x, int y, int zoom) {
        int z = 17 - zoom;
        return "https://tile.openstreetmap.org/" + z + "/" + x + "/" + y + ".png";
    }
};
DefaultTileFactory tileFactory = new DefaultTileFactory(info);
tileFactory.setUserAgent("Mozilla/5.0 SistemaElectricoSantander/1.0");
        tileFactory.setThreadPoolSize(4);
        mapViewer.setTileFactory(tileFactory);

        // Centrar en Santander, Colombia
        GeoPosition santan = new GeoPosition(7.0, -73.2);
        mapViewer.setAddressLocation(santan);
        mapViewer.setZoom(7);

        // Zoom con scroll
        mapViewer.addMouseWheelListener(e -> {
            int zoom = mapViewer.getZoom() + e.getWheelRotation();
            if (zoom >= 1 && zoom <= 17) mapViewer.setZoom(zoom);
        });

        // Pan con arrastre
        MouseAdapter panAdapter = new MouseAdapter() {
            Point lastPos;
            public void mousePressed(MouseEvent e) { lastPos = e.getPoint(); }
            public void mouseDragged(MouseEvent e) {
                if (lastPos != null) {
                    int dx = lastPos.x - e.getX();
                    int dy = lastPos.y - e.getY();
                    Rectangle vp = mapViewer.getViewportBounds();
                    mapViewer.setCenter(new Point(vp.x + vp.width/2 + dx, vp.y + vp.height/2 + dy));
                    lastPos = e.getPoint();
                    mapViewer.repaint();
                }
            }
        };
        mapViewer.addMouseListener(panAdapter);
        mapViewer.addMouseMotionListener(panAdapter);

        add(mapViewer, BorderLayout.CENTER);
        add(crearLeyenda(), BorderLayout.SOUTH);
    }

    public void actualizarDatos(List<Subestacion> subs, List<Generador> generadores, List<LineaTransmision> lineas) {
        this.subestaciones = subs;
        this.generadores= generadores;
        this.lineas = lineas;
        refrescarPintores();
    }

    private void refrescarPintores() {
        List<Painter<JXMapViewer>> painters = new ArrayList<>();

        // Pintor de líneas
        painters.add((g, map, w, h) -> {
            g = (Graphics2D) g.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(2.0f));

            for (LineaTransmision l : lineas) {
                double lat1, lon1, lat2, lon2;

Subestacion orig = buscarSub(l.getSubestacionOrigen());
if (orig != null) { lat1 = orig.getLatitud(); lon1 = orig.getLongitud(); }
else {
    Generador gOrig = buscarGen(l.getSubestacionOrigen());
    if (gOrig == null) continue;
    lat1 = gOrig.getLatitud(); lon1 = gOrig.getLongitud();
}

Subestacion dest = buscarSub(l.getSubestacionDestino());
if (dest != null) { lat2 = dest.getLatitud(); lon2 = dest.getLongitud(); }
else {
    Generador gDest = buscarGen(l.getSubestacionDestino());
    if (gDest == null) continue;
    lat2 = gDest.getLatitud(); lon2 = gDest.getLongitud();
}

Point2D p1 = map.convertGeoPositionToPoint(new GeoPosition(lat1, lon1));
Point2D p2 = map.convertGeoPositionToPoint(new GeoPosition(lat2, lon2));

                Color color = COLORES_VOLTAJE.getOrDefault(l.getVoltajeNominalKV(), Color.GRAY);
                g.setColor(color);
                g.draw(new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
            }
            g.dispose();
        });

        // Pintor de subestaciones
        painters.add((g, map, w, h) -> {
            g = (Graphics2D) g.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Subestacion s : subestaciones) {
                Point2D p = map.convertGeoPositionToPoint(new GeoPosition(s.getLatitud(), s.getLongitud()));
                int r = 6;
                double px = p.getX(), py = p.getY();
                g.setColor(new Color(80, 0, 120));
                g.fill(new Ellipse2D.Double(px - r, py - r, r * 2, r * 2));
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(1.5f));
                g.draw(new Ellipse2D.Double(px - r, py - r, r * 2, r * 2));

                // Etiqueta
                g.setColor(Color.BLACK);
                g.setFont(new Font("SansSerif", Font.PLAIN, 9));
                g.drawString(s.getNombre(), (float)(px + r + 2), (float)(py + 4));
            }
            g.dispose();
        });
        
        // Painter de generadores 
        painters.add((g, map, w, h) -> {
            g = (Graphics2D) g.create();
            for (Generador gen : generadores) {
                Point2D p = map.convertGeoPositionToPoint(
                new GeoPosition(gen.getLatitud(), gen.getLongitud()));
                int r = 6;
                double px = p.getX(), py = p.getY();
                Color colorGen = COLORES_GENERADOR.getOrDefault(
                gen.getTipoGeneracion(), new Color(255, 140, 0));
                g.setColor(colorGen);
                g.fillRect((int)(px - r), (int)(py - r), r * 2, r * 2);
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(1.5f));
                g.drawRect((int)(px - r), (int)(py - r), r * 2, r * 2);
                g.setColor(Color.BLACK);
                g.setFont(new Font("SansSerif", Font.PLAIN, 9));
                g.drawString(gen.getNombre(), (float)(px + r + 2), (float)(py + 4));
            }
            g.dispose();
            
        });

        mapViewer.setOverlayPainter(new CompoundPainter<>(painters));
        mapViewer.repaint();
    }

    private Subestacion buscarSub(String nombre) {
        if (nombre == null || nombre.isEmpty()) return null;
        return subestaciones.stream()
            .filter(s -> s.getNombre().equalsIgnoreCase(nombre.trim()))
            .findFirst().orElse(null);
    }
    private Generador buscarGen(String nombre) {
    if (nombre == null || nombre.isEmpty()) return null;
    return generadores.stream()
        .filter(g -> g.getNombre().equalsIgnoreCase(nombre.trim()))
        .findFirst().orElse(null);
}

    private JPanel crearLeyenda() {
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        leyenda.setBackground(new Color(245, 245, 245));
        leyenda.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        agregarItemLeyenda(leyenda, "115 kV", COLORES_VOLTAJE.get(115.0));
        agregarItemLeyenda(leyenda, "230 kV", COLORES_VOLTAJE.get(230.0));
        agregarItemLeyenda(leyenda, "500 kV", COLORES_VOLTAJE.get(500.0));
        agregarItemLeyenda(leyenda, "Subestación", new Color(80, 0, 120));
        agregarItemLeyenda(leyenda, "Generador", new Color(255, 140, 0));
        agregarItemLeyenda(leyenda, "Térmica", COLORES_GENERADOR.get("Térmica"));
        agregarItemLeyenda(leyenda, "Hidroeléctrica", COLORES_GENERADOR.get("Hidroeléctrica"));
        agregarItemLeyenda(leyenda, "Solar", COLORES_GENERADOR.get("Solar"));
        agregarItemLeyenda(leyenda, "PCH", COLORES_GENERADOR.get("PCH"));
        return leyenda;
    }

    private void agregarItemLeyenda(JPanel panel, String texto, Color color) {
        JLabel icono = new JLabel("  ") {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(color);
                g.fillRect(0, 4, 20, 10);
            }
        };
        icono.setPreferredSize(new Dimension(20, 18));
        panel.add(icono);
        panel.add(new JLabel(texto));
    }
}