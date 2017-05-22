package agent.cosi;

import agent.Agent;
import neural_network_lib.Network;
import neural_network_lib.Neuron;
import neural_network_lib.network_gui.NetworkGUI;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class CosiAgent extends Agent {

    public static void main(String[] args) {
        NetworkGUI g = new NetworkGUI();

        Network[] nets = new Network[30];
        nets[0] = Network.createDFF(5, 7, 2, 3);
        g.addNetwork(nets[0]);
        for (int i = 1; i < nets.length; i++) {

            System.out.println();
            nets[i] = new Network(nets[i - 1].getDescriptor());
            for (int j = 0; j < nets[i].getDescriptor().length; j++) {
                System.out.print(nets[i - 1].getDescriptor()[j] + " ");
            }
            System.out.println();
            double[] data = nets[i].processData(new double[]{0.8, 0.5, 0.8, 0.6, 0.2});
            for (int j = 0; j < data.length; j++) {
                System.out.print(data[j] + " ");
            }
            System.out.println();
            g.addNetwork(nets[i]);
        }


    }
}
