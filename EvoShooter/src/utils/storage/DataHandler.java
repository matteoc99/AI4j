package utils.storage;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public abstract class DataHandler {

    void print(String path, Serializable data) {
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

    Object read(String path) {
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

    private static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }
}
