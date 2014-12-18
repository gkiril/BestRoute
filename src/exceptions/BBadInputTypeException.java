package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BBadInputTypeException extends Exception{
    public BBadInputTypeException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Entrez un numéro réel pour B",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }

    public BBadInputTypeException(String msg) {
        super(msg);
    }
}