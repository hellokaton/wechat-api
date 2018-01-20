package io.github.biezhi.wechat.callback;

import io.github.biezhi.wechat.request.ApiRequest;
import io.github.biezhi.wechat.response.ApiResponse;

import java.io.IOException;

public interface Callback<T extends ApiRequest, R extends ApiResponse> {

    void onResponse(T request, R response);

    void onFailure(T request, IOException e);

}