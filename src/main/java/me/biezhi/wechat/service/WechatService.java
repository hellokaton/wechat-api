package me.biezhi.wechat.service;

import com.blade.kit.json.JSONObject;

import me.biezhi.wechat.exception.WechatException;
import me.biezhi.wechat.model.WechatContact;
import me.biezhi.wechat.model.WechatMeta;

public interface WechatService {
	
	/**
	 * 获取UUID
	 * @return
	 */
	String getUUID();
	
	/**
	 * 微信初始化
	 * @param wechatMeta
	 * @throws WechatException
	 */
	void wxInit(WechatMeta wechatMeta);
	
	/**
	 * 开启状态通知
	 * @return
	 */
	void openStatusNotify(WechatMeta wechatMeta);
	
	/**
	 * 获取联系人
	 * @param wechatMeta
	 * @return
	 */
	WechatContact getContact(WechatMeta wechatMeta);
	
	/**
	 * 选择同步线路
	 * 
	 * @param wechatMeta
	 * @return
	 * @throws WechatException
	 */
	void choiceSyncLine(WechatMeta wechatMeta);
	
	/**
	 * 消息检查
	 * @param wechatMeta
	 * @return
	 */
	int[] syncCheck(WechatMeta wechatMeta);
	
	/**
	 * 处理聊天信息
	 * @param wechatMeta
	 * @param data
	 */
	void handleMsg(WechatMeta wechatMeta, JSONObject data);
	
	/**
	 * 获取最新消息
	 * @param meta
	 * @return
	 */
	JSONObject webwxsync(WechatMeta meta);
}
