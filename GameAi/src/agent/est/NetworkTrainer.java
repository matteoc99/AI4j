package agent.est;

import neural_network_lib.Network;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Maximilian Estfelller
 * @since 26.05.2017
 */
public class NetworkTrainer {

    final int amountOfChildren = 100;
    final int amountOfSurvivers = 5;

    private ArrayList<EstAgent> agents = new ArrayList<>();

    public static void main(String[] args) {
        new NetworkTrainer();
    }

    void init() {
        // create startNetworks
        for (int i = 0; i < amountOfSurvivers; i++) {
            agents.add(new EstAgent(Network.createDFF(5,2,2,5)));
        }
    }

    void pickNetworks() {
        agents.sort(new Comparator<EstAgent>() {
            @Override
            public int compare(EstAgent o1, EstAgent o2) {
                return o1.getFitness()>=o2.getFitness()? 1:-1;
            }
        });
        for (int i = amountOfSurvivers; i < agents.size(); i++) {
            agents.remove(i);
        }
    }

    void cloneAgents() {
        for (int i = 0; i < amountOfChildren/amountOfSurvivers; i++) {
            double[] descriptor = agents.get(i).getNet().getDescriptor();

        }

        /*
        double[] descriptor = network.getDescriptor();
        Network[] ret = new Network[amountOfChildren];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new Network(descriptor);
        }
        return ret;
        */
    }
}
