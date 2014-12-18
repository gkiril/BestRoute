package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DistanceBadInputTypeException extends Exception{
    public DistanceBadInputTypeException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Entrez un numéro réel pour la distance",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}