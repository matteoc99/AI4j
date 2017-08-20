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
public class Spawn extends Circle {

    private Field parent;

    private Position goal;

    private GoalPath goalPath;

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
        goalPath = new GoalPath(goal);
    }

    void moveToGoal(int jumps) {
        if (jumps > 0)
            moveToGoal(jumps - 1);


    }

    /**
     * Method creates LineFunctions. Seen as an 2D Object, their area represents every possible
     * position occupied by this Circle, when moving from pos1 to pos2
     *
     * Given that none of the returned LineFunctions collide with a wall, there is
     * a high chance that the path is free.
     *
     * ATTENTION: There is still a chance for walls to be completely within the above mentioned area,
     * and thereby basically invisible.
     *
     * @param pos1 start
     * @param pos2 end
     * @return LineFunctions[3]
     */
    private LineFunction[] getMovementLines(Position pos1, Position pos2) {
        Position h = pos1;
        pos1 = (pos1.x <= pos2.x)? pos1 : pos2;
        pos2 = (pos1.x > pos2.x)? h : pos2;

        double k = pos1.getSlopeTo(pos2);

        Position lineA_pos1 = pos1.clone();
        lineA_pos1.translateTowards(1/k, radius);

        Position lineB_pos1 = pos1.clone();
        lineB_pos1.translateTowards(1/k, -radius);

        Position lineA_pos2 = pos2.clone();
        lineA_pos2.translateTowards(1/k, radius);
        lineA_pos2.translateTowards(k, radius);

        Position lineB_pos2 = pos2.clone();
        lineB_pos2.translateTowards(1/k, -radius);
        lineB_pos2.translateTowards(k, radius);

        return new LineFunction[] {
                new LineFunction(lineA_pos1, lineA_pos2),
                new LineFunction(lineB_pos1, lineB_pos2),
                new LineFunction(lineA_pos2, lineB_pos2)
        };
    }

    void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval((int)center.getX(), (int)center.getY(), radius, radius);
    }
}