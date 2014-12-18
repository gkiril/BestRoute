package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TollBadInputTypeException extends Exception{
    public TollBadInputTypeException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Entrez des nombres non - négatifs réels pour le péage",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}