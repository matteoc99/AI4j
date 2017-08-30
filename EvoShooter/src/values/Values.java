package values;

/**
 * @author Maximilian Estfeller
 * @since 01.08.2017
 */
public abstract class Values {
    public static final int UNIT_RADIUS = 5;
    public static final int FLAG_RADIUS = 4;
    public static final int SPAWN_RADIUS = 4;

    // K_MAX * K_MIN must be 1
    public static final double K_MAX = 10000;  // 10 ^  |x|
    public static final double K_MIN = 0.0001; // 10 ^ -|x|

    public static final int POSSIBLE_SPAWN_POSITIONS_COUNT = 24;

    public static final int REACH_FLAG_MINIMUM_PERCENTAGE = 20;
}
