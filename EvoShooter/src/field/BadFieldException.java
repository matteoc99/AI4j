package field;

/**
 * There are a lot of randomly generated things within a field,
 * which leads to the possibility of creating a "bad" map
 * Basically things, that make the map unplayable or almost senseless to train on.
 *
 * Further reasons are:
 * Maps, which imply calculations towards infinity or sometimes over Integer's limit.
 * Whenever specific mapObjects are broken. (stuck..)
 * Constants are set wrong.
 *
 *
 * @author Maximilian Estfeller
 * @since 01.08.2017
 */
class BadFieldException extends RuntimeException {

    public enum BadFieldType {
        /**
         * Occurs when, due to the use of randomness, a Field turns out to be bad
         */
        RANDOM_ERROR,
        /**
         * One or more Constants are set wrongly
         */
        CONST_ERROR,
        /**
         * Unexpected error
         */
        BUG_ERROR
    }

    private BadFieldType type;

    BadFieldException(String msg, BadFieldType type) {
        super(msg);
        this.type = type;
    }

    public boolean isBug() {
        return type == BadFieldType.BUG_ERROR;
    }

    public boolean isRandom() {
        return type == BadFieldType.RANDOM_ERROR;
    }
}
