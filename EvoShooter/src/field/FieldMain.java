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


        System.out.println(Function.calcSlopeByDeg(0));
        System.out.println(Function.calcSlopeByDeg(90));
        System.out.println(Function.calcSlopeByDeg(180));
        System.out.println(Function.calcSlopeByDeg(270));
        System.out.println(Function.calcSlopeByDeg(360));
        System.out.println(Function.calcSlopeByDeg(1));
        System.out.println(Function.calcSlopeByDeg(359));
        System.out.println(Function.calcSlopeByDeg(91));
        System.out.println(Function.calcSlopeByDeg(89));
        System.out.println(Function.calcSlopeByDeg(269));
        System.out.println(Function.calcSlopeByDeg(271));
        System.out.println(Function.calcSlopeByDeg(179));
        System.out.println(Function.calcSlopeByDeg(181));
    }
}
