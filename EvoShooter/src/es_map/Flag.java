package es_map;

import field_library.field.Field;
import field_library.math.Circle;
import field_library.math.Position;
import values.Values;

import java.awt.*;

/**
 * @author Maximilian Estfeller
 * @since 25.08.2017
 */
public class Flag extends Circle {

    private final Field parent;

    public Flag(Field parent, Position center) {
        super(center, Values.FLAG_RADIUS);
        this.parent = parent;
    }

    public Flag(Field parent) {
        this(parent, new Position(0, 0));
    }

    public void paint(Graphics g) {
        g.fillOval((int)center.getX()-radius/2, (int)center.getY()-radius/2, radius, radius);
    }
}
