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
        makeFrame(createField());
    }

    private static Field createField() {
        try {
            try {
                return new Field(10);
            } catch (BadFieldException e) {
                if (e.isBug()) throw e;
                else return createField();
            }
        } catch (StackOverflowError error) {
            return null;
        }
    }
    private static void createFields(int i) {
        int success = 0;
        for (int c = 0; c < i; c++) {
            try {
                new Field(10);
                success++;
            } catch (BadFieldException e) {
                System.out.println(c+": "+e.getMessage());
                if (e.isBug()) throw e;
            }
        }
        System.out.println(success);
        System.out.println("SuccessRate: " + success*100/i + "%");
    }
    private static void makeFrame(Field field) {
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
    }
    private static void printFieldSections(Field field) {
        for (FieldSection[] fieldSections : field.getSections())
            for (FieldSection fieldSection : fieldSections)
                System.out.println(fieldSection);
    }
    private static void speedTest(int i) {
        // 28.07 pre bestSpawnCalculation           0,21ms/field    (2129868212/10000)  95& success
        // 28.07 after bestSpawnCalculation         0,28ms/field    (2827453834/10000)  80% success
        long start = System.nanoTime();
        for (; i > 0; i--) {
            try {
                new Field(10);
            } catch (BadFieldException ignored) {}
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
