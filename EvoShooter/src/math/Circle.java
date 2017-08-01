package math;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Circle {

    /**
     * Point describing the center of this Circle
     */
    public Point center;

    /**
     * radius of this Circle
     */
    public int radius;

    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle(Point center) {
        this(center, 1);
    }

    public Circle() {
        this(new Point(0, 0));
    }

    Point[] collides(Function f) {
        // create vertical Function to f
        Function fV = new Function(1/f.getK());

        // translate vertical Function to hit the center of this Circle
        fV.translateToHit(center);

        // calculate collision P of given Functions
        Point pS = f.collides(fV);

        double dis = pS.distance(this.center);

        // if the collision happens within the circle, we know that there is a collision,
        // however we need to further calculate the exact point
        if (dis <= this.radius) {
            Point[] ret = new Point[2];
            double inCircleDis = Math.sqrt(Math.pow(this.radius, 2) - Math.pow(dis, 2));

        }
        return null;
    }
}
