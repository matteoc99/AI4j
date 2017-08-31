package es_map;

import javax.swing.*;
import java.awt.*;

/**
 * @author Maximilian Estfeller
 * @since 31.08.2017
 */
public class ESFieldTest {

    public static void main(String[] args) {
        makeFrame(ESFieldFactory.createRandomESField(500, 500, 10));
        for (int i = 0; i < 100; i++) {
            ESFieldFactory.createRandomESField(500,500, 10);
        }
    }

    private static void makeFrame(ESField field) {
        JFrame frame = new JFrame("Map");
        frame.setLayout(null);
        frame.pack();
        Insets insets = frame.getInsets();
        frame.setBounds(600, 100, field.width + insets.left + insets.right, field.height + insets.top + insets.bottom);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getContentPane().add(field);

        frame.setVisible(true);
    }
}
