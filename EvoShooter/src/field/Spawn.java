package field;

import math.Circle;
import math.Function;
import math.LineFunction;
import math.Position;
import values.Values;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
class Spawn extends Circle {

    private Field parent;

    Spawn(Field parent, Position center) {
        super(center, Values.spawnRadius);
        this.parent = parent;
    }

    Spawn(Field parent) {
        super(Values.spawnRadius);
        this.parent = parent;
    }

    void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval((int)center.getX()-radius/2, (int)center.getY()-radius/2, radius, radius);
    }
}