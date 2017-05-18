package agent.cosi;

import agent.Agent;
import neural_network_lib.Function;
import neural_network_lib.Network;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class CosiAgent extends Agent {

    public static void main(String[] args) {
        Network n = Network.createDFF(3, 1, 2, 5);
       double []in = new double[3];
        for (int i = 0; i < in.length; i++) {
            in[i]=Math.random();
        }
        double[] d = n.processData(in);
        for (int i = 0; i < d.length; i++) {
            System.out.println(d[i]);
        }

    }
}
