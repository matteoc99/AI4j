package math;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
public class Function {
    /**
     * Function of type y=kx+d
     */
    private double k;

    /**
     * equals xOff & yOff as a single value
     */
    public double d;

    public Function(double k, double d) {
        setK(k);
        this.d = d;
    }

    public Function(double k) {
        this(k,0);
    }

    public Function() {
        this(0);
    }

    public void setK(double k) {
        if (Math.abs(k) > 0.0001)
            this.k = k;
        else if (k > 0)
            this.k = 0.0001;
        else
            this.k = -0.0001;
    }

    public double getK() {
        return k;
    }

    public Point collides(Function f) {
        int x = (int)((f.d-d)/(k-f.k));
        return new Point(x, (int)calcY(x));
    }

    public double calcY(double x) {
        return k*x+d;
    }

    public void translateToHit(Point p) {
        d = p.getY()-k*p.getX();
    }

    public void rotateToHit(Point p) {
        // TODO: 01.08.2017
    }

    @Override
    public String toString() {
        return "Function{" +
                "k=" + k +
                ", d=" + d +
                '}';
    }
}
