package field_library.math;

/**
 * @author Maximilian Estfeller
 * @since 10.09.2017
 */
public final class Degrees {

    private Degrees(){}

    /**
     * Returns a value in the range [0, 360[ equivalent to the given
     * value deg, seen as an angle
     * @param deg to calculate with
     * @return normalized degree-value
     */
    public static double normalizeDeg(double deg) {
        if (deg < 0)
            return normalizeDeg(deg + 360);

        if (deg >= 360)
            return normalizeDeg(deg - 360);

        return deg;
    }

    /**
     * Returns a value int the range [0, 90] or ]270, 360[, which slope is equivalent
     * to the given deg
     * @param deg to simplify
     * @return simplified deg
     */
    public static double simplifyDeg(double deg) {
        // in order to simplify a degree, it must be normalized first
        deg = normalizeDeg(deg);

        if (deg > 90) {
            if (deg < 180)
                deg += 180;
            else if (deg <= 270)
                deg -= 180;
        }
        return deg;
    }
}
