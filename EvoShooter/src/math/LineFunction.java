package math;

/**
 * @author Maximilian Estfelller
 * @since 20.08.2017
 */
public class LineFunction extends Function {

    /**
     * start-X and end-X
     */
    protected int a;
    protected int b;

    public LineFunction() {}

    public LineFunction(Position pos1, Position pos2) {
        this.a = (pos1.x <= pos2.x)? pos1.x : pos2.x;
        this.b = (pos1.x > pos2.x)? pos1.x : pos2.x;

        this.setK(pos1.getSlopeTo(pos2));

        translateToHit(pos1);
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    /**
     * Calculates the collision of this Line and a Function
     * @param f to calculate with
     * @return collision
     */
    @Override
    public Position collides(Function f) {
        Position p = super.collides(f);
        if (p.getX() >= a && p.getX() <= b)
            return p;
        return null;
    }

    public class LineData {
        public int a;
        public int b;

        public double k;
        public double d;

        LineData setA(int a) {
            this.a = a;
            return this;
        }

        LineData setB(int b) {
            this.b = b;
            return this;
        }

        LineData setFunction(Function f) {
            this.k = f.getK();
            this.d = f.d;
            return this;
        }
    }
}
