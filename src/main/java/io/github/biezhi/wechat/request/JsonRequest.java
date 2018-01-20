package io.github.biezhi.wechat.request;

import io.github.biezhi.wechat.response.JsonResponse;

/**
 * @author biezhi
 * @date 2018/1/18
 */
public class JsonRequest extends ApiRequest<JsonRequest, JsonResponse> {

    public JsonRequest(String url) {
        super(url, JsonResponse.class);
    }

}