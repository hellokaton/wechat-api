package io.github.biezhi.wechat.response;

import com.google.gson.annotations.SerializedName;
import io.github.biezhi.wechat.model.*;
import lombok.Data;

import java.util.List;

/**
 * sync check 响应体
 *
 * @author biezhi
 * @date 2018/1/20
 */
@Data
public class WebSyncResponse {

    @SerializedName("BaseResponse")
    private BaseResponse baseResponse;

    @SerializedName("AddMsgCount")
    private Integer addMsgCount;

    @SerializedName("AddMsgList")
    private List<Message> addMessageList;

    @SerializedName("ModContactCount")
    private Integer modContactCount;

    @SerializedName("ModContactList")
    private List<User> modContactList;

    @SerializedName("DelContactCount")
    private Integer delContactCount;

    @SerializedName("DelContactList")
    private List<User> delContactList;

    @SerializedName("ModChatRoomMemberCount")
    private Integer modChatRoomMemberCount;

    @SerializedName("ModChatRoomMemberList")
    private List<User> modChatRoomMemberList;

    @SerializedName("Profile")
    private Profile profile;

    @SerializedName("ContinueFlag")
    private Integer continueFlag;

    @SerializedName("SyncKey")
    private SyncKey syncKey;

    @SerializedName("SKey")
    private String sKey;

    @SerializedName("SyncCheckKey")
    private SyncKey syncCheckKey;

    public boolean success() {
        return null != baseResponse && baseResponse.getRet().equals(0);
    }

}
