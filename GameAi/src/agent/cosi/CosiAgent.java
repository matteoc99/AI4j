package agent.cosi;

import agent.Agent;
import neural_network_lib.Network;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class CosiAgent extends Agent {

    public static void main(String[] args) {
        Network n = Network.createDFF(10, 4, 4, 6);
        for (int i = 0; i < n.generateDescriptor().length; i++) {
            System.out.print(n.getDescriptor()[i]+" ");
        }

     /*  double []in = new double[8];
        for (int i = 0; i < in.length; i++) {
            in[i]=Math.random();
        }
        double[] d = n.processData(in);
        for (double aD : d) System.out.println(aD);
*/
    }
}
