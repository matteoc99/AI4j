package field;

import math.FunctionData;
import math.LineFunction;
import math.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 * y=kx+d
 */
public class WallFunction extends LineFunction {

    private static AtomicInteger nextID = new AtomicInteger(0);

    private final int iD;

    private Field parent;

    private ArrayList<FieldSection> touchedSections = new ArrayList<>();

    WallFunction(Field field) {
        this.parent = field;
        this.iD = nextID.getAndIncrement();

        create();
        register();
    }

    WallFunction(Field field, FunctionData data) {
        this.parent = field;
        this.iD = nextID.getAndIncrement();

        this.a = data.getA();
        this.b = data.getB();

        setK(data.getK());
        this.d = data.getD();
    }

    /**
     * Creates the random Function
     */
    private void create() {
        // degree of the function
        double     deg;
        // offsets to guarantee the function has any Point within this field
        int     xOff;
        int     yOff;
        // length of the visible function
        double  length;

        // generating random values
        deg = (Math.random() * 360);
        xOff = (int) (Math.random() * (parent.getWidth()-1));
        yOff = (int) (Math.random() * (parent.getHeight()-1));

        setK(calcSlopeByDeg(deg));

        // deciding length of the visible part of the wall + validation
        do {
            length = (parent.getWidth() + parent.getHeight()) / 4 + new Random().nextGaussian() * 10;
        } while (length < (parent.getWidth() + parent.getHeight()) / 20 || length > parent.getWidth() || length > parent.getHeight());
        double h = (Math.abs(Math.cos(Math.toRadians(deg))) * length);

        a = xOff -h/2;
        b = xOff +h/2;

        // conversion to normal Function format for simpler calculation
        d = getK() * -xOff + yOff;

        // adjusting a and b to be within the field
        if (a < 0) a = 0;
        if (b > parent.getWidth()-1) b = parent.getWidth()-1;

        // adjusting a and b so that their y-value is within the field

        double topOut = calcX(parent.getHeight()-1);
        double botOut = calcX(0);

        double aOut = (topOut < botOut)? topOut : botOut;
        double bOut = (topOut > botOut)? topOut : botOut;

        if (a < aOut) a = aOut;
        if (b > bOut) b = bOut;
    }

    /**
     * Registers this Function within the FieldSections
     */
    public void register() {
        for (FieldSection fieldSection : parent.getTouchedSections(this)) {
            fieldSection.addWall(this);
            touchedSections.add(fieldSection);
        }
    }

    public ArrayList<FieldSection> getTouchedSections() {
        return touchedSections;
    }

    public int getiD() {
        return iD;
    }

    @Override
    public String toString() {
        return "WallFunction:"+iD+" {" +
                "k=" + getK() +
                ", d=" + d +
                ", a=" + a +
                ", b=" + b +
                "}:\ny="+getK()+"*x+"+d;
    }

    void paint(Graphics g) {
        g.drawLine((int) a, (int) calcY(a), (int) b, (int) calcY(b));
    }
}