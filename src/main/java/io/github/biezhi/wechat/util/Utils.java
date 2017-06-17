package io.github.biezhi.wechat.util;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.HttpURLConnection;
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

    private Utils() {
    }

    public static Map<String, Object> createMap(Object... values) {
        Map<String, Object> map = new HashMap<String, Object>(values.length / 2);
        for (int i = 0; i < values.length; i++) {
            map.put(values[i].toString(), values[++i]);
        }
        return map;
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

    public static String getCookie(HttpRequest request) {
        HttpURLConnection conn = request.getConnection();
        Map<String, List<String>> resHeaders = conn.getHeaderFields();
        StringBuffer sBuffer = new StringBuffer();
        for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
            String name = entry.getKey();
            if (name == null)
                continue; // http/1.1 line
            List<String> values = entry.getValue();
            if (name.equalsIgnoreCase("Set-Cookie")) {
                for (String value : values) {
                    if (value == null) {
                        continue;
                    }
                    String cookie = value.substring(0, value.indexOf(";") + 1);
                    sBuffer.append(cookie);
                }
            }
        }
        if (sBuffer.length() > 0) {
            return sBuffer.toString();
        }
        return sBuffer.toString();
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
            return new Gson().toJson(o);
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
            return new Gson().fromJson(json, classOfT);
        } catch (Exception e) {
            log.error("Json反序列化失败", e);
        }
        return null;
    }

}
