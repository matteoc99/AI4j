package botGUI.bot;

import botGUI.World;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mcosi on 14/06/2017.
 */
public class Body extends JComponent {

    public Body(){
        setSize(World.CHUNK_SIZE/2,World.CHUNK_SIZE/2);
        repaint();
    }
    @Override
    public void paintComponents(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(0,0, World.CHUNK_SIZE/2,World.CHUNK_SIZE/2);
        super.paintComponents(g);
    }
}
