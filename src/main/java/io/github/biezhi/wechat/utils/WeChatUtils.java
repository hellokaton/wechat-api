package io.github.biezhi.wechat.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.exception.WeChatException;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信公共静态方法
 *
 * @author biezhi
 * @date 2018/1/19
 */
public class WeChatUtils {

    private static final Gson gson = new Gson();

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

    public static String fileMd5(File file) {
        InputStream       is  = null;
        DigestInputStream dis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            is = new FileInputStream(file);
            dis = new DigestInputStream(is, md);
            byte[] digest = md.digest();
            return new String(digest, "UTF-8");
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (null != dis) {
                    dis.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (Exception e) {

            }
        }
    }

    public static byte[] fileToBytes(File file) {
        byte[]                b           = new byte[1024];
        InputStream           inputStream = null;
        ByteArrayOutputStream os          = null;
        try {
            inputStream = new FileInputStream(file);
            os = new ByteArrayOutputStream();
            int c;
            while ((c = inputStream.read(b)) != -1) {
                os.write(b, 0, c);
            }
            return os.toByteArray();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if(null != os){
                    os.close();
                }
                if(null != inputStream){
                    inputStream.close();
                }
            } catch (Exception e){

            }
        }
    }
}
