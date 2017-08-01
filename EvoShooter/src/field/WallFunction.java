package field;

import math.Function;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 * y=kx+d
 */
public class WallFunction extends Function {

    private static AtomicInteger nextID = new AtomicInteger(0);

    private int iD;

    /**
     * start-X and end-X
     */
    private int a;
    private int b;

    private Field parent;

    WallFunction(Field field) {
        this.parent = field;
        this.iD = nextID.getAndIncrement();
        create();
        register();
    }

    /**
     * create the random function
     */
    private void create() {
        // degree of the function
        int     deg;
        // offsets to guarantee the function as any Point within this field
        int     xOff;
        int     yOff;
        // length of the visible function
        double  length;

        // generating random values
        deg = (int) (Math.random() * 90);
        xOff = (int) (Math.random() * parent.width);
        yOff = (int) (Math.random() * parent.height);

        // calculating k given deg
        setK(Math.tan(Math.toRadians(deg)));
        setK((int)(getK()*100000)/100000.0);
        // 50% of a negative k
        if (Math.random() < 0.5)
            setK(-getK());

        // deciding length of the visible part of the wall + validation
        do {
            length = (parent.width + parent.height) / 4 + new Random().nextGaussian() * 10;
        } while (length < (parent.width + parent.height) / 20 || length > parent.width || length > parent.height);
        int h = (int) (Math.cos(Math.toRadians(deg)) * length);

        a = xOff -h/2;
        b = xOff +h/2;

        // adjusting a and b to be within the field
        if (a < 0) a = 0;
        if (b > parent.width) b = parent.width;

        // conversation to normal Function format for simpler calculation
        d = getK() * -xOff + yOff;
    }

    /**
     * register this function within the fieldSections
     */
    private void register() {

    }

    Point collides(WallFunction f2) {
        Point p = super.collides(f2);
        if (p.getX() >= a && p.getX() <= b)
            return p;
        return null;
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
        g.drawLine(a, (int) calcY(a), b, (int) calcY(b));
    }
}