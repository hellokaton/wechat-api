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
public class ContactUser {

    @SerializedName("UserName")
    private String userName;

    @SerializedName("ChatRoomId")
    private String chatRoomId;

}
