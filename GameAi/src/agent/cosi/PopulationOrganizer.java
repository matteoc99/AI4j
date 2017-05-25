package agent.cosi;

import agent.Agent;
import game.PlayGround;
import game.Player;
import neural_network_lib.Network;
import neural_network_lib.Neuron;
import neural_network_lib.Utils;
import neural_network_lib.network_gui.NetworkGUI;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class PopulationOrganizer {
    public static double[] bestDescriptor;

    public static int Generations = 50000;
    public static int population = 1000;
    public static double bestFitness = -1;


    public static void main(String[] args) {
        NetworkGUI n = new NetworkGUI();
        for (int i = 0; i < Generations; i++) {

            CosiAgent[] agent = new CosiAgent[population];
            for (int j = 0; j < population; j++) {
                if (i == 0) {
                    //first generation
                    agent[j] = new CosiAgent(Network.createDFF(5, 1, 2, 5));
                    agent[j].setFitness(agent[j].processData(new double[]{0.5, 0.5, 0.5, 0.5, 0.5})[0]);
                } else {
                    //other generations
                    agent[j] = new CosiAgent(new Network(bestDescriptor));
                    agent[j] = new CosiAgent(new Network(Utils.mutate(agent[j].getNet().getDescriptor())));
                }
            }

            PlayGround p = new PlayGround(agent);

            for (int j = 0; j < population; j++) {
                if (agent[j].getFitness() > bestFitness) {
                    n.addNetwork(agent[j].getNet());
                    bestDescriptor = agent[j].getNet().getDescriptor();
                    bestFitness = agent[j].getFitness();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
