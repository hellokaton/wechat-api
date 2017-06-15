package io.github.biezhi.wechat.model.entity;

import java.io.Serializable;

/**
 * @author biezhi
 *         15/06/2017
 */
public class AddMessage implements Serializable {

    private Integer MsgType;
    private String FromUserName;
    private String ToUserName;
    private String Content;
    private String MsgId;

    public Integer getMsgType() {
        return MsgType;
    }

    public void setMsgType(Integer msgType) {
        MsgType = msgType;
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

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}
