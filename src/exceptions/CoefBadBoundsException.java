package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gashteovski
 */
public class CoefBadBoundsException extends Exception{
    public CoefBadBoundsException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Vous devez entrer un numéro réel dans l'intervalle (0, 2) pour coef",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}