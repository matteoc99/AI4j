package utils.storage;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * @author Maximilian Estfeller
 * @since 23.08.2017
 */
public final class DataHandlers {

    private DataHandlers() {}

    public static void print(String path, Serializable data) {
        if (!isValidPath(path)) return;

        File file = new File(path);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(file));

            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object read(String path) {
        if (!isValidPath(path)) return null;

        File file = new File(path);
        if (!file.exists()) return null;

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }
}
