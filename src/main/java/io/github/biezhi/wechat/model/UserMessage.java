package io.github.biezhi.wechat.model;

import com.google.gson.JsonObject;
import io.github.biezhi.wechat.api.WechatApi;

/**
 * 用户消息
 *
 * @author biezhi
 * 17/06/2017
 */
public class UserMessage {

    // 元消息
    private JsonObject rawMsg;

    // 位置信息
    private String location;

    // 消息id
    private String msgId;

    // 消息类型
    private String msgType;

    private String log;

    // 文本消息内容
    private String text;

    // 谁发的
    private String fromUserName;

    // 发给谁
    private String toUserName;

    private WechatApi wechatApi;

    public UserMessage(WechatApi wechatApi) {
        this.wechatApi = wechatApi;
    }

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
        return "UserMessage(" +
                "location='" + location + '\'' +
                ", log='" + log + '\'' +
                ", text='" + text + '\'' +
                ')';
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public boolean isEmpty() {
        return null == text || null == rawMsg;
    }

    public void sendText(String msg, String uid) {
        wechatApi.sendText(msg, uid);
    }

}
