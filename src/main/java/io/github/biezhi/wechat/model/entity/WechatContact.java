package io.github.biezhi.wechat.model.entity;

import java.util.List;

public class WechatContact {

    // 微信联系人列表，可聊天的联系人列表
    private List<WechatUser> memberList;
    private List<WechatUser> contactList;
    private List<WechatUser> groupList;

    public WechatContact() {
    }

    public List<WechatUser> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<WechatUser> memberList) {
        this.memberList = memberList;
    }

    public List<WechatUser> getContactList() {
        return contactList;
    }

    public void setContactList(List<WechatUser> contactList) {
        this.contactList = contactList;
    }

    public List<WechatUser> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<WechatUser> groupList) {
        this.groupList = groupList;
    }
}
