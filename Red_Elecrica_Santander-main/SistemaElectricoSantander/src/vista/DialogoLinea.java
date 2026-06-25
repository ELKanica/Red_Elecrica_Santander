package vista;

import modelo.LineaTransmision;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogoLinea extends JDialog {

    private JTextField txtNombre, txtFecha, txtLongitud;
    private JTextField txtOrigen, txtDestino, txtMunicipio, txtDepartamento;
    private JComboBox<String> cmbOperador, cmbEstado, cmbTipoUso, cmbVoltaje;
    private JTextField txtCorriente;
    private JLabel lblCapacidad;
    private boolean confirmado = false;

    private static final String[] OPERADORES = {
        "ELECTRIFICADORA DE SANTANDER S.A. E.S.P.",
        "ISA INTERCOLOMBIA S.A. E.S.P.",
        "CELSIA COLOMBIA S.A. E.S.P.",
        "PCH SAN BARTOLOME S.A.S. E.S.P.",
        "ACUEDUCTO METROPOLITANO DE BUCARAMANGA S A E S P"
    };

    public DialogoLinea(Frame parent, String titulo, LineaTransmision linea) {
        super(parent, titulo, true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 500));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);

        if (linea != null) cargarDatos(linea);

        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.3;
        panel.add(new JLabel("Nombre *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.7;
        txtNombre = new JTextField();
        panel.add(txtNombre, gbc);

        // Fila 1: Operador
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Operador *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        cmbOperador = new JComboBox<>(OPERADORES);
        panel.add(cmbOperador, gbc);

        // Fila 2: Estado | Tipo Uso
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        cmbEstado = new JComboBox<>(new String[]{"Operación", "Fuera de servicio", "Construcción"});
        panel.add(cmbEstado, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Tipo de uso:"), gbc);
        gbc.gridx = 3;
        cmbTipoUso = new JComboBox<>(new String[]{"Uso STR", "Uso STN", "Generación", "Carga", "Del OR al STN"});
        panel.add(cmbTipoUso, gbc);

        // Fila 3: Subestación Origen | Destino
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Sub. Origen:"), gbc);
        gbc.gridx = 1;
        txtOrigen = new JTextField();
        panel.add(txtOrigen, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Sub. Destino:"), gbc);
        gbc.gridx = 3;
        txtDestino = new JTextField();
        panel.add(txtDestino, gbc);

        // Fila 4: Voltaje | Corriente
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Voltaje (kV) *:"), gbc);
        gbc.gridx = 1;
        cmbVoltaje = new JComboBox<>(new String[]{"115.0", "230.0", "500.0", "66.0", "57.5"});
        cmbVoltaje.addActionListener(e -> actualizarCapacidad());
        panel.add(cmbVoltaje, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Corriente (A) *:"), gbc);
        gbc.gridx = 3;
        txtCorriente = new JTextField();
        txtCorriente.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarCapacidad(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarCapacidad(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarCapacidad(); }
        });
        panel.add(txtCorriente, gbc);

        // Fila 5: Capacidad calculada (solo lectura)
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Capacidad (MW):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        lblCapacidad = new JLabel("0.00  (calculado automáticamente)");
        lblCapacidad.setFont(lblCapacidad.getFont().deriveFont(Font.BOLD));
        lblCapacidad.setForeground(new Color(0, 100, 0));
        panel.add(lblCapacidad, gbc);

        // Fila 6: Longitud | Fecha
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        panel.add(new JLabel("Longitud (km) *:"), gbc);
        gbc.gridx = 1;
        txtLongitud = new JTextField();
        panel.add(txtLongitud, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Fecha operación:"), gbc);
        gbc.gridx = 3;
        txtFecha = new JTextField("01/01/2000");
        panel.add(txtFecha, gbc);

        // Fila 7: Municipio | Departamento
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Municipio:"), gbc);
        gbc.gridx = 1;
        txtMunicipio = new JTextField();
        panel.add(txtMunicipio, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Departamento:"), gbc);
        gbc.gridx = 3;
        txtDepartamento = new JTextField("SANTANDER");
        panel.add(txtDepartamento, gbc);

        return panel;
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(0, 0, 10, 15));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(0, 120, 215));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> {
            if (validar()) { confirmado = true; dispose(); }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnCancelar);
        panel.add(btnGuardar);
        return panel;
    }

    private void actualizarCapacidad() {
        try {
            double voltaje = Double.parseDouble(cmbVoltaje.getSelectedItem().toString());
            double corriente = Double.parseDouble(txtCorriente.getText().trim());
            double mw = Math.sqrt(3) * voltaje * corriente / 1000.0 * 0.95;
            lblCapacidad.setText(String.format("%.2f MW  (calculado automáticamente)", mw));
        } catch (NumberFormatException e) {
            lblCapacidad.setText("— (ingrese voltaje y corriente válidos)");
        }
    }

    private boolean validar() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio."); return false;
        }
        try { Double.parseDouble(txtCorriente.getText().trim()); }
        catch (Exception e) { mostrarError("La corriente debe ser un número válido."); return false; }
        try {
            double l = Double.parseDouble(txtLongitud.getText().trim());
            if (l <= 0) { mostrarError("La longitud debe ser mayor a 0."); return false; }
        } catch (Exception e) { mostrarError("La longitud debe ser un número válido."); return false; }
        return true;
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error de validación", JOptionPane.ERROR_MESSAGE);
    }

    private void cargarDatos(LineaTransmision l) {
        txtNombre.setText(l.getNombre());
        cmbOperador.setSelectedItem(l.getOperador());
        cmbEstado.setSelectedItem(l.getEstado());
        cmbTipoUso.setSelectedItem(l.getTipoUso());
        txtOrigen.setText(l.getSubestacionOrigen());
        txtDestino.setText(l.getSubestacionDestino());
        cmbVoltaje.setSelectedItem(String.valueOf(l.getVoltajeNominalKV()));
        txtCorriente.setText(String.valueOf(l.getCorrienteNominalA()));
        txtLongitud.setText(String.valueOf(l.getLongitudKm()));
        txtFecha.setText(l.getFechaPuestaOperacion());
        txtMunicipio.setText(l.getMunicipio());
        txtDepartamento.setText(l.getDepartamento());
    }

    public boolean isConfirmado() { return confirmado; }

    public LineaTransmision getLinea() {
        LineaTransmision l = new LineaTransmision();
        l.setNombre(txtNombre.getText().trim());
        l.setOperador(cmbOperador.getSelectedItem().toString());
        l.setEstado(cmbEstado.getSelectedItem().toString());
        l.setFechaPuestaOperacion(txtFecha.getText().trim());
        l.setTipoUso(cmbTipoUso.getSelectedItem().toString());
        l.setSubestacionOrigen(txtOrigen.getText().trim());
        l.setSubestacionDestino(txtDestino.getText().trim());
        l.setVoltajeNominalKV(Double.parseDouble(cmbVoltaje.getSelectedItem().toString()));
        l.setCorrienteNominalA(Double.parseDouble(txtCorriente.getText().trim()));
        l.setLongitudKm(Double.parseDouble(txtLongitud.getText().trim()));
        l.setCapacidadMW(l.calcularCapacidadMW(l.getVoltajeNominalKV(), l.getCorrienteNominalA(), 0.95));
        l.setMunicipio(txtMunicipio.getText().trim());
        l.setDepartamento(txtDepartamento.getText().trim());
        l.setSubarea("SubArea Santander");
        return l;
    }
}
