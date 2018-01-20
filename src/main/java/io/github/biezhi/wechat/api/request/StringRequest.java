package io.github.biezhi.wechat.api.request;

import io.github.biezhi.wechat.api.response.ApiResponse;

/**
 * @author biezhi
 * @date 2018/1/18
 */
public class StringRequest extends ApiRequest<StringRequest, ApiResponse> {

    public StringRequest(String url) {
        super(url, ApiResponse.class);
    }

}