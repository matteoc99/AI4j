package botGUI;

/**
 * Created by mcosi on 14/06/2017.
 */
public class Island {
    public int [][] island;
    //Coordinates for placing
    public int x;
    public int y;
    public Island(int size,int x,int y) {
        this.x=x;
        this.y=y;
        island = new int [size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                island[i][j]=0;
                if (i*j+2>=(Math.random()*size*(size/2))){
                    island[i][j]=1;
                }
            }
        }
    }
}
