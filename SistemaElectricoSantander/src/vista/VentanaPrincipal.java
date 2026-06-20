package vista;

import controlador.Controlador;
import modelo.LineaTransmision;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final Controlador controlador;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltroVoltaje, cmbFiltroOperador;
    private JLabel lblCapacidadTotal, lblLongitudTotal, lblTotalLineas;
    private PanelMapa panelMapa;

    // Columnas de la tabla
    private static final String[] COLUMNAS = {
        "ID", "Nombre", "Operador", "Voltaje (kV)", "Corriente (A)",
        "Capacidad (MW)", "Longitud (km)", "Origen", "Destino", "Municipio", "Estado"
    };

    public VentanaPrincipal(Controlador controlador) {
        this.controlador = controlador;
        configurarVentana();
        construirUI();
        cargarDatosIniciales();
    }

    private void configurarVentana() {
        setTitle("Sistema Eléctrico Nacional — SubÁrea Santander");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        add(crearBarraHerramientas(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelIzquierdo(), crearPanelMapa());
        split.setDividerLocation(700);
        split.setResizeWeight(0.6);
        add(split, BorderLayout.CENTER);
        add(crearBarraEstado(), BorderLayout.SOUTH);

        setJMenuBar(crearMenuBar());
    }

    // ── Barra de herramientas ─────────────────────────────────────
    private JToolBar crearBarraHerramientas() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setBackground(new Color(30, 60, 114));
        tb.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel titulo = new JLabel("⚡ Gestión de Líneas de Transmisión — Santander");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        tb.add(titulo);
        tb.addSeparator(new Dimension(30, 0));

        tb.add(botonToolbar("➕ Nueva", new Color(0, 153, 76), e -> accionNueva()));
        tb.addSeparator(new Dimension(5, 0));
        tb.add(botonToolbar("✏️ Editar", new Color(0, 100, 200), e -> accionEditar()));
        tb.addSeparator(new Dimension(5, 0));
        tb.add(botonToolbar("🗑 Eliminar", new Color(200, 30, 30), e -> accionEliminar()));
        tb.addSeparator(new Dimension(20, 0));
        tb.add(botonToolbar("💾 Guardar CSV", new Color(100, 60, 140), e -> accionGuardar()));
        tb.addSeparator(new Dimension(5, 0));
        tb.add(botonToolbar("📂 Abrir CSV", new Color(100, 60, 140), e -> accionAbrir()));

        return tb;
    }

    private JButton botonToolbar(String texto, Color bg, ActionListener al) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(al);
        return b;
    }

    // ── Panel izquierdo (filtros + tabla) ─────────────────────────
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBorder(new EmptyBorder(8, 8, 8, 4));

        panel.add(crearPanelFiltros(), BorderLayout.NORTH);
        panel.add(crearTabla(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros y Búsqueda"));

        txtBuscar = new JTextField(18);
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar por nombre, municipio...");
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { actualizarTabla(); }
        });

        cmbFiltroVoltaje = new JComboBox<>(new String[]{"Todos los voltajes", "115.0", "230.0", "500.0", "66.0", "57.5"});
        cmbFiltroVoltaje.addActionListener(e -> actualizarTabla());

        cmbFiltroOperador = new JComboBox<>();
        cmbFiltroOperador.addItem("Todos los operadores");
        cmbFiltroOperador.addActionListener(e -> actualizarTabla());

        panel.add(new JLabel("🔍 Buscar:"));
        panel.add(txtBuscar);
        panel.add(new JLabel("Voltaje:"));
        panel.add(cmbFiltroVoltaje);
        panel.add(new JLabel("Operador:"));
        panel.add(cmbFiltroOperador);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            cmbFiltroVoltaje.setSelectedIndex(0);
            cmbFiltroOperador.setSelectedIndex(0);
            actualizarTabla();
        });
        panel.add(btnLimpiar);

        return panel;
    }

    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int c) {
                return (c == 0 || c == 3 || c == 4 || c == 5 || c == 6) ? Double.class : String.class;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(30, 60, 114));
        tabla.getTableHeader().setForeground(Color.BLUE);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabla.setRowSorter(new TableRowSorter<>(modeloTabla));

        // Doble clic para editar
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) accionEditar();
            }
        });

        // Ajustar anchos de columnas
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getColumnModel().getColumn(3).setMaxWidth(80);
        tabla.getColumnModel().getColumn(4).setMaxWidth(85);
        tabla.getColumnModel().getColumn(5).setMaxWidth(90);
        tabla.getColumnModel().getColumn(6).setMaxWidth(80);

        return new JScrollPane(tabla);
    }

    // ── Panel mapa ────────────────────────────────────────────────
    private JPanel crearPanelMapa() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Mapa Geográfico — Santander"));

        panelMapa = new PanelMapa();
        panel.add(panelMapa, BorderLayout.CENTER);
        return panel;
    }

    // ── Barra de estado ───────────────────────────────────────────
    private JPanel crearBarraEstado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 4));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        lblTotalLineas = new JLabel("Líneas: 0");
        lblCapacidadTotal = new JLabel("Capacidad total: 0.00 MW");
        lblLongitudTotal = new JLabel("Longitud total: 0.00 km");

        panel.add(new JLabel("📊"));
        panel.add(lblTotalLineas);
        panel.add(new JSeparator(JSeparator.VERTICAL));
        panel.add(lblCapacidadTotal);
        panel.add(new JSeparator(JSeparator.VERTICAL));
        panel.add(lblLongitudTotal);

        return panel;
    }

    // ── Menú ──────────────────────────────────────────────────────
    private JMenuBar crearMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu mArchivo = new JMenu("Archivo");
        mArchivo.add(menuItem("Abrir CSV...", e -> accionAbrir()));
        mArchivo.add(menuItem("Guardar CSV...", e -> accionGuardar()));
        mArchivo.addSeparator();
        mArchivo.add(menuItem("Salir", e -> System.exit(0)));

        JMenu mLineas = new JMenu("Líneas");
        mLineas.add(menuItem("Nueva línea", e -> accionNueva()));
        mLineas.add(menuItem("Editar seleccionada", e -> accionEditar()));
        mLineas.add(menuItem("Eliminar seleccionada", e -> accionEliminar()));

        JMenu mVista = new JMenu("Herramientas");
        mVista.add(menuItem("Estadísticas del sistema", e -> mostrarEstadisticas()));

        mb.add(mArchivo);
        mb.add(mLineas);
        mb.add(mVista);
        return mb;
    }

    private JMenuItem menuItem(String texto, ActionListener al) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(al);
        return item;
    }

    // ── Acciones CRUD-S ───────────────────────────────────────────
    private void accionNueva() {
        DialogoLinea dlg = new DialogoLinea(this, "Nueva Línea de Transmisión", null);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) {
            LineaTransmision l = dlg.getLinea();
            String res = controlador.agregarLinea(l.getNombre(), l.getOperador(), l.getEstado(),
                l.getFechaPuestaOperacion(), l.getTipoUso(), l.getSubestacionOrigen(),
                l.getSubestacionDestino(), l.getVoltajeNominalKV(), l.getCorrienteNominalA(),
                l.getLongitudKm(), l.getMunicipio(), l.getDepartamento(), l.getSubarea());
            if (res.equals("OK")) { actualizarTabla(); actualizarMapa(); }
            else JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEditar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una línea."); return; }
        int id = (int) Double.parseDouble(modeloTabla.getValueAt(tabla.convertRowIndexToModel(fila), 0).toString());
        LineaTransmision actual = controlador.obtenerTodasLineas().stream()
            .filter(l -> l.getId() == id).findFirst().orElse(null);
        if (actual == null) return;

        DialogoLinea dlg = new DialogoLinea(this, "Editar Línea de Transmisión", actual);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) {
            LineaTransmision l = dlg.getLinea();
            l.setId(id);
            String res = controlador.actualizarLinea(id, l.getNombre(), l.getOperador(), l.getEstado(),
                l.getFechaPuestaOperacion(), l.getTipoUso(), l.getSubestacionOrigen(),
                l.getSubestacionDestino(), l.getVoltajeNominalKV(), l.getCorrienteNominalA(),
                l.getLongitudKm(), l.getMunicipio(), l.getDepartamento(), l.getSubarea());
            if (res.equals("OK")) { actualizarTabla(); actualizarMapa(); }
            else JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una línea."); return; }
        int id = (int) Double.parseDouble(modeloTabla.getValueAt(tabla.convertRowIndexToModel(fila), 0).toString());
        int conf = JOptionPane.showConfirmDialog(this,
            "¿Desea eliminar la línea seleccionada?", "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            controlador.eliminarLinea(id);
            actualizarTabla();
            actualizarMapa();
        }
    }

    private void accionGuardar() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("lineas_santander.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String res = controlador.guardarLineas(fc.getSelectedFile().getAbsolutePath());
            JOptionPane.showMessageDialog(this, res.startsWith("OK") ?
                "Archivo guardado correctamente." : res,
                res.startsWith("OK") ? "Éxito" : "Error",
                res.startsWith("OK") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionAbrir() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String res = controlador.cargarLineas(fc.getSelectedFile().getAbsolutePath());
            actualizarTabla();
            actualizarMapa();
            JOptionPane.showMessageDialog(this, res.startsWith("OK") ?
                res.replace("OK: ", "") : res,
                res.startsWith("OK") ? "Carga exitosa" : "Error",
                res.startsWith("OK") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Actualización UI ──────────────────────────────────────────
    private void actualizarTabla() {
        String busqueda = txtBuscar.getText().trim();
        String voltStr = (String) cmbFiltroVoltaje.getSelectedItem();
        String oper = (String) cmbFiltroOperador.getSelectedItem();

        List<LineaTransmision> lista = busqueda.isEmpty()
            ? controlador.obtenerTodasLineas()
            : controlador.buscarLineas(busqueda);

        if (!"Todos los voltajes".equals(voltStr)) {
            try {
                double v = Double.parseDouble(voltStr);
                lista = lista.stream().filter(l -> l.getVoltajeNominalKV() == v)
                    .collect(java.util.stream.Collectors.toList());
            } catch (NumberFormatException ignored) {}
        }

        if (oper != null && !"Todos los operadores".equals(oper)) {
            final String opFinal = oper;
            lista = lista.stream().filter(l -> l.getOperador().equalsIgnoreCase(opFinal))
                .collect(java.util.stream.Collectors.toList());
        }

        modeloTabla.setRowCount(0);
        for (LineaTransmision l : lista) {
            modeloTabla.addRow(new Object[]{
                l.getId(), l.getNombre(), l.getOperador(),
                l.getVoltajeNominalKV(), l.getCorrienteNominalA(),
                l.getCapacidadMW(), l.getLongitudKm(),
                l.getSubestacionOrigen(), l.getSubestacionDestino(),
                l.getMunicipio(), l.getEstado()
            });
        }

        actualizarEstadisticas();
    }

    private void actualizarMapa() {
        panelMapa.actualizarDatos(
            controlador.obtenerSubestaciones(),
            controlador.obtenerGeneradores(),
            controlador.obtenerTodasLineas()
        );
    }

    private void actualizarEstadisticas() {
        lblTotalLineas.setText("Líneas: " + controlador.getTotalLineas());
        lblCapacidadTotal.setText(String.format("Capacidad total: %.2f MW", controlador.getCapacidadTotal()));
        lblLongitudTotal.setText(String.format("Longitud total: %.2f km", controlador.getLongitudTotal()));
    }

    private void mostrarEstadisticas() {
        String msg = String.format(
            "📊 Estadísticas del Sistema — SubÁrea Santander\n\n" +
            "Total de líneas: %d\n" +
            "Capacidad total instalada: %.2f MW\n" +
            "Longitud total de red: %.2f km\n\n" +
            "Operadores activos: %d\n" +
            "Niveles de voltaje: %s",
            controlador.getTotalLineas(),
            controlador.getCapacidadTotal(),
            controlador.getLongitudTotal(),
            controlador.getOperadores().size(),
            controlador.getVoltajes().toString()
        );
        JOptionPane.showMessageDialog(this, msg, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosIniciales() {
        // Cargar CSVs desde carpeta data/
        String base = System.getProperty("user.dir") + File.separator + "data" + File.separator;
        controlador.cargarDatosIniciales(base + "lineas.csv", base + "subestaciones.csv", base + "generadores.csv");

        // Poblar combo de operadores
        controlador.getOperadores().forEach(o -> cmbFiltroOperador.addItem(o));

        actualizarTabla();
        actualizarMapa();
    }
}
