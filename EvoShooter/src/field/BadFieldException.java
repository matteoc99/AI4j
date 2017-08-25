package field;

/**
 * There are a lot of randomly generated things within a field,
 * which leads to the possibility of creating a "bad" map
 * Basically things, that make the map unplayable or almost senseless to train on.
 *
 * Further reasons are:
 * Map which imply calculations towards infinity or sometimes over Integer's limit.
 * Whenever specific mapObjects are broken. (stuck..)
 * Constants are set wrong.
 *
 *
 * @author Maximilian Estfeller
 * @since 01.08.2017
 */
class BadFieldException extends RuntimeException {

    BadFieldException(String msg) {
        super(msg);
    }
}
