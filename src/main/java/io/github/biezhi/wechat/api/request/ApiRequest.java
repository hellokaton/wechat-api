
package io.github.biezhi.wechat.api.request;

import io.github.biezhi.wechat.api.constant.Constant;
import io.github.biezhi.wechat.api.response.ApiResponse;
import lombok.Getter;
import okhttp3.Headers;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础请求
 *
 * @author biezhi
 */
@Getter
public abstract class ApiRequest<T extends ApiRequest, R extends ApiResponse> {

    protected int timeout = 10;
    protected boolean noRedirect;
    protected boolean jsonBody;
    protected boolean multipart;

    protected String url;
    protected String method = "GET";
    protected String fileName;
    protected String contentType = "application/x-www-form-urlencoded";
    protected Headers headers;
    @SuppressWarnings("unchecked")
    protected final T thisAsT = (T) this;
    private final Class<? extends R>  responseClass;
    private final Map<String, Object> parameters;

    public ApiRequest(String url, Class<? extends R> responseClass) {
        this.url = url;
        this.responseClass = responseClass;
        this.parameters = new HashMap<>();
        this.headers = Headers.of("User-Agent", Constant.USER_AGENT, "Content-Type", this.contentType);
    }

    public T header(String name, String value) {
        this.headers = this.headers.newBuilder().set(name, value).build();
        return thisAsT;
    }

    public T add(String name, Object val) {
        parameters.put(name, val);
        return thisAsT;
    }

    public T noRedirect() {
        this.noRedirect = true;
        return thisAsT;
    }

    public T multipart() {
        this.multipart = true;
        return thisAsT;
    }

    public Type getResponseType() {
        return responseClass;
    }

    public T url(String url) {
        this.url = url;
        return thisAsT;
    }

    public T timeout(int seconds) {
        this.timeout = seconds;
        return thisAsT;
    }

    public T fileName(String fileName) {
        this.fileName = fileName;
        return thisAsT;
    }

    public T post() {
        this.method = "POST";
        return thisAsT;
    }

    public T jsonBody() {
        this.jsonBody = true;
        this.contentType = "application/json; charset=UTF-8";
        this.header("Content-Type", this.contentType);
        return thisAsT;
    }

}