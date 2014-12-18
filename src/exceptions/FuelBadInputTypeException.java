package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FuelBadInputTypeException extends Exception{
    public FuelBadInputTypeException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Entrez nombre réel positif pour le carburant",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}