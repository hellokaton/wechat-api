package io.github.biezhi.wechat.handle;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * 茉莉机器人实现
 *
 * @author biezhi
 *         17/06/2017
 */
public class MoliHandler implements MessageHandle {

    private String api_key;
    private String api_secret;
    private String baseUrl;

    public MoliHandler(String api_key, String api_secret) {
        this.api_key = api_key;
        this.api_secret = api_secret;
        this.baseUrl = "http://i.itpk.cn/api.php?api_key=" + api_key + "&api_secret=" + api_secret;
    }

    /**
     * 保存微信消息
     *
     * @param msg
     */
    @Override
    public void wxSync(JsonObject msg) {
    }

    @Override
    public void userMessage(WechatMessage wechatMessage) {
        if (null == wechatMessage) {
            return;
        }
        String text = wechatMessage.getText();
        JsonObject raw_msg = wechatMessage.getRawMsg();
        String toUid = raw_msg.get("FromUserName").getAsString();

        String url = baseUrl + "&question=" + text;
        String result = HttpRequest.get(url).connectTimeout(3000).body();
        wechatMessage.getWechatApi().send_text(result, toUid);

    }

    @Override
    public void groupMessage(WechatMessage wechatMessage) {

    }

}
