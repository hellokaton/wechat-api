package io.github.biezhi.wechat.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 消息体
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
    private Integer msgType;

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

    public boolean isGroup() {
        return fromUserName.contains("@@");
    }

}
