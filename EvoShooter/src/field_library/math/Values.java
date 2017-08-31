package field_library.math;

/**
 * @author Maximilian Estfeller
 * @since 31.08.2017
 */
public abstract class Values {
    static final double K_MAX;
    static final double K_MIN;

    static {
        // ONLY CHANGE THIS VALUE
        int x = 6;

        K_MAX = Math.pow(10, x);
        K_MIN = Math.pow(10, -x);
    }
}
