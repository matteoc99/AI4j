package neural_network_lib;

/**
 * @author Matteo Cosi
 * @since 15.04.2017
 */
public class ValueOutOfBoundException extends RuntimeException {
    public ValueOutOfBoundException(String s) {
        super(s);
    }

    public ValueOutOfBoundException() {
        super();
    }
}
