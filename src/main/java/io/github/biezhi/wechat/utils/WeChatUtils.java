package io.github.biezhi.wechat.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.exception.WeChatException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信公共静态方法
 *
 * @author biezhi
 * @date 2018/1/19
 */
public class WeChatUtils {

    private static final Gson GSON        = new Gson();
    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 正则匹配
     *
     * @param p
     * @param str
     * @return
     */
    public static String match(String p, String str) {
        Pattern pattern = Pattern.compile(p);
        Matcher m       = pattern.matcher(str);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static String toPrettyJson(Object bean) {
        return PRETTY_GSON.toJson(bean);
    }

    public static String toJson(Object bean) {
        return GSON.toJson(bean);
    }

    public static <T> T fromJson(String json, Class<T> from) {
        return GSON.fromJson(json, from);
    }

    public static <T> T fromJson(String json, Type from) {
        return GSON.fromJson(json, from);
    }

    public static <T> T fromJson(FileReader fileReader, Type from) {
        return GSON.fromJson(fileReader, from);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }

    public static File saveFile(InputStream inputStream, String dirPath, String id) {
        return saveFileByDay(inputStream, dirPath, id, false);
    }

    public static File saveFileByDay(InputStream inputStream, String dirPath, String id, boolean byDay) {
        FileOutputStream fileOutputStream = null;
        try {
            if (byDay) {
                dirPath = dirPath + "/" + DateUtils.getDateString();
            }
            File dir = new File(dirPath);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            File path = new File(dir, id);
            fileOutputStream = new FileOutputStream(path);
            byte[] buffer = new byte[2048];
            int    len    = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            return path;
        } catch (Exception e) {
            throw new WeChatException(e);
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static <T> void writeJson(String file, T data) {
        try {
            FileWriter writer = new FileWriter(file);
            GSON.toJson(data, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
