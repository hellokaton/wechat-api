package io.github.biezhi.wechat.api;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * 一个默认的消息处理实现
 *
 * @author biezhi
 *         17/06/2017
 */
public class SampleMessageHandler implements MsgHandle {

    /**
     * 保存微信消息
     *
     * @param msg
     */
    @Override
    public void handleWxsync(JsonObject msg) {

    }

    @Override
    public void handleUserMessage(WechatMessage wechatMessage) {
        if (null == wechatMessage) {
            return;
        }
        String text = wechatMessage.getText();
        JsonObject raw_msg = wechatMessage.getRawMsg();
        String toUid = raw_msg.get("FromUserName").getAsString();
        // 撤回消息
        if ("test_revoke".equals(text)) {
            JsonObject dic = wechatMessage.getWechatApi().webwxsendmsg("这条消息将被撤回", toUid);
        } else if ("reply".equals(text)) {
            wechatMessage.getWechatApi().send_text("自动回复", toUid);
        } else {
            String replayMsg = "接收到：" + text;
            wechatMessage.getWechatApi().send_text(replayMsg, toUid);
        }
    }

}
