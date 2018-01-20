package io.github.biezhi.wechat.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author biezhi
 * @date 2018/1/20
 */
@Data
public class RecommendInfo {

    @SerializedName("UserName")
    private String userName;

    @SerializedName("NickName")
    private String nickName;

    @SerializedName("QQNum")
    private Integer qqNum;

    @SerializedName("Province")
    private String province;

    @SerializedName("City")
    private String city;

    @SerializedName("Content")
    private String content;

    @SerializedName("Signature")
    private String signature;

    @SerializedName("Alias")
    private String alias;

    @SerializedName("Scene")
    private Integer scene;

    @SerializedName("VerifyFlag")
    private Integer verifyFlag;

    @SerializedName("AttrStatus")
    private Integer attrStatus;

    @SerializedName("Sex")
    private Integer sex;

    @SerializedName("Ticket")
    private String ticket;

    @SerializedName("OpCode")
    private Integer opCode;

}
