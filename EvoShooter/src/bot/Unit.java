package bot;

import es_map.ESField;
import field_library.field.WallFunction;
import field_library.math.Position;
import values.Values;

/**
 * @author Maximilian Estfeller
 * @since 10.09.2017
 */
public abstract class Unit {

    private ESField field;

    private ESAgent agent;

    private Position position;

    private double frontDirection;

    private int accuracy;

    private int skipMoveValidationCount = 0;

    private int teamIndex = -2;

    private boolean alive = false;

    public Unit(ESAgent agent, ESField field) {
        this.agent = agent;
        this.field = field;
    }

    /**
     * Method gets called on all Units of an Field each tick.
     *
     * get EnvironmentData
     * send Data to Network
     * interpret output
     * adjust fitness
     */
    public void action() {

    }

    /**
     * Unit moves forward for a specific distance
     */
    public void move() {
        Position nextPosition = position.clone();
        nextPosition.translateTowards(frontDirection, Values.UNIT_MOVE_DISTANCE);

        if (skipMoveValidationCount <= 0) {
            if (validateMove(nextPosition))
                this.position = nextPosition;
            else
                onIllegalMove();
        } else {
            this.position = nextPosition;
            skipMoveValidationCount--;
        }

    }

    /**
     * Checks if the next Position of this Unit is free
     * @param pos new Position
     * @return result
     */
    private boolean validateMove(Position pos) {
        double closestWallDistance;

        // distance to the edge of the field
        closestWallDistance = pos.getX();
        if (field.width - pos.getX() < closestWallDistance)
            closestWallDistance = field.getWidth()-pos.getX();
        if (pos.getY() < closestWallDistance)
            closestWallDistance = pos.getY();
        if (field.getHeight() - pos.getY() < closestWallDistance)
            closestWallDistance = field.getHeight() - pos.getY();

        for (WallFunction wall : field.getWalls()) {
            if (wall.closestTo(pos).distanceTo(pos) < closestWallDistance)
                closestWallDistance = wall.closestTo(pos).distanceTo(pos);
        }

        if (closestWallDistance >= Values.UNIT_RADIUS + Values.UNIT_MIN_DISTANCE_TO_WALL) {
            skipMoveValidationCount = (int)closestWallDistance / Values.UNIT_MOVE_DISTANCE;
            return true;
        } else
            return false;
    }

    /**
     * Unit turns towards the given direction dir for a specific amount
     * @param dir to turn towards
     */
    public void turn(double dir) {
        double difference = dir-frontDirection;

        if (Math.abs(difference) <= Values.MAX_UNIT_TURN)
            frontDirection += difference;
        else
            frontDirection += (difference > 0)? + Values.MAX_UNIT_TURN : - Values.MAX_UNIT_TURN;
    }

    /**
     * The Unit shoots in the direction it's looking.
     *
     * A shot is represented by an LineFunction, which starts at this Units position and
     * ends at the Fields border.
     *
     * Calls one of the onHit methods in order to adjust the Agents fitness.
     */
    public void shoot() {

    }

    /**
     * Method gets called when this Unit shoots and hits an enemy Unit.
     * Adjusts the fitness of the Agent.
     */
    public void onEnemyKill() {

    }

    /**
     * Method gets called when this Unit misses a Shot.
     * Only Adjusts the fitness of the Agent.
     */
    public void onMiss() {

    }

    /**
     * Method gets called when this Unit shoots and hits an friendly Unit.
     * Only Adjusts the fitness of the Agent.
     */
    public void onFriendlyKill() {

    }

    /**
     * Method gets called when the Unit tries to move through a wall.
     * Only Adjusts the fitness of the Agent.
     */
    public void onIllegalMove() {

    }

    /**
     * Method gets called when this Unit dies.
     */
    public void onDeath() {
        alive = false;
    }

    public void revive() {
        alive = true;
    }

    /**
     * Method gets called when this Unit sees the flag for the first time.
     * Only Adjusts the fitness of the Agent.
     */
    public void onFlagSightedFirstTime() {

    }

    public void setTeamIndex(int teamIndex) {
        this.teamIndex = teamIndex;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getTeamIndex() {
        return teamIndex;
    }
}
