package io.github.biezhi.wechat.model.response;

import io.github.biezhi.wechat.model.entity.WechatUser;
import io.github.biezhi.wechat.model.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @author biezhi
 *         15/06/2017
 */
public class WechatResponse implements Serializable {

    private BaseResponse BaseResponse;

    private List<WechatUser> MemberList;

    private List<WechatUser> ContactList;

    public BaseResponse getBaseResponse() {
        return BaseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        BaseResponse = baseResponse;
    }

    public List<WechatUser> getMemberList() {
        return MemberList;
    }

    public void setMemberList(List<WechatUser> memberList) {
        MemberList = memberList;
    }

    public List<WechatUser> getContactList() {
        return ContactList;
    }

    public void setContactList(List<WechatUser> contactList) {
        ContactList = contactList;
    }
}
