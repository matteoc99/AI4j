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
        this(k, 0);
    }

    public Function() {
        this(0);
    }

    /**
     * checks for k not to be to close to 0, as this could cause problems
     * ex: calculating 1/k given k = 0;
     *
     * k has to be in range of [0.0001, 10000]
     *
     * @param k to set
     */
    public void setK(double k) {
        if (Math.abs(k) > 0.0001)
            this.k = (int) (k*10000) / 10000.0;
        else if (k > 0)
            this.k = 0.0001;
        else
            this.k = -0.0001;

        if (k > 10000) this.k = 10000;
    }

    public double getK() {
        return k;
    }

    /**
     * Method return the equivalent k-value for a given degree value
     * @param deg to calculate with (must be in range 1-89)
     * @return k
     */
    public static double calcSlopeByDeg(double deg) {
        // deg has to be in range [0, 360[
        deg = normalizeDeg(deg);

        // deg has to be in range [0, 90] or ]270, 360[
        deg = simplifyDeg(deg);

        if (deg == 0) deg = 0.0001;
        if (deg == 90) deg = 89.9999;

        return Math.tan(Math.toRadians(deg));

    }

    /**
     * Returns a value in the range [0, 360[ equivalent to the given
     * value deg, seen as an angle
     * @param deg to calculate with
     * @return normalized degree-value
     */
    private static double normalizeDeg(double deg) {
        if (deg < 0)
            return normalizeDeg(deg + 360);

        if (deg >= 360)
            return normalizeDeg(deg - 360);

        return deg;
    }

    /**
     * Returns a value int the range [0, 90] or ]270, 360[, which slope is equivalent
     * to the given deg
     * @param deg to simplify
     * @return simplified deg
     */
    private static double simplifyDeg(double deg) {
        if (deg > 90) {
            if (deg < 180)
                deg += 180;
            else if (deg <= 270)
                deg -= 180;
        }
        return deg;
    }

    /**
     * Calculates the collision of two functions
     * @param f to calculate with
     * @return Position of the collision
     */
    public Position collides(Function f) {
        // The Slope of a WallFunction hat at most 5 point numbers and therefore will never call the else block
        // Exception: Equal slope
        if (Math.abs(k-f.k) > 0.00001) {
            int x = (int)((f.d - d) / (k - f.k));
            return new Position(x, (int)calcY(x));
        } else {
            // Slopes are very similar, result could extend range of an Integer
            return secureCollides(f);
        }
    }

    /**
     * Equal to collides(),
     * however this method checks for collisions, which values extend the range of an Integer
     * and also reacts to functions with equal slope (no collision at all)
     * In the above mentioned cases a Position with max values is returned
     * @param f to calculate with
     * @return Position of the collision
     */
    private Position secureCollides(Function f) {
        int x;
        try {
            double res = (f.d - d) / (k - f.k);
            if (res > Integer.MAX_VALUE)
                return new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
            x = (int) res;
        } catch (ArithmeticException e) {
            return new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
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
     * calculates result of the function given y
     * @param y to fill in the function
     * @return result x
     */
    public double calcX(double y) {
        return (y-d)/k;
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
     * Rotates the Function in Position(0, d), so that it collides with Position p
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
