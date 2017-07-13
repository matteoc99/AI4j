import network.Network;

/**
 * @author Matteo Cosi
 * @since 13.07.2017
 */
public class Main {


    /**
     * x1,x2,y*
     * x1...temperatur drausen
     * x2...menschen im raum
     * y*...benötigter heizstrom
     */
    public static double[][] data = {{-5,5}, {0.3,0.2}, {0.4,0.5}, {-0.05,0}};
    public static double[][] expected = {{0.8}, {0.15}, {0.05}, {1.0}};
    public static void main(String[] args) {

        Network net = Network.createDFF(2,1,1,2);
        double error=1;
        while(error>0.05){
            error=0;
            for (int i = 0; i < data.length; i++) {
                double y=net.processData(data[i])[0];
                net.propagateBack(y-expected[i][0]);
            }
        }
    }
}
