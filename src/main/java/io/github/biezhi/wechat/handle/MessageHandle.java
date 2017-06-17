package io.github.biezhi.wechat.handle;

import com.google.gson.JsonObject;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * 一个默认的消息处理实现
 *
 * @author biezhi
 *         17/06/2017
 */
public interface MessageHandle {

    /**
     * 保存微信消息
     *
     * @param msg
     */
    void wxSync(JsonObject msg);

    void userMessage(WechatMessage wechatMessage);

    void groupMessage(WechatMessage wechatMessage);

}
