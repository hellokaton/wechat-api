package io.github.biezhi.wechat.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailMember {

    @SerializedName("UserName")
    private String userName;

    @SerializedName("EncryChatRoomId")
    private String encryChatRoomId;

}
