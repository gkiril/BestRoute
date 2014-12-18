package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gashteovski
 */
public class ABadBoundsException extends Exception{
    public ABadBoundsException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Vous devez entrer des nombres réels dans l'intervalle (0, 1] pour A",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}