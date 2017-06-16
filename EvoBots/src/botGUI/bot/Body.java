package botGUI.bot;

import botGUI.World;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mcosi on 14/06/2017.
 */
public class Body extends JComponent {
    public int width=10;
    public int height=width;

    public Body(){
        setSize(width,height);
        repaint();
    }
    @Override
    public void paint(Graphics g) {
        setSize(width,height);
        g.setColor(Color.RED);
        g.fillOval(0,0, width,height);
        super.paint(g);
    }
}
