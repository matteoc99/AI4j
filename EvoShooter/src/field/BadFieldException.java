package field;

/**
 * There are a lot of randomly generated things within a field,
 * which leads to the possibility of creating a "bad" map
 * Basically things, that make the map unplayable or almost senseless to train on.
 *
 * This also includes maps, which imply calculations towards infinity or sometimes over Integer's limit.
 *
 * Can also be thrown when specific mapObjects are broken. (stuck..)
 * Maximilian Estfeller
 * @since 01.08.2017
 */
public class BadFieldException extends RuntimeException {

    public BadFieldException(String msg) {
        super(msg);
    }
}
