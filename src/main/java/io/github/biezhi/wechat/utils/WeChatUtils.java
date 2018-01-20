package io.github.biezhi.wechat.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.biezhi.wechat.model.ChatRoom;
import io.github.biezhi.wechat.model.User;

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

}
