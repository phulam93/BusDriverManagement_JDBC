package util;

import java.util.List;

public class DataUtil {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static <T> boolean isEmptyCollection(List<T> lst) {
        return isNull(lst) || lst.isEmpty();
    }

}
