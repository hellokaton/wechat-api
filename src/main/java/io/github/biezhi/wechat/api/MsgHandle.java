package io.github.biezhi.wechat.api;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * 一个默认的消息处理实现
 *
 * @author biezhi
 *         17/06/2017
 */
public interface MsgHandle {

    /**
     * 保存微信消息
     *
     * @param msg
     */
    void handleWxsync(JsonObject msg);

    void handleUserMessage(WechatMessage wechatMessage);

}
