package utils.storage;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * @author Maximilian Estfeller
 * @since 23.08.2017
 *
 * This class consists exclusively of static methods that help printing and reading serialized Objects
 */
final class DataIOs {

    private DataIOs() {}

    /**
     * Writes a File to the given Path
     *
     * Path get validated first
     * @see #validatePath(String)
     *
     * @param path to the File
     * @param data Object to print
     * @throws DataRepository.DataException validation failed, IOException
     */
    static void print(String path, Serializable data) throws DataRepository.DataException {
        validatePath(path);

        File file = new File(path);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(file));

            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            throw new DataRepository.DataException(e);
        }
    }

    /**
     * Reads a File from the given Path
     *
     * Path validation first
     * @see #validatePath(String)
     * @param path to the File
     * @return an Object
     * @throws DataRepository.DataException validation failed, IOException
     */
    static Object read(String path) throws DataRepository.DataException {
        validatePath(path);

        File file = new File(path);

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DataRepository.DataException(e);
        }
    }

    /**
     * Method validates the path
     * @param path to the File
     * @throws DataRepository.DataException not a path, or not a serialized Object
     */
    private static void validatePath(String path) throws DataRepository.DataException {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            throw new DataRepository.DataException(ex);
        }
        if (!path.endsWith(".ser"))
            throw new DataRepository.DataException("DataFiles need to end in .ser");
    }
}
