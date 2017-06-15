package botGUI.bot;

import botGUI.World;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mcosi on 14/06/2017.
 */
public class Sensor extends JComponent {

    public int width=15;
    public int height=width;

    public Sensor() {
        setSize(width,height);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        setSize(width,height);
        g.setColor(Color.BLUE);
        g.fillOval(0,0,width,height);
        super.paint(g);
    }
}
