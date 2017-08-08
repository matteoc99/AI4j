import network.Network;
import network_gui.NetworkGUI;

/**
 * @author Matteo Cosi
 * @since 13.07.2017
 */
public class Trainer {


    /**
     * x1,x2,y*
     * x1...schlaf
     * x2...lernen
     * y*...note
     */
    public static double[][] data = {{0.8, 0.4}, {0.9, 0.2}, {0.4, 0.5}, {0.2, 0.4}};
    public static double[][] expected = {{1}, {0.75}, {0.25}, {0}};

    public static void main(String[] args) {
        Network net = Network.createDFF(2, 1, 1, 100);
        System.out.println("before= " + net.processData(new double[]{0.5, 0.5})[0]);
        int iterations = 1;
        double rate = 0.04;
        while (iterations < 10000) {
            for (int i = 0; i < data.length; i++) {
                double y = net.processData(data[i])[0];
                net.propagateBack(rate, expected[i][0] - y);
            }
            iterations++;
            if (iterations % 1000 == 0)
                System.out.println(iterations / 1000);
        }
        System.out.println("after= " + net.processData(new double[]{0.9, 0.2})[0]);

    }
}
