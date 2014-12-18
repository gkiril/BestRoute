package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gashteovski
 */
public class CoefBadInputTypeException extends Exception{
    public CoefBadInputTypeException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Entrez un numéro réel pour le coefficient",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}