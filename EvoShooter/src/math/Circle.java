package math;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Circle {

    /**
     * Position describing the center of this Circle
     */
    public Position center;

    /**
     * radius of this Circle
     */
    public int radius;

    public Circle(Position center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle(Position center) {
        this(center, 1);
    }

    public Circle() {
        this(new Position(0, 0));
    }

    /**
     * Calculates the collision of this circle with a Function.
     *
     * Returns null when there are no collisions.
     *
     * In case of a LineFunction there can also be just a single value within
     * the returned Position[].
     *
     * NOTE: The reason, why null is returned instead of an empty Array, is that
     * this function should be used very similar to the collides() method of a LineFunction,
     * which returns either a single Position or null
     *
     * @param f to calculate with
     * @return Position[] of both collision points
     */
    public Position[] collides(Function f) {
        // create vertical Function to f
        Function fV = new Function(-1/f.getK());

        // translate vertical Function to hit the center of this Circle
        fV.translateToHit(center);

        // calculate collision P of given Functions
        Position pS = f.collidesForceBase((fV));

        double dis = pS.distance(this.center);

        // if the collisionPoint is within the circle, we know that there (most likely) is a collision,
        // however we need to further calculate the exact Position
        if (dis < this.radius)
            return calcExactCollision(f, pS, dis);
        else if (dis == this.radius)
            return new Position[]{pS};
        else
            return null;
    }

    /**
     * in collides() we only calculated whether a collision is possible or not, by
     * calculating the Position pS, which is the closest Position of the Function f to the center of the circle.
     *
     * Method doesn't get called when the distance is greater then the circles radius
     *
     * By "exact" collisionPoint, we mean the Positions the Function f enters and leaves the Circle
     *
     * In case of aLineFunction there can also be no collision at all
     *
     * @param f to calc with
     * @param pS closest Position to the center of the circle
     * @param dis distance between the center and Position pS
     * @return Collisions of Function f with the border of this circle
     */
    private Position[] calcExactCollision(Function f, Position pS, double dis) {
        // Both collision points start at Position pS
        Position[] ret = new Position[] {(pS.clone()), (pS.clone())};

        // Distance to the border of this circle from Position pS on the line of Function f
        double translateDistance = Math.sqrt(Math.pow(this.radius, 2) - Math.pow(dis, 2));

        // translating both positions in different directions
        ret[0].translateTowards(f.getK(), -translateDistance); // always left
        ret[1].translateTowards(f.getK(), translateDistance); // always right

        // all calculations above base on Function f to have no limits like a LineFunction
        // a LineFunction however can end before reaching the Circle
        if (f instanceof LineFunction) {
            return verifyCollisions((LineFunction)f, ret);
        }
        else
            return ret;
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
     * NOTE: A better solution has been found
     *
     * @param pos1 start
     * @param pos2 end
     * @return LineFunction[3]
     */
    private LineFunction[] getMovementLines(Position pos1, Position pos2) {
        Position h = pos1;
        pos1 = (pos1.getX() <= pos2.getX())? pos1 : pos2;
        pos2 = (pos1.getX() > pos2.getX())? h : pos2;

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

    /**
     * Method verifies a collision calculated by collides(Function) by checking
     * whether the given Positions are  part of the LineFunction or not.
     * Only collisions that have been verified are returned.
     *
     * Returns null instead of an empty Array
     * @see #collides(Function)
     *
     * @param f LineFunction
     * @param collisions calculated by collides(Function)
     * @return Array with verified Positions
     */
    private Position[] verifyCollisions(LineFunction f, Position[] collisions) {
        ArrayList<Position> ret = new ArrayList<>(Arrays.asList(collisions));
        ret.removeIf(position -> position.getX() >= f.getA() && position.getX() <= f.getB());
        if (ret.isEmpty())
            return null;
        else
            return ret.toArray(new Position[ret.size()]);
    }
}
