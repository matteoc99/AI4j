package math;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Position extends Point{

    public Position(int x, int y) {
        super(x, y);
    }

    /**
     * Translates this point in a given direction for a given distance
     * @param k direction
     * @param distance distance
     */
    public void translateTowards(double k, double distance) {
        double normDistance = Math.sqrt(Math.pow(k, 2) + 1);
        double factor = distance/normDistance;

        this.x+=factor;
        this.y+=factor*k;
    }

    public double getSlopeTo(Position pos) {
        // TODO: 20.08.2017 Verify by testing
        if (this.y == pos.y) return 0.0001;
        if (this.x == pos.x) return 10000;

        return (this.y-pos.y)/(this.x-pos.x);
    }

    @Override
    public Position clone() {
        return (Position)super.clone();
    }
}
