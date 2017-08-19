package field;

import math.Function;
import math.Position;

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

    public final int iD;

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
        xOff = (int) (Math.random() * parent.width);
        yOff = (int) (Math.random() * parent.height);

        setK(calcSlopeByDeg(deg));

        // deciding length of the visible part of the wall + validation
        do {
            length = (parent.width + parent.height) / 4 + new Random().nextGaussian() * 10;
        } while (length < (parent.width + parent.height) / 20 || length > parent.width || length > parent.height);
        int h = (int) (Math.cos(Math.toRadians(Math.abs(deg))) * length);

        a = xOff -h/2;
        b = xOff +h/2;

        // conversion to normal Function format for simpler calculation
        d = getK() * -xOff + yOff;

        // adjusting a and b to be within the field
        if (a < 0) a = 0;
        if (b > parent.width) b = parent.width;

        // adjusting a and b so that their y-value is within the field

        double topOut = calcX(parent.height);
        double botOut = calcX(0);

        int aOut = (topOut < botOut)? (int)topOut+1:(int)botOut+1;
        int bOut = (topOut > botOut)? (int)topOut:(int)botOut;

        if (a < aOut) a = aOut;
        if (b > bOut) b = bOut;
    }

    /**
     * Registers this Function within the FieldSections
     */
    private void register() {
        int startY = (int)calcY(a);
        int endY = (int)calcY(b);

        FieldSection startSection = parent.getSectionAt(new Position(a, startY));
        FieldSection endSection = parent.getSectionAt(new Position(b, endY));

        // register at start and end
        startSection.addWall(this);
        endSection.addWall(this);

        // Finding affected Sections between startSection and endSection

        // Horizontal
        if (startSection.X != endSection.X) {
            for (int i = startSection.X; i < endSection.X; i++) {
                // line indicating the start of a new Section
                int rightLine = parent.getSectionFromArray(i, 0).RIGHT + 1; // add 1 to get to the next Section

                // calculating the height at which this WallFunction enters a new Section
                int yRes = (int)calcY(rightLine);

                parent.getSectionAt(new Position(rightLine, yRes)).addWall(this);
            }
        }

        // Vertical
        if (startSection.Y != endSection.Y) {
            // order
            FieldSection smallerSection = (startSection.Y < endSection.Y)? startSection:endSection;
            FieldSection biggerSection = (startSection.Y > endSection.Y)? startSection:endSection;

            for (int i = smallerSection.Y; i < biggerSection.Y; i++) {
                // line indicating the start of a new Section
                int botLine = parent.getSectionFromArray(0, i).BOT + 1; // add 1 to get to the next Section

                // calculating the width at which this WallFunction enters a new Section
                int xRes = (int)calcX(botLine);

                parent.getSectionAt(new Position(xRes, botLine)).addWall(this);
            }
        }
    }

    /**
     * Calculates the collision of this Wall and a Function
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