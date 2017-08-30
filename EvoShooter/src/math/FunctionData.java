package math;

import values.Values;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Maximilian Estfelller
 * @since 21.08.2017
 */
@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class FunctionData implements Serializable {
    private Double a;
    private Double b;

    private Double k;
    private Double d;

    public FunctionData setA(double a) {
        this.a = a;
        return this;
    }

    public static ArrayList<FunctionData> transformToFD(Collection functions) {
        ArrayList<FunctionData> ret = new ArrayList<>();

        for (Object function : functions)
            if (function instanceof Function)
                ret.add(((Function) function).getFunctionData());

        return ret;
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

    public FunctionData setK(double k) {
        if (Math.abs(k) > Values.K_MIN)
            this.k = (int) (k* Values.K_MAX) / Values.K_MAX;
        else if (k > 0)
            this.k = Values.K_MIN;
        else
            this.k = -Values.K_MIN;

        if (k > Values.K_MAX) this.k = Values.K_MAX;

        return this;
    }

    public FunctionData setD(double d) {
        this.d = d;
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

    private class InsufficientFunctionDataException extends RuntimeException {
        private InsufficientFunctionDataException(String msg) {
            super(msg);
        }
    }
}
