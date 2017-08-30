package math;


/**
 * @author Maximilian Estfelller
 * @since 01.08.2017
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Position implements Cloneable{

    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the distance from this Position to the given Position
     *
     * @param pos Position to calculate with
     * @return the distance
     */
    public double distanceTo(Position pos) {
        double pX = pos.getX() - getX();
        double pY = pos.getY() - getY();

        return Math.sqrt(pX * pX + pY * pY);
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

        return (this.y - pos.y) / (this.x - pos.x);
    }

    @Override
    public Position clone() {
        try {
            return (Position)super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
