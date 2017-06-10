package agent.est;

import game.PlayGround;
import neural_network_lib.Network;
import neural_network_lib.Utils;

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

    private void init() {
        // create startNetworks
        for (int i = 0; i < amountOfSurvivors; i++) {
            agents.add(new EstAgent(Network.createDFF(5,2,1,5)));
        }

        for (;;) {
            processAgents();
            pickAgents();
            cloneAndMutateAgents();
        }
    }

    private void processAgents() {
        EstAgent[] agentsArray = new EstAgent[agents.size()];
        agents.toArray(agentsArray);
        new PlayGround(null, agentsArray);
        while (!PlayGround.trainingOver) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void pickAgents() {
        agents.sort((o1, o2) -> o1.getFitness()<o2.getFitness()? 1:-1);
        int size = agents.size();
        for (int i = size-1; i >= amountOfSurvivors; i--) {
            agents.remove(i);
        }
        System.out.println(agents.get(0).getFitness());
    }

    private void cloneAndMutateAgents() {
        ArrayList<EstAgent> newAgents = new ArrayList<>();
        for (EstAgent agent : agents) {
            newAgents.add(new EstAgent(new Network(agent.getNet().getDescriptor())));
        }
        for (int survivorIndex = 0; survivorIndex < amountOfSurvivors; survivorIndex++) {
            double[] descriptor = agents.get(survivorIndex).getNet().getDescriptor();
            for (int childNumber = 0; childNumber<amountOfChildren/amountOfSurvivors; childNumber++) {
                newAgents.add(new EstAgent(new Network(Utils.mutate(descriptor))));
            }
        }
        agents = newAgents;
    }
}
