package io.github.biezhi.wechat.handle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.biezhi.wechat.model.GroupMessage;
import io.github.biezhi.wechat.model.UserMessage;

/**
 * 一个默认的消息处理实现
 *
 * @author biezhi
 * 17/06/2017
 */
public class SampleMessageHandler implements MessageHandle {

    /**
     * 保存微信消息
     *
     * @param msg
     */
    @Override
    public void wxSync(JsonObject msg) {
    }

    @Override
    public void userMessage(UserMessage userMessage) {
        if (null == userMessage || userMessage.isEmpty()) {
            return;
        }
        String     text    = userMessage.getText();
        JsonObject raw_msg = userMessage.getRawMsg();
        String     toUid   = raw_msg.get("FromUserName").getAsString();
        // 撤回消息
        if ("test_revoke".equals(text)) {
            JsonObject dic = userMessage.getWechatApi().wxSendMessage("这条消息将被撤回", toUid);
        } else if ("reply".equals(text)) {
            userMessage.sendText("自动回复", toUid);
        } else {
            String replayMsg = "接收到：" + text;
            userMessage.sendText(replayMsg, toUid);
        }
    }

    @Override
    public void groupMessage(GroupMessage groupMessage) {
        groupMessage.sendText("自动回复", groupMessage.getGroupId());
    }

    @Override
    public void groupMemberChange(String groupId, JsonArray memberList) {

    }

    @Override
    public void groupListChange(String groupId, JsonArray memberList) {

    }

}
