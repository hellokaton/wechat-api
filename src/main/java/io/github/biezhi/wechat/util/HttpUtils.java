package io.github.biezhi.wechat.util;

import com.github.kevinsawicki.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author biezhi
 *         16/06/2017
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static <T> T doRequest(String url, String cookie, Object object, Class<T> responseType) {

        String bodyJson = JsonUtils.toJson(object);
        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", cookie).send(bodyJson);
        log.info("请求: {}", request);
        String body = request.body();
        log.info("响应: {}", body);

        request.disconnect();
        if (null != responseType) {
            return JsonUtils.fromJson(body, responseType);
        }
        return null;
    }

}
