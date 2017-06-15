package io.github.biezhi.wechat.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author biezhi
 *         15/06/2017
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * obj转json
     */
    public static String toJson(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            log.error("Json序列化失败[系统异常]", e);
        }
        return null;
    }

    /**
     * json转obj
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return OBJECT_MAPPER.readValue(json, classOfT);
        } catch (Exception e) {
            log.error("Json反序列化失败[系统异常]", e);
        }
        return null;
    }

    /**
     * json转obj
     */
    public static <T> T fromJson(String json, TypeReference<T> typeOfT) {
        try {
            return OBJECT_MAPPER.readValue(json, typeOfT);
        } catch (Exception e) {
            log.error("Json反序列化失败[系统异常]", e);
        }
        return null;
    }

}
