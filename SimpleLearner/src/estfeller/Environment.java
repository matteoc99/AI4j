package estfeller;

/**
 * @author Maximilian Estfelller
 * @since 09.06.2017
 */
public class Environment {

    final int environmentLength = 8;

    double[] data;

    void createData() {
        data = new double[environmentLength];
        for (int i = 0; i < data.length; i++) {
            data[i] = Math.random()*2-1;
        }
    }

    public double[] getData() {
        return data;
    }


    int evaluateActions(double[] originalInput, double[] actions) {
        if (actions.length != originalInput.length)
            throw new RuntimeException("NotCompatible Network used");

        // A Network can get up to 200 points for each action
        int points = 0;
        for (int i = 0; i < actions.length; i++) {
            double dif = Math.abs(actions[i]-originalInput[originalInput.length-i-1]);
            points+=200-dif*100;
        }
        return points;
    }
}
