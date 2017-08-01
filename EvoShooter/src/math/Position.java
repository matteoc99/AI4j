package math;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Position extends Point{

    public Position() {
        super();
    }

    public Position(int x, int y) {
        super(x, y);
    }

    public void translateTowards(double k, double distance) {
        double normDistance = Math.sqrt(Math.pow(k, 2) + 1);
        double factor = distance/normDistance;

        this.x+=factor;
        this.y+=factor*k;
    }
}
