package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gashteovski
 */
public class ABadInputTypeException extends Exception{
    public ABadInputTypeException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Entrez une numéro réel pour A",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}