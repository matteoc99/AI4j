package estfeller;

/**
 * @author Maximilian Estfelller
 * @since 09.06.2017
 */
public class Utils {

    static void mutateDescriptor(double[] descriptor) {
        int offset = (int) (descriptor[0]) + 1;
        for (int i = offset; i < descriptor.length; i++) {
            descriptor[i] = descriptor[i]+Math.random()*0.2-0.1;
            if (descriptor[i] > 1) descriptor[i] = 1;
            if (descriptor[i] < -1) descriptor[i] = -1;
        }
    }
}
