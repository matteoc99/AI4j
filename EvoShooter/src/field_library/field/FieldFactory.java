package field_library.field;

import field_library.math.FunctionData;
import values.Values;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static field_library.math.Function.calcSlopeByDeg;
import static field_library.math.Function.calcXRaw;

/**
 * @author Maximilian Estfeller
 * @since 29.08.2017
 */
public final class FieldFactory {

    private FieldFactory() {}

    public static Field createRandomField(int width, int height, int lineAmount) {
        return createRandomField(width, height, lineAmount, Field.class);
    }

    public static <T extends Field>T createRandomField(int width, int height, int lineAmount, Class<T> type) {
        try {
            try {
                T field = getT(width, height, type);

                field.addFieldLines(createRandomFieldLines(field, lineAmount));

                return field;
            } catch (BadFieldException e) {
                if (e.isRandom()) return createRandomField(width, height, lineAmount, type);
                else throw e;
            }
        } catch (StackOverflowError error) {
            throw new BadFieldException("Can't create a Field with given Constants",
                    BadFieldException.BadFieldType.CONST_ERROR);
        }
    }

    /**
     * Method returns an Object of type <T extends Field>
     * @param width constructorParam
     * @param height constructorParam
     * @param type specifies the type of the Object to return
     * @param <T> Field or subclass
     * @return Object of type <T extends Field>
     */
    private static <T extends Field> T getT(int width, int height, Class<T> type) {
        T field;

        try {
            field = type.getConstructor(int.class, int.class, int.class, int.class).
                    newInstance(width, height,
                    width  / (4*Values.UNIT_RADIUS),
                    height / (4*Values.UNIT_RADIUS));

        } catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            throw new BadFieldException("Can't create given Class with given Constructor parameters.\n"+
                    "All subclasses of Field must implement the following public constructor: " +
                    "Field(int, int, int, int)\n"+
                    "All subclasses of FieldSection must implement the following public constructor: " +
                    "FieldSection(Field, int, int, Position, Position",
                    BadFieldException.BadFieldType.BUG_ERROR);
        }
        return field;
    }

    public static Collection<FunctionData> createRandomFieldLines(Field field, int amount) {
        Collection<FunctionData> lines = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            lines.add(createRandomFieldLine(field));
        }
        return lines;
    }

    public static FunctionData createRandomFieldLine(Field field) {
        FunctionData ret = new FunctionData();

        // degree of the function
        double  deg;
        // offsets to guarantee the function has any Point within this Field
        int     xOff;
        int     yOff;
        // length of the visible function
        double  length;

        // generating random values
        deg = (Math.random() * 360);
        xOff = (int) (Math.random() * (field.width-1));
        yOff = (int) (Math.random() * (field.height-1));

        ret.setK(calcSlopeByDeg(deg));

        // deciding length of the visible part of the line + validation
        do {
            length = (field.width + field.height) / 4 + new Random().nextGaussian() * 10;
        } while (length < (field.width + field.height) / 20 ||
                length > field.width || length > field.height);

        double h = (Math.abs(Math.cos(Math.toRadians(deg))) * length);

        ret.setA(xOff -h/2);
        ret.setB(xOff +h/2);

        // conversion to normal Function format for simpler calculation
        ret.setD(ret.getK() * -xOff + yOff);

        // adjusts a and b to keep EVERY point of the LineFunction within the field_library.field
        adjustABValues(field, ret);

        return ret;
    }

    private static void adjustABValues(Field field, FunctionData ret) {
        // adjusting a and b to be within the Field
        if (ret.getA() < 0) ret.setA(0);
        if (ret.getB() > field.width-1) ret.setB(field.width-1);

        // adjusting a and b so that their y-value is within the Field

        double topOut = calcXRaw(field.height-1, ret);
        double botOut = calcXRaw(0, ret);

        double aOut = (topOut < botOut)? topOut : botOut;
        double bOut = (topOut > botOut)? topOut : botOut;

        if (ret.getA() < aOut) ret.setA(aOut);
        if (ret.getB() > bOut) ret.setB(bOut);
    }
}
