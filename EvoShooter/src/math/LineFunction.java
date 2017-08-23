package math;

/**
 * @author Maximilian Estfelller
 * @since 20.08.2017
 */
public class LineFunction extends Function {

    /**
     * start-X and end-X
     */
    protected double a;
    protected double b;

    public LineFunction() {}

    public LineFunction(Position pos1, Position pos2) {
        this.a = (int)((pos1.getX() <= pos2.getX())? pos1.getX() : pos2.getX());
        this.b = (int)((pos1.getX() > pos2.getX())? pos1.getX() : pos2.getX());

        this.setK(pos1.getSlopeTo(pos2));

        translateToHit(pos1);
    }

    public LineFunction(FunctionData data) {
        super(data);
        this.a = data.getA();
        this.b = data.getB();
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    @Override
    public FunctionData getFunctionData() {
        return super.getFunctionData().setA(a).setB(b);
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
}
