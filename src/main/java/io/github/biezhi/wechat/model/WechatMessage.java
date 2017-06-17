package io.github.biezhi.wechat.model;

import com.google.gson.JsonObject;
import io.github.biezhi.wechat.api.WechatApi;

/**
 * @author biezhi
 *         17/06/2017
 */
public class WechatMessage {

    private JsonObject rawMsg;
    private String location;
    private String log;
    private String text;

    private WechatApi wechatApi;

    public JsonObject getRawMsg() {
        return rawMsg;
    }

    public void setRawMsg(JsonObject rawMsg) {
        this.rawMsg = rawMsg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public WechatApi getWechatApi() {
        return wechatApi;
    }

    public void setWechatApi(WechatApi wechatApi) {
        this.wechatApi = wechatApi;
    }

    @Override
    public String toString() {
        return "WechatMessage(" +
                "location='" + location + '\'' +
                ", log='" + log + '\'' +
                ", text='" + text + '\'' +
                ')';
    }
}
