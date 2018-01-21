package io.github.biezhi.wechat.api.model;

import com.google.gson.annotations.SerializedName;
import io.github.biezhi.wechat.api.enums.MsgType;
import lombok.Data;

/**
 * 微信原始消息体
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
public class Message {

    @SerializedName("MsgId")
    private String id;

    @SerializedName("FromUserName")
    private String fromUserName;

    @SerializedName("ToUserName")
    private String toUserName;

    @SerializedName("MsgType")
    private Integer type;

    @SerializedName("Content")
    private String content;

    @SerializedName("Status")
    private Integer status;

    @SerializedName("ImgStatus")
    private Integer imgStatus;

    @SerializedName("CreateTime")
    private Long createTime;

    @SerializedName("VoiceLength")
    private Long voiceLength;

    @SerializedName("PlayLength")
    private Long playLength;

    @SerializedName("FileName")
    private String fileName;

    @SerializedName("FileSize")
    private String fileSize;

    @SerializedName("MediaId")
    private String mediaId;

    @SerializedName("Url")
    private String url;

    @SerializedName("AppMsgType")
    private Integer appMsgType;

    @SerializedName("StatusNotifyCode")
    private Integer statusNotifyCode;

    @SerializedName("StatusNotifyUserName")
    private String statusNotifyUserName;

    @SerializedName("RecommendInfo")
    private Recommend recommend;

    @SerializedName("ForwardFlag")
    private Integer forwardFlag;

    @SerializedName("AppInfo")
    private AppInfo appInfo;

    @SerializedName("HasProductId")
    private Integer hasProductId;

    @SerializedName("Ticket")
    private String ticket;

    @SerializedName("ImgHeight")
    private Integer imgHeight;

    @SerializedName("ImgWidth")
    private Integer imgWidth;

    @SerializedName("SubMsgType")
    private Integer subMsgType;

    @SerializedName("NewMsgId")
    private Long newMsgId;

    @SerializedName("OriContent")
    private String oriContent;

    @SerializedName("EncryFileName")
    private String encryFileName;

    /**
     * 是否是群聊消息
     *
     * @return 返回是否是群组消息
     */
    public boolean isGroup() {
        return fromUserName.contains("@@") || toUserName.contains("@@");
    }

    public MsgType msgType() {
        switch (this.type) {
            case 1:
                return MsgType.TEXT;
            case 3:
                return MsgType.IMAGE;
            case 34:
                return MsgType.VOICE;
            case 37:
                return MsgType.ADD_FRIEND;
            case 42:
                return MsgType.PERSON_CARD;
            case 43:
                return MsgType.VIDEO;
            case 47:
                return MsgType.EMOTICONS;
            case 49:
                return MsgType.SHARE;
            case 51:
                return MsgType.CONTACT_INIT;
            case 62:
                return MsgType.VIDEO;
            case 10000:
                return MsgType.SYSTEM;
            case 10002:
                return MsgType.REVOKE_MSG;
            default:
                return MsgType.UNKNOWN;
        }
    }
}
