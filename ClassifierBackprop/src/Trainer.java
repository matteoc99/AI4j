import network.Network;

/**
 * @author Matteo Cosi
 * @since 13.07.2017
 */
public class Trainer {


    /**
     * x1,x2,y*
     * x1...schlaf
     * x2...leren
     * y*...note
     */
    public static double[][] data = {{0.8, 0.4}, {0.9, 0.2}, {0.4, 0.5}, {0.2, 0.4}};
    public static double[][] expected = {{0.8}, {0.6}, {0.5}, {0.2}};

    public static void main(String[] args) {

        Network net = Network.createDFF(2, 1, 1, 2);
        int iterations = 1;
        while (iterations < 1000000) {
            for (int i = 0; i < data.length; i++) {
                double y = net.processData(data[i])[0];
                net.propagateBack(expected[i][0]-y);
            }
            if (iterations % 10000 == 0)
                System.out.println(iterations / 10000);
            iterations++;
        }
        System.out.println("Answer= " + net.processData(new double[]{0.8, 0.6})[0]);
    }
}
