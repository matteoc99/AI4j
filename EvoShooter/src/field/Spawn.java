package field;

import math.Circle;
import math.Position;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Spawn extends Circle {

    private Field parent;

    private Position goal;

    public Spawn(Field parent, Position center, int radius) {
        super(center, radius);
        this.parent = parent;
    }

    public Spawn(Field parent, Position center) {
        super(center);
        this.parent = parent;
    }

    public Spawn(Field parent) {
        this.parent = parent;
    }

    /**
     * A spawn can have a goal; by calling @method moveToGoal(int), the spawn tries
     * to get closer to it
     * @param goal goal
     */
    void setGoal(Position goal){
        this.goal = goal;
    }

    void moveToGoal(int jumps) {
        if (jumps >0) {
            moveToGoal(jumps - 1);
        }
    }

    void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval((int)center.getX(), (int)center.getY(), radius, radius);
    }
}