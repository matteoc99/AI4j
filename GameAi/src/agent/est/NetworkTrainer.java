package agent.est;

import game.PlayGround;
import neural_network_lib.Network;

import java.util.ArrayList;


/**
 * @author Maximilian Estfelller
 * @since 26.05.2017
 */
public class NetworkTrainer {

    final int amountOfChildren = 100;
    final int amountOfSurvivors = 5;

    private ArrayList<EstAgent> agents = new ArrayList<>();

    public static void main(String[] args) {
        new NetworkTrainer().init();
    }

    void init() {
        // create startNetworks
        for (int i = 0; i < amountOfSurvivors; i++) {
            agents.add(new EstAgent(Network.createDFF(5,2,2,5)));
        }
        for (;;) {
            processNetworks();
            pickNetworks();
            cloneAgents();
        }
    }

    void processNetworks() {
        EstAgent[] agentsArray = new EstAgent[agents.size()];
        agents.toArray(agentsArray);
        new PlayGround(null, agentsArray);
    }

    void pickNetworks() {
        agents.sort((o1, o2) -> o1.getFitness()>=o2.getFitness()? 1:-1);
        for (int i = amountOfSurvivors; i < agents.size(); i++) {
            agents.remove(i);
        }
    }

    void cloneAgents() {
        ArrayList<EstAgent> newAgents = new ArrayList<>();
        for (int survivorIndex = 0; survivorIndex < amountOfSurvivors; survivorIndex++) {
            double[] descriptor = agents.get(survivorIndex).getNet().getDescriptor();
            for (int childNumber = 0; childNumber<amountOfChildren/amountOfSurvivors; childNumber++) {
                newAgents.add(new EstAgent(new Network(descriptor)));
            }
        }
        agents = newAgents;
    }
}
