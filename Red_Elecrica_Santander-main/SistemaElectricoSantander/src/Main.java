import controlador.Controlador;
import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
/**
 * Autores:
 * Andres Ramos-028
 * Manuel Amell-001
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controlador ctrl = new Controlador();
            VentanaPrincipal ventana = new VentanaPrincipal(ctrl);
            ventana.setVisible(true);
        });
    }
}
