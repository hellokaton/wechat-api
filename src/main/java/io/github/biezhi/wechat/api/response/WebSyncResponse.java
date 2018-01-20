package io.github.biezhi.wechat.api.response;

import com.google.gson.annotations.SerializedName;
import io.github.biezhi.wechat.api.model.Message;
import io.github.biezhi.wechat.api.model.Profile;
import io.github.biezhi.wechat.api.model.SyncKey;
import io.github.biezhi.wechat.api.model.User;
import lombok.Data;

import java.util.List;

/**
 * WebSync 响应
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
