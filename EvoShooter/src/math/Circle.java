package math;

import field.WallFunction;

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
     * Calculates the collision of this circle with a Function
     * @param f to calculate with
     * @return Position[] of both collision points
     */
    public Position[] collides(Function f) {
        // create vertical Function to f
        Function fV = new Function(1/f.getK());

        // translate vertical Function to hit the center of this Circle
        fV.translateToHit(center);

        // calculate collision P of given Functions
        Position pS = f.collides((fV));

        // In case of a WallFunction there might be no collision ever
        if (pS == null) return null;

        double dis = pS.distance(this.center);

        // if the collisionPoint is within the circle, we know that there is a collision,
        // however we need to further calculate the exact Position
        if (dis <= this.radius) {
            // Both collision points start at Position pS
            Position[] ret = new Position[] {((Position) pS.clone()), ((Position) pS.clone())};

            // Distance to the border of this circle from Position pS on the line of Function f
            double translateDistance = Math.sqrt(Math.pow(this.radius, 2) - Math.pow(dis, 2));

            // translating both positions in different directions
            ret[0].translateTowards(f.getK(), translateDistance);
            ret[1].translateTowards(f.getK(), -translateDistance);
        }
        return null;
    }

}
