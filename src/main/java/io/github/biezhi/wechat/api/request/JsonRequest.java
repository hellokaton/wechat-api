package io.github.biezhi.wechat.api.request;

import io.github.biezhi.wechat.api.response.JsonResponse;

/**
 * JSON请求
 *
 * @author biezhi
 * @date 2018/1/18
 */
public class JsonRequest extends ApiRequest<JsonRequest, JsonResponse> {

    public JsonRequest(String url) {
        super(url, JsonResponse.class);
    }

}