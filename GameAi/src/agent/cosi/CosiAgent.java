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
        for (int i = 0; i < nets.length; i++) {
            nets[1] = new Network(nets[0].getDescriptor());
            g.addNetwork(nets[i]);
        }


    }
}
