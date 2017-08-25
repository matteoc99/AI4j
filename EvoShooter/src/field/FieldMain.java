package field;

import math.Position;

import javax.swing.*;
import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 25.07.2017
 */
public class FieldMain {
    public static void main(String[] args) {

        Field field = new Field(10);

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

    private static void speedTest() {
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            Field field = new Field(10);
        }
        System.out.println(System.nanoTime()-start);
    }

    private static void calculateDistanceTest() {
        Field field = new Field(0);
        System.out.println(field.calculateDistance(new Position(250, 250), 0));
        System.out.println(field.calculateDistance(new Position(250, 250), 90));
        System.out.println(field.calculateDistance(new Position(250, 250), 180));
        System.out.println(field.calculateDistance(new Position(250, 250), 270));
        System.out.println(field.calculateDistance(new Position(250, 250), 45));
        System.out.println(field.calculateDistance(new Position(250, 250), 135));
        System.out.println(field.calculateDistance(new Position(250, 250), 225));
        System.out.println(field.calculateDistance(new Position(250, 250), 315));
        System.out.println(field.calculateDistance(new Position(250, 250), 26));
        System.out.println(field.calculateDistance(new Position(250, 250), 142));
        System.out.println(field.calculateDistance(new Position(250, 250), 241));
        System.out.println(field.calculateDistance(new Position(250, 250), 299));
    }
}
