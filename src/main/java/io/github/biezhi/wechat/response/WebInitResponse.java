package io.github.biezhi.wechat.response;

import com.google.gson.annotations.SerializedName;
import io.github.biezhi.wechat.model.SyncKey;
import io.github.biezhi.wechat.model.User;
import io.github.biezhi.wechat.response.JsonResponse;
import lombok.Data;

import java.util.List;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Data
public class WebInitResponse {

    @SerializedName("BaseResponse")
    private JsonResponse baseResponse;

    @SerializedName("Count")
    private Integer count;

    @SerializedName("ContactList")
    private List<User> contactList;

    @SerializedName("SyncKey")
    private SyncKey syncKey;

    @SerializedName("User")
    private User user;

    @SerializedName("ChatSet")
    private String chatSet;

    @SerializedName("SKey")
    private String sKey;

    @SerializedName("InviteStartCount")
    private Integer inviteStartCount;

    @SerializedName("ClickReportInterval")
    private Integer clickReportInterval;

}
