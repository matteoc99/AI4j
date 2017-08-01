package field;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public abstract class Function {
    /**
     * Function of type y=kx+d
     */
    double k;

    /**
     * equals xOff & yOff as a single value
     */
    double d;

    Point collides(Function f2) {
        int x = (int)((f2.d-d)/(k-f2.k));
        return new Point(x, (int)calcY(x));
    }

    double calcY(double x) {
        return k*x+d;
    }
}
