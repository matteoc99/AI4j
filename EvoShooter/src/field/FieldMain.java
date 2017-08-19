package field;

import math.Function;

import javax.swing.*;
import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 25.07.2017
 */
public class FieldMain {
    public static void main(String[] args) {
        Field field = new Field();

        JFrame frame = new JFrame("Map");
        frame.pack();
        Insets insets = frame.getInsets();
        frame.setBounds(600, 100, field.width + insets.left + insets.right, field.height + insets.top + insets.bottom);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel label = new JLabel(new ImageIcon(field.createMapImage()));
        label.setBounds(0, 0, field.width, field.height);
        frame.getContentPane().add(label);

        frame.setVisible(true);
    }
}
