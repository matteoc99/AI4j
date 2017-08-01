package math;

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

    /**
     * checks for k not to be to close to 0, as this could cause problems
     * ex: calculating 1/k
     * @param k to set
     */
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

    /**
     * Calculates the collision of two functions
     * @param f to calculate with
     * @return Position of the collision
     */
    public Position collides(Function f) {
        int x = (int)((f.d-d)/(k-f.k));
        return new Position(x, (int)calcY(x));
    }

    /**
     * calculates result of the function given x
     * @param x to fill in the function
     * @return result y
     */
    public double calcY(double x) {
        return k*x+d;
    }

    /**
     * Translates the function on the y-Axis so that it collides with Position p
     * Changes var:d
     * @param p to collide with
     */
    public void translateToHit(Position p) {
        d = p.getY()-k*p.getX();
    }

    /**
     * Rotates the function so that it collides with Position p
     * changes var:k
     * @param p to collide with
     */
    public void rotateToHit(Position p) {
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
