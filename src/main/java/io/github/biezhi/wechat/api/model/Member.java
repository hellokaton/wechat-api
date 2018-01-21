package io.github.biezhi.wechat.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 群成员
 *
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class Member {

    @SerializedName("Uin")
    private Long uin;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("NickName")
    private String nickName;

    @SerializedName("DisplayName")
    private String displayName;

    @SerializedName("RemarkName")
    private String remarkName;

    @SerializedName("AttrStatus")
    private Long attrStatus;

    @SerializedName("MemberStatus")
    private Integer memberStatus;

    @SerializedName("KeyWord")
    private String keyWord;

}
