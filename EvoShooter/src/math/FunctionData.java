package math;

/**
 * @author Maximilian Estfelller
 * @since 21.08.2017
 */
public class FunctionData {
    private Double a;
    private Double b;

    private Double k;
    private Double d;

    public FunctionData setA(double a) {
        this.a = a;
        return this;
    }

    public double getA() {
        if (a != null)
            return a;
        throw new InsufficientFunctionDataException("A");
    }

    public FunctionData setB(double b) {
        this.b = b;
        return this;
    }

    public double getB() {
        if (b != null)
            return b;
        throw new InsufficientFunctionDataException("B");
    }

    public FunctionData setFunction(Function f) {
        this.k = f.getK();
        this.d = f.d;
        return this;
    }

    public double getK() {
        if (k != null)
            return k;
        throw new InsufficientFunctionDataException("K");
    }

    public double getD() {
        if (d != null)
            return d;
        throw new InsufficientFunctionDataException("D");
    }

    public class InsufficientFunctionDataException extends RuntimeException {
        public InsufficientFunctionDataException(String msg) {
            super(msg);
        }
    }
}
