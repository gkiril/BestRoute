package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gashteovski
 */
public class AlphaBadInputTypeException extends Exception{
    public AlphaBadInputTypeException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Entrez nombre réel positif pour Alpha",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}