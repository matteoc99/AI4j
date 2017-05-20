package agent.cosi;

import agent.Agent;
import neural_network_lib.Network;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class CosiAgent extends Agent {

    public static void main(String[] args) {
        Network n = Network.createDFF(2, 4, 3, 6);
        int len = n.generateDescriptor().length;
        for (int i = 0; i < len; i++) {
            System.out.print(n.getDescriptor()[i]+" ");
        }
        double []in = new double[n.getLayerByIndex(0).getNeuronCount()];
        for (int i = 0; i < in.length; i++) {
            in[i]=Math.random();
        }
        double[] d = n.processData(in);
        for (double aD : d) System.out.println(aD);

        System.out.println();
        Network net = new Network(n.getDescriptor());
        for (int i = 0; i < net.generateDescriptor().length; i++) {
                System.out.print(net.getDescriptor()[i]+" ");
        }


       double []in2 = new double[net.getLayerByIndex(0).getNeuronCount()];
        for (int i = 0; i < in2.length; i++) {
            in2[i]=Math.random();
        }
        double[] d2 = n.processData(in2);
        for (double aD : d2) System.out.println(aD);

        Network net2 = new Network(net.getDescriptor());
        for (int i = 0; i < net2.generateDescriptor().length; i++) {
            System.out.print(net2.getDescriptor()[i] + " ");
        }

        double []in3 = new double[net2.getLayerByIndex(0).getNeuronCount()];
        for (int i = 0; i < in3.length; i++) {
            in3[i]=Math.random();
        }
        double[] d3 = n.processData(in3);
        for (double aD : d3) System.out.println(aD);


    }
}
