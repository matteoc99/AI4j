package field;

import math.Function;
import math.FunctionData;
import math.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * @author Maximilian Estfelller
 * @since 25.07.2017
 */
public class FieldMain {
    public static void main(String[] args) {
        Field field = new Field(10);

        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            field = new Field(10);
        }
        System.out.println(System.nanoTime()-start);

        JFrame frame = new JFrame("Map");
        frame.setLayout(null);
        frame.pack();
        Insets insets = frame.getInsets();
        frame.setBounds(600, 100, field.getWidth() + insets.left + insets.right, field.getHeight() + insets.top + insets.bottom);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel label = new JLabel(new ImageIcon(field.createMapImage()));
        label.setBounds(0, 0, field.getWidth(), field.getHeight());
        frame.getContentPane().add(label);

        frame.setVisible(true);

        for (FieldSection[] fieldSections : field.getSections())
            for (FieldSection fieldSection : fieldSections)
                ;//System.out.println(fieldSection);
    }
}
