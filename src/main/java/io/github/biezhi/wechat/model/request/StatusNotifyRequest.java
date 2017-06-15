package io.github.biezhi.wechat.model.request;

import java.io.Serializable;

/**
 * @author biezhi
 *         15/06/2017
 */
public class StatusNotifyRequest implements Serializable {

    private BaseRequest BaseRequest;
    private int Code;
    private String FromUserName;
    private String ToUserName;
    private long ClientMsgId;

    public BaseRequest getBaseRequest() {
        return BaseRequest;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        BaseRequest = baseRequest;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public long getClientMsgId() {
        return ClientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        ClientMsgId = clientMsgId;
    }
}
