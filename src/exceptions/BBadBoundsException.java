package exceptions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BBadBoundsException extends Exception{
    public BBadBoundsException() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                "Vous devez entrer un numéro réel dans l'intervalle [0, 10] pour B",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
    }
}