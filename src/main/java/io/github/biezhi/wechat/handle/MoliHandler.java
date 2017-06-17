package io.github.biezhi.wechat.handle;

import com.google.gson.JsonObject;
import io.github.biezhi.wechat.model.GroupMessage;
import io.github.biezhi.wechat.model.UserMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 茉莉机器人实现
 *
 * @author biezhi
 *         17/06/2017
 */
public class MoliHandler extends AbstractMessageHandler {

    private String baseUrl;

    public MoliHandler(String api_key, String api_secret) {
        this.baseUrl = "http://i.itpk.cn/api.php?api_key=" + api_key + "&api_secret=" + api_secret;
    }

    @Override
    public void userMessage(UserMessage userMessage) {
        if (null == userMessage) {
            return;
        }
        String text = userMessage.getText();
        JsonObject raw_msg = userMessage.getRawMsg();
        String toUid = raw_msg.get("FromUserName").getAsString();
        String result = getResult(text);
        userMessage.sendText(result, toUid);
    }

    @Override
    public void groupMessage(GroupMessage groupMessage) {
        System.out.println(groupMessage);
        if (groupMessage.getGroup_name().equals("测试群聊567")) {
//            groupMessage.sendText(groupMessage.toString(), groupMessage.getGroupId());
            String result = getResult(groupMessage.getText());
            groupMessage.sendText(result, groupMessage.getGroupId());
        }
    }

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private String getResult(String question) {
        String url = baseUrl + "&question=" + question;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
