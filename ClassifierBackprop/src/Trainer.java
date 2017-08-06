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
        NetworkGUI networkGUI = new NetworkGUI();
        Network net = Network.createDFF(2, 1, 2, 2);
        networkGUI.addNetwork(net);
        System.out.println("before= " + net.processData(new double[]{1, 1})[0]);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int iterations = 1;
        while (iterations < 100000) {
            for (int i = 0; i < data.length; i++) {
                double y = net.processData(data[i])[0];
                net.propagateBack(expected[i][0]-y);
            }
            iterations++;
        }
        System.out.println("after= " + net.processData(new double[]{1, 1})[0]);
        networkGUI.refreshNetwork(net);

    }
}
