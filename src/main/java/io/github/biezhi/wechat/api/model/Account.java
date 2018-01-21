package io.github.biezhi.wechat.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.biezhi.wechat.api.enums.AccountType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static io.github.biezhi.wechat.api.constant.Constant.API_SPECIAL_USER;

/**
 * 微信用户
 *
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class Account implements Serializable {

    @SerializedName("Uin")
    private Long uin;

    /**
     * 用户唯一标识
     */
    @SerializedName("UserName")
    private String userName;

    /**
     * 微信昵称
     */
    @SerializedName("NickName")
    private String nickName;

    /**
     * 微信头像URL
     */
    @SerializedName("HeadImgUrl")
    private String headImgUrl;

    /**
     * 备注名
     */
    @SerializedName("RemarkName")
    private String remarkName;

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

    /**
     * 性别
     */
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

    /**
     * 群成员
     */
    @SerializedName("MemberList")
    private List<Member> members;

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

    /**
     * 群id
     */
    @SerializedName("EncryChatRoomId")
    private String encryChatRoomId;

    @SerializedName("IsOwner")
    private Integer isOwner;

    /**
     * 账户类型，群、好友、公众号、特殊账号
     */
    @Expose
    private AccountType accountType;

    public AccountType getAccountType() {
        if (null != this.accountType) {
            return this.accountType;
        }
        if (verifyFlag > 0 && verifyFlag % 8 == 0) {
            this.accountType = AccountType.TYPE_MP;
        }
        if (API_SPECIAL_USER.contains(this.userName)) {
            this.accountType = AccountType.TYPE_SPECIAL;
        }
        if (this.userName.startsWith("@@")) {
            this.accountType = AccountType.TYPE_GROUP;
        }
        return AccountType.TYPE_FRIEND;
    }

}
