package io.github.biezhi.wechat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author biezhi
 *         17/06/2017
 */
public final class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private Utils() {
    }

    public static Map<String, Object> createMap(Object... values) {
        Map<String, Object> map = new HashMap<String, Object>(values.length / 2);
        for (int i = 0; i < values.length; i++) {
            map.put(values[i].toString(), values[++i]);
        }
        return map;
    }

    public static String emptyOr(String str1, String str2) {
        if (isBlank(str1)) {
            return str2;
        }
        return str1;
    }

    public static void sleep(long timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (Exception e) {

        }
    }

    public static String match(String p, String str) {
        Pattern pattern = Pattern.compile(p);
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isBlank(String str) {
        return null == str || "".equals(str.trim());
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String getRandomNumber(int size) {
        String num = "";
        for (int i = 0; i < size; i++) {
            double a = Math.random() * 9;
            a = Math.ceil(a);
            int randomNum = new Double(a).intValue();
            num += randomNum;
        }
        return num;
    }

    /**
     * obj转json
     */
    public static String toJson(Object o) {
        try {
            return gson.toJson(o);
        } catch (Exception e) {
            log.error("Json序列化失败", e);
        }
        return null;
    }

    /**
     * json转obj
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            log.error("Json反序列化失败", e);
        }
        return null;
    }

    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
            if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                //英文及数字等
                sb.append(myBuffer[i]);
            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                //全角半角字符
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                //汉字
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    public static String unicodeToUtf8(String string) {
        try {
            if (string.indexOf("\\u") == -1) {
                return string;
            }
            byte[] utf8 = string.getBytes("UTF-8");
            // Convert from UTF-8 to Unicode
            string = new String(utf8, "UTF-8");
            return string;
        } catch (UnsupportedEncodingException e) {
        }
        return string;
    }

    public static String getCookie(List<String> cookies) {
        StringBuilder sBuffer = new StringBuilder();
        for (String value : cookies) {
            if (value == null) {
                continue;
            }
            String cookie = value.substring(0, value.indexOf(";") + 1);
            sBuffer.append(cookie);
        }
        return sBuffer.toString();
    }
}
