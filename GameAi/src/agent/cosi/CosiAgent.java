package agent.cosi;

import agent.Agent;
import neural_network_lib.Network;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class CosiAgent extends Agent {

    public static void main(String[] args) {
        Network n = Network.createDFF(3, 10, 0, 0);
        double[] d = n.processData(new double[]{0.8, 0.2, 0.3});
        for (int i = 0; i < d.length; i++) {
            System.out.println(d[i]);
        }
    }
}
