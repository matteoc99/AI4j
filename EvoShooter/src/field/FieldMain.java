package field;

import javax.swing.*;

/**
 * @author Maximilian Estfelller
 * @since 25.07.2017
 */
public class FieldMain {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Map");
        frame.setBounds(600,100,500,500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Field field = new Field();
        JLabel label = new JLabel(new ImageIcon(field.createMapImage()));
        label.setBounds(0,0,500,500);
        frame.getContentPane().add(label);

        frame.setVisible(true);
    }
}
