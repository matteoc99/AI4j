package field_library.field;

import field_library.math.FunctionData;
import field_library.math.LineFunction;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class FieldLine extends LineFunction {

    private static AtomicInteger nextId = new AtomicInteger(0);

    private final int id;

    private Field parent;

    private ArrayList<FieldSection> touchedSections = new ArrayList<>();

    FieldLine(Field field, FunctionData data) {
        super(data);

        this.parent = field;
        this.id = nextId.getAndIncrement();

        setK(data.getK());
        this.d = data.getD();

        register();
    }

    /**
     * Registers this Function within the FieldSections
     */
    private void register() {
        for (FieldSection fieldSection : parent.getTouchedSections(this)) {
            fieldSection.addFieldLine(this);
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
        return "FieldLine:"+ id +" {" +
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