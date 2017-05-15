package neuralNetworkLib;

/**
 * Created by matte on 15.05.2017.
 */
public class ValueOutOfBoundException extends RuntimeException {
    public ValueOutOfBoundException(String s) {
        super(s);
    }

    public ValueOutOfBoundException() {
        super();
    }
}
