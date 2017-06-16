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

    public static String doRequest(String url, String cookie, String bodyJson) {

        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", cookie).send(bodyJson);

        log.info("发送请求: {}", request);

        String body = request.body();
        request.disconnect();
        return body;
    }

    public static String doRequest(String url, String cookie, Object object) {

        String bodyJson = JsonUtils.toJson(object);

        log.info("微信初始化请求URL: {}", url);
        log.info("发送参数: {}", bodyJson);

        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8")
                .header("Cookie", cookie).send(bodyJson);
        String body = request.body();
        request.disconnect();
        return body;
    }

}
