package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gashteovski
 */
public class MnZeroException extends Exception{
    public MnZeroException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "m et n ne doit pas etre zéros",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}