package io.github.biezhi.wechat.model.response;

import io.github.biezhi.wechat.model.entity.SyncKey;
import io.github.biezhi.wechat.model.entity.WechatUser;

import java.io.Serializable;
import java.util.List;

/**
 * @author biezhi
 *         15/06/2017
 */
public class BaseResponse implements Serializable {

    private SyncKey SyncKey;
    private Integer Ret;
    private WechatUser User;
    private List<WechatUser> MemberList;

    public SyncKey getSyncKey() {
        return SyncKey;
    }

    public void setSyncKey(SyncKey syncKey) {
        SyncKey = syncKey;
    }

    public Integer getRet() {
        return Ret;
    }

    public void setRet(Integer ret) {
        Ret = ret;
    }

    public List<WechatUser> getMemberList() {
        return MemberList;
    }

    public void setMemberList(List<WechatUser> memberList) {
        MemberList = memberList;
    }

    public WechatUser getUser() {
        return User;
    }

    public void setUser(WechatUser user) {
        User = user;
    }
}
