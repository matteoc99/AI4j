package utils.storage;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maximilian Estfeller
 * @since 27.08.2017
 */
public class DataRepository {

    private static Map<String, List<String>> repositoryFiles = new HashMap<>();

    private String repositoryPath;

    public DataRepository(String repositoryPath) {
        this.repositoryPath = repositoryPath;

        repositoryFiles.put(repositoryPath, new ArrayList<>());
    }

    public void print(String fileName, Serializable obj) throws DataException {
        DataIOs.print(repositoryPath+"\\"+fileName, obj);

        if (!repositoryFiles.get(repositoryPath).contains(fileName))
            repositoryFiles.get(repositoryPath).add(fileName);
    }

    public Object read(String fileName) throws DataException {
        return DataIOs.read(repositoryPath+"\\"+fileName);
    }

    /**
     * removes this repository from the list, and deletes all of it's files
     */
    public void deleteRepository() {
        clearRepository();
        repositoryFiles.remove(repositoryPath);
    }

    /**
     * Deletes all Files from this repository
     */
    public void clearRepository() {
        repositoryFiles.get(repositoryPath).removeIf(s -> {
            try {
                deleteFile(s);
            } catch (DataException e) {
                System.out.println(e.getMessage());
                return false;
            }
            return true;
        });
    }

    /**
     * removes the given File from the list and deletes it
     *
     * @param fileName path of the File to delete
     * @throws DataException when deletion fails
     */
    public void deleteFile(String fileName) throws DataException {
        if (!new File(repositoryPath+"\\"+fileName).delete())
            throw new DataException("Can't delete File: "+fileName);
        else
            repositoryFiles.get(repositoryPath).remove(fileName);
    }

    static class DataException extends Exception {
        DataException(String msg) {
            super(msg);
        }

        DataException(Exception e) {
            super(e.getMessage());
        }
    }
}
