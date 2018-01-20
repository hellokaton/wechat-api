package io.github.biezhi.wechat.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Profile 信息
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
public class Profile {

    @SerializedName("BitFlag")
    private Integer bitFlag;

    @SerializedName("UserName")
    private UserName userName;

    @SerializedName("NickName")
    private UserName nickName;

    @SerializedName("BindUin")
    private Integer bindUin;

    @SerializedName("BindEmail")
    private UserName bindEmail;

    @SerializedName("BindMobile")
    private UserName bindMobile;

    @SerializedName("Status")
    private Integer status;

    @SerializedName("Sex")
    private Integer sex;

    @SerializedName("PersonalCard")
    private Integer personalCard;

    @SerializedName("Alias")
    private String alias;

    @SerializedName("HeadImgUpdateFlag")
    private Integer headImgUpdateFlag;

    @SerializedName("HeadImgUrl")
    private String headImgUrl;

    @SerializedName("Signature")
    private String signature;

    @Data
    static class UserName {
        @SerializedName("Buff")
        String buff;
    }

}
