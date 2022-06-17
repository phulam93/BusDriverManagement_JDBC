package util;

import java.io.*;
import java.util.List;

public class FileUtil {

    // utility - tiện ích

    public static List readDataFromFile(String fileName) {
        if (StringUtil.isNullOrEmpty(fileName)) {
            return null;
        }
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
            return (List) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static <T> void writeDataToFile(List<T> data, String fileName) {
        if (StringUtil.isNullOrEmpty(fileName) || DataUtil.isEmptyCollection(data)) {
            return;
        }
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
