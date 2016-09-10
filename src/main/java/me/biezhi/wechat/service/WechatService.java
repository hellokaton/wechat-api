package me.biezhi.wechat.service;

import com.blade.kit.json.JSONObject;

import me.biezhi.wechat.exception.WechatException;
import me.biezhi.wechat.model.WechatContact;
import me.biezhi.wechat.model.WechatMeta;
import me.biezhi.wechat.model.WechatRequest;

public interface WechatService {
	
	/**
	 * 获取UUID
	 * @return
	 */
	String getUUID() throws WechatException;
	
	WechatRequest wxInit(WechatMeta wechatMeta) throws WechatException;
	
	/**
	 * 开启状态通知
	 * @return
	 */
	boolean openStatusNotify(WechatMeta wechatMeta, WechatRequest wechatRequest);
	
	/**
	 * 获取联系人
	 * @param wechatMeta
	 * @param wechatRequest
	 * @return
	 */
	WechatContact getContact(WechatMeta wechatMeta, WechatRequest wechatRequest);
	
	/**
	 * 消息检查
	 * @param wechatMeta
	 * @return
	 */
	int[] syncCheck(String webpush_url, WechatMeta wechatMeta);
	
	/**
	 * 处理聊天信息
	 * @param wechatRequest
	 * @param data
	 */
	void handleMsg(WechatMeta wechatMeta, WechatRequest wechatRequest, WechatContact contact, JSONObject data);
	
	/**
	 * 获取最新消息
	 * @param meta
	 * @return
	 */
	JSONObject webwxsync(WechatMeta meta, WechatRequest wechatRequest);
}
