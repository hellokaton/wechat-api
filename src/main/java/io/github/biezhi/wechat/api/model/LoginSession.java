package io.github.biezhi.wechat.api.model;

import io.github.biezhi.wechat.api.request.BaseRequest;
import io.github.biezhi.wechat.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录会话
 *
 * @author biezhi
 * @date 2018/1/18
 */
@Data
public class LoginSession implements Serializable {

    private Account account;

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
    private String      syncKeyStr;
    private Integer     inviteStartCount;
    private BaseRequest baseRequest;
    private SyncKey     syncKey;

    public String getSyncOrUrl() {
        if (StringUtils.isNotEmpty(this.syncUrl)) {
            return this.syncUrl;
        }
        return this.url;
    }

    public String getFileUrl() {
        if (StringUtils.isNotEmpty(this.fileUrl)) {
            return this.fileUrl;
        }
        return this.url;
    }

    public void setSyncKey(SyncKey syncKey) {
        this.syncKey = syncKey;

        StringBuilder syncKeyBuf = new StringBuilder();
        for (KeyItem item : syncKey.getList()) {
            syncKeyBuf.append("|").append(item.getKey()).append("_").append(item.getVal());
        }
        if (syncKeyBuf.length() > 0) {
            this.syncKeyStr = syncKeyBuf.substring(1);
        }
    }

}
