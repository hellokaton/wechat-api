package io.github.biezhi.wechat.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class User {

    @SerializedName("Uin")
    private Long uin;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("NickName")
    private String nickName;

    @SerializedName("HeadImgUrl")
    private String headImgUrl;

    @SerializedName("RemarkName")
    private String remarkName;

    @SerializedName("MemberList")
    private List<Member> members;

    @SerializedName("PYInitial")
    private String pyInitial;

    @SerializedName("PYQuanPin")
    private String pyQuanPin;

    @SerializedName("RemarkPYInitial")
    private String remarkPYInitial;

    @SerializedName("RemarkPYQuanPin")
    private String remarkPYQuanPin;

    @SerializedName("HideInputBarFlag")
    private Integer iideInputBarFlag;

    @SerializedName("StarFriend")
    private Integer starFriend;

    @SerializedName("Sex")
    private Integer sex;

    @SerializedName("Signature")
    private String signature;

    @SerializedName("AppAccountFlag")
    private Integer appAccountFlag;

    @SerializedName("VerifyFlag")
    private Integer verifyFlag;

    @SerializedName("ContactFlag")
    private Integer contactFlag;

    @SerializedName("WebWxPluginSwitch")
    private Integer webWxPluginSwitch;

    @SerializedName("HeadImgFlag")
    private Integer headImgFlag;

    @SerializedName("SnsFlag")
    private Integer snsFlag;

    ///////////////////群聊相关/////////////////////

    @SerializedName("UniFriend")
    private Integer uniFriend;

    @SerializedName("DisplayName")
    private String displayName;

    @SerializedName("ChatRoomOwner")
    private String chatRoomOwner;

    @SerializedName("OwnerUin")
    private Long ownerUin;

    @SerializedName("ChatRoomId")
    private Long chatRoomId;

    @SerializedName("EncryChatRoomId")
    private String encryChatRoomId;

    @SerializedName("IsOwner")
    private Integer isOwner;
}
