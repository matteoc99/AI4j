package agent.est;

import game.PlayGround;
import network.Network;

import java.util.ArrayList;


/**
 * @author Maximilian Estfelller
 * @since 26.05.2017
 */
public class NetworkTrainer {

    private final int amountOfChildren = 1;
    private final int amountOfSurvivors = 1;

    private ArrayList<EstAgent> agents = new ArrayList<>();

    public static void main(String[] args) {
        new NetworkTrainer().init();
    }

    private void init() {
        // create startNetworks
        for (int i = 0; i < amountOfSurvivors; i++) {
            agents.add(new EstAgent(Network.createDFF(2,1,0,0)));
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
        //for (EstAgent agent : agents) {
        //    newAgents.add(new EstAgent(new Network(agent.getNet().getDescriptor())));
        //}
        for (int survivorIndex = 0; survivorIndex < amountOfSurvivors; survivorIndex++) {
            double[] descriptor = agents.get(survivorIndex).getNet().getDescriptor();
            for (int childNumber = 0; childNumber<amountOfChildren/amountOfSurvivors; childNumber++) {
                double[] descClone = descriptor.clone();
                Utils.mutateDescriptor(descClone);
                newAgents.add(new EstAgent(new Network(descClone)));
            }
        }
        agents = newAgents;
    }
}
