package io.github.biezhi.wechat.util;

/**
 * @author biezhi
 *         15/06/2017
 */
public class StringKit {

    public static boolean isBlank(String str) {
        return null == str || "".equals(str.trim());
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String getRandomNumber(int i) {
        return "";
    }

}
