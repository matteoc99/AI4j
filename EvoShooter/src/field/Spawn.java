package field;

import math.Circle;
import math.Function;
import math.LineFunction;
import math.Position;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
class Spawn extends Circle {

    private Field parent;

    Spawn(Field parent, Position center, int radius) {
        super(center, radius);
        this.parent = parent;
    }

    Spawn(Field parent, Position center) {
        super(center);
        this.parent = parent;
    }

    Spawn(Field parent) {
        this.parent = parent;
    }

    void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval((int)center.getX(), (int)center.getY(), radius, radius);
    }
}