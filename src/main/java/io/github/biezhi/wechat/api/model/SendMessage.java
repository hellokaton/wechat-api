package io.github.biezhi.wechat.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送消息体
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessage {

    @SerializedName("Type")
    private Integer type;

    @SerializedName("Content")
    private String content;

    @SerializedName("FromUserName")
    private String fromUserName;

    @SerializedName("ToUserName")
    private String toUserName;

    @SerializedName("LocalID")
    private String localID;

    @SerializedName("ClientMsgId")
    private String clientMsgId;

}
