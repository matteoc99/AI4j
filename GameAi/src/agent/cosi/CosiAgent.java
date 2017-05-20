package agent.cosi;

import agent.Agent;
import neural_network_lib.Network;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class CosiAgent extends Agent {

    public static void main(String[] args) {
        Network n = Network.createDFF(2, 4, 1, 6);
        int len = n.generateDescriptor().length;
        for (int i = 0; i < len; i++) {
              System.out.print(n.getDescriptor()[i]+" ");
        }

      /* double []in = new double[n.getLayerByIndex(0).getNeuronCount()];
        for (int i = 0; i < in.length; i++) {
            in[i]=Math.random();
        }
        double[] d = n.processData(in);
        for (double aD : d) System.out.println(aD);
*/

        System.out.println();
        Network net = new Network(n.getDescriptor());
        for (int i = 0; i < net.generateDescriptor().length; i++) {
                System.out.print(net.getDescriptor()[i]+" ");
        }
    }
}
