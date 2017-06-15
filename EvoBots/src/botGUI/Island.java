package botGUI;

/**
 * Created by mcosi on 14/06/2017.
 */
public class Island {
    public int[][] island;
    //Coordinates for placing
    public int x;
    public int y;

    public Island(int size, int x, int y) {
        this.x = x-size/2-1;
        this.y = y-size/2-1;
        island = new int[size][size];
        int type = (int) (Math.random() * 6);

        System.out.println(type);
        switch (type) {
            case 0:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        island[i][j] = 0;
                        if (i * j >= Math.random() * size) {
                            island[i][j] = 1;
                        }
                    }
                }
                break;

            case 1:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        island[i][j] = 0;
                        if (i + j < Math.random() * size * 2) {
                            island[i][j] = 1;
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        island[i][j] = 0;
                        if (i * j >= Math.random() * size && i > j) {
                            island[i][j] = 1;
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        island[i][j] = 0;
                        if (i * j >= Math.random() * size && i > j) {
                            island[i][j] = 1;
                        }
                    }
                }
                break;
            default:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        island[i][j] = 0;
                        if (i + j < Math.random() * size * 2) {
                            island[i][j] = 1;
                        }
                    }
                }
        }

    }
}
