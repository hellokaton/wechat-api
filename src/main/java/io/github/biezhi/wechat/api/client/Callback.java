package io.github.biezhi.wechat.api.client;

import io.github.biezhi.wechat.api.request.ApiRequest;
import io.github.biezhi.wechat.api.response.ApiResponse;

import java.io.IOException;

public interface Callback<T extends ApiRequest, R extends ApiResponse> {

    void onResponse(T request, R response);

    void onFailure(T request, IOException e);

}