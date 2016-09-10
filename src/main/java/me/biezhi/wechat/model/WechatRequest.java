package me.biezhi.wechat.model;

import com.blade.kit.json.JSONObject;

public class WechatRequest {

	private JSONObject SyncKey = new JSONObject();
	private JSONObject User = new JSONObject();
	
	public WechatRequest() {
	
	}
	
	public JSONObject getSyncKey() {
		return SyncKey;
	}
	public void setSyncKey(JSONObject syncKey) {
		SyncKey = syncKey;
	}
	public JSONObject getUser() {
		return User;
	}
	public void setUser(JSONObject user) {
		User = user;
	}
	
	
}
