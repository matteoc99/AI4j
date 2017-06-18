package botGUI.bot;

import botGUI.World;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mcosi on 14/06/2017.
 */
public class Sensor extends JComponent {

    public int width=10;
    public int height=width;

    Color sensorColor = Color.blue;

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

    public void setSensorColor(Color sensorColor) {
        this.sensorColor = sensorColor;
        repaint();
    }
}
