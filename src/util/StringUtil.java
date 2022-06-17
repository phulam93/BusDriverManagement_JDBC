package util;

public class StringUtil {

    public static boolean isNullOrEmpty(String str) {
        return DataUtil.isNull(str) || str.trim().equals("");
    }

}
