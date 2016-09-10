package me.biezhi.wechat.model;

import com.blade.kit.json.JSONArray;

public class WechatContact {

	// 微信联系人列表，可聊天的联系人列表
	private JSONArray MemberList, ContactList;
	
	public WechatContact() {
		// TODO Auto-generated constructor stub
	}

	public JSONArray getMemberList() {
		return MemberList;
	}

	public void setMemberList(JSONArray memberList) {
		MemberList = memberList;
	}

	public JSONArray getContactList() {
		return ContactList;
	}

	public void setContactList(JSONArray contactList) {
		ContactList = contactList;
	}
	
}
