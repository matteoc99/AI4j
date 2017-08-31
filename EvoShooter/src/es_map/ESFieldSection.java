package es_map;

import field_library.field.Field;
import field_library.field.FieldSection;
import field_library.math.Position;

/**
 * @author Maximilian Estfeller
 * @since 31.08.2017
 */
public class ESFieldSection extends FieldSection {

    /**
     * A Section is reachable, when there is a path from the flag to this Section.
     * A Section which isn't free to move on can never be reachable.
     */
    boolean canReachTheFlag = false;

    /**
     * Amount of steps it takes to get from this Section to the Section the flag is located in
     */
    int stepsToFlag = Integer.MAX_VALUE;

    public ESFieldSection(Field parent, int x, int y, Position topLeft, Position botRight) {
        super(parent, x, y, topLeft, botRight);
    }

    /**
     * Tells this Section, that it can reach the flag.
     *
     * Then also tells every neighbour, which is free to move on, that it reaches the flag too
     */
    void reachesFlag(int steps) {
        if (!canReachTheFlag) { // avoids infinite loops
            canReachTheFlag = true;
            stepsToFlag = steps;
            for (FieldSection fieldSection : getNeighbours())
            if (fieldSection.isFreeToMoveOn()) ((ESFieldSection) fieldSection).reachesFlag(steps+1);
        }
    }
}
