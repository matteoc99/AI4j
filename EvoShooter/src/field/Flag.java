package field;

import math.Circle;
import math.Position;
import values.Values;

/**
 * @author Maximilian Estfeller
 * @since 25.08.2017
 */
class Flag extends Circle {

    private final Field parent;

    Flag(Field parent, Position center) {
        super(center, Values.flagRadius);
        this.parent = parent;
    }

    Flag(Field parent) {
        this(parent, new Position(0, 0));
    }
}
