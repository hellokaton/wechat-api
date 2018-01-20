package io.github.biezhi.wechat.api.model;

import io.github.biezhi.wechat.api.request.BaseRequest;
import lombok.Data;

/**
 * 登录会话
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Data
public class LoginSession {

    private User        user;

    private String userName;
    private String nickName;

    private String      url;
    private String      fileUrl;
    private String      syncUrl;
    private String      deviceId;
    private String      sKey;
    private String      wxSid;
    private String      wxUin;
    private String      passTicket;
    private String      synckeyStr;
    private Integer     inviteStartCount;
    private BaseRequest baseRequest;
    private SyncKey     syncKey;

    public String getSyncOrUrl() {
        String url = this.getSyncUrl();
        if (null == url || url.isEmpty()) {
            return this.getUrl();
        }
        return url;
    }

    public void setSyncKey(SyncKey syncKey) {
        this.syncKey = syncKey;

        StringBuilder syncKeyBuf = new StringBuilder();
        for (KeyItem item : syncKey.getList()) {
            syncKeyBuf.append("|").append(item.getKey()).append("_").append(item.getVal());
        }
        if (syncKeyBuf.length() > 0) {
            this.synckeyStr = syncKeyBuf.substring(1);
        }
    }

}
