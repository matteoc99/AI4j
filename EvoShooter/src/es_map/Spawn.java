package es_map;

import field_library.field.Field;
import field_library.math.Circle;
import field_library.math.Position;
import values.Values;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Spawn extends Circle {

    private Field parent;

    public Spawn(Field parent, Position center) {
        super(center, Values.SPAWN_RADIUS);
        this.parent = parent;
    }

    public Spawn(Field parent) {
        super(Values.SPAWN_RADIUS);
        this.parent = parent;
    }

    public void paint(Graphics g) {
        g.fillOval((int)center.getX()-radius/2, (int)center.getY()-radius/2, radius, radius);
    }
}