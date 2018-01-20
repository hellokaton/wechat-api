package io.github.biezhi.wechat.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class ChatRoom {

    @SerializedName("UserName")
    private String userName;

    @SerializedName("MemberList")
    private List<Member> memberList;

    @SerializedName("EncryChatRoomId")
    private String encryChatRoomId;

    private List<String> items;
}
