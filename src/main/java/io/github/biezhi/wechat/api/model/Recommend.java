package io.github.biezhi.wechat.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 个人名片信息
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
public class Recommend {

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
