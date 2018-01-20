package io.github.biezhi.wechat.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.exception.WeChatException;
import io.github.biezhi.wechat.api.model.User;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author biezhi
 * @date 2018/1/19
 */
public class WeChatUtils {

    private static final Gson gson = new Gson();

    public static User searchDictList(List<User> chatRooms, String key, String name) {
        return chatRooms.get(0);
    }

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

    public static String toJson(Object bean) {
        return gson.toJson(bean);
    }

    public static <T> T fromJson(String json, Class<T> from) {
        return gson.fromJson(json, from);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }

    public static String escapeHTML(String s) {
        String escapedHtml = s
                .replaceAll("&", "&amp;")
                .replaceAll("\"", "&quot;")
                .replaceAll("\"", "&quot;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
        return escapedHtml;
    }

    public static File saveFile(InputStream inputStream, String dirPath, String id) {
        FileOutputStream fileOutputStream = null;
        try {
            File dir = new File(dirPath + "/" + DateUtils.getDateString());
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

    public static void localOpen(String url) {
        try {
            Desktop.getDesktop().open(new File(new URI(url)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
