package math;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Position extends Point{

    public Position(int x, int y) {
        super(x, y);
        if (x < 0) this.x = 0;
        if (y < 0) this.y = 0;
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
}
