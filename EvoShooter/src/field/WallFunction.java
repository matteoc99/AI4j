package field;

import math.FunctionData;
import math.LineFunction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class WallFunction extends LineFunction {

    private static AtomicInteger nextId = new AtomicInteger(0);

    private final int id;

    private Field parent;

    private ArrayList<FieldSection> touchedSections = new ArrayList<>();

    WallFunction(Field field, FunctionData data) {
        this.parent = field;
        this.id = nextId.getAndIncrement();

        this.a = data.getA();
        this.b = data.getB();

        setK(data.getK());
        this.d = data.getD();

        register();
    }

    /**
     * Registers this Function within the FieldSections
     */
    private void register() {
        for (FieldSection fieldSection : parent.getTouchedSections(this)) {
            fieldSection.addWall(this);
            touchedSections.add(fieldSection);
        }
    }

    public ArrayList<FieldSection> getTouchedSections() {
        return touchedSections;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "WallFunction:"+ id +" {" +
                "k=" + getK() +
                ", d=" + d +
                ", a=" + a +
                ", b=" + b +
                "}:\n" +
                "y="+getK()+"*x+"+d;
    }

    void paint(Graphics g) {
        g.drawLine((int) a, (int) calcY(a), (int) b, (int) calcY(b));
    }
}