package field;

import math.Circle;
import math.Position;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Spawn extends Circle{

    public Spawn(Position center, int radius) {
        super(center, radius);
    }

    public Spawn(Position center) {
        super(center);
    }

    public Spawn() {
    }

    void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval((int)center.getX(), (int)center.getY(), radius, radius);
    }
}