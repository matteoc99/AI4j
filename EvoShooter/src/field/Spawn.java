package field;

import math.Circle;
import math.Position;
import values.Values;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Spawn extends Circle {

    private Field parent;

    Spawn(Field parent, Position center) {
        super(center, Values.SPAWN_RADIUS);
        this.parent = parent;
    }

    Spawn(Field parent) {
        super(Values.SPAWN_RADIUS);
        this.parent = parent;
    }

    void paint(Graphics g) {
        g.fillOval((int)center.getX()-radius/2, (int)center.getY()-radius/2, radius, radius);
    }
}