
package io.github.biezhi.wechat.request;

import io.github.biezhi.wechat.constant.Constant;
import io.github.biezhi.wechat.constant.ContentTypes;
import io.github.biezhi.wechat.response.ApiResponse;
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

    protected String  url;
    protected boolean noRedirect;
    protected boolean jsonBody;
    protected String method      = "GET";
    protected String contentType = ContentTypes.GENERAL_MIME_TYPE;
    protected int    timeout     = 10;
    protected Headers headers;
    @SuppressWarnings("unchecked")
    protected final T thisAsT = (T) this;
    private final Class<? extends R>  responseClass;
    private final Map<String, Object> parameters;

    public ApiRequest(String url, Class<? extends R> responseClass) {
        this.url = url;
        this.responseClass = responseClass;
        this.parameters = new HashMap<String, Object>();
        this.headers = Headers.of("User-Agent", Constant.USER_AGENT, "ContentType", this.contentType);
    }

    public T headers(String... nameValues) {
        this.headers = Headers.of(nameValues);
        return thisAsT;
    }

    public T header(String name, String value) {
        this.headers = this.headers.newBuilder().set(name, value).build();
        return thisAsT;
    }

    public T add(String name, Object val) {
        parameters.put(name, val);
        return thisAsT;
    }

    public T addAll(Map<String, Object> values) {
        parameters.putAll(values);
        return thisAsT;
    }

    public T noRedirect() {
        this.noRedirect = true;
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

    public T jsonBody() {
        this.jsonBody = true;
        this.contentType = ContentTypes.GENERAL_JSON_TYPE;
        this.header("ContentType", this.contentType);
        return thisAsT;
    }

    public boolean isMultipart() {
        return false;
    }

    public String getFileName() {
        return ContentTypes.GENERAL_FILE_NAME;
    }

    public T post() {
        this.method = "POST";
        return thisAsT;
    }


}