package io.github.biezhi.wechat.robot;

import com.google.gson.JsonObject;
import io.github.biezhi.wechat.Utils;
import io.github.biezhi.wechat.handle.AbstractMessageHandler;
import io.github.biezhi.wechat.model.Environment;
import io.github.biezhi.wechat.model.GroupMessage;
import io.github.biezhi.wechat.model.UserMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.github.biezhi.wechat.api.WechatApi.JSON;

/**
 * 图灵机器人实现
 *
 * @author biezhi
 *         17/06/2017
 */
public class TulingRobot extends AbstractMessageHandler {

    private String baseUrl = "http://www.tuling123.com/openapi/api";
    private String apiKey;
    private String apiSecret;

    public TulingRobot(Environment environment) {
        this.apiKey = environment.get("tuling.api_key");
        this.apiSecret = environment.get("tuling.api_secret");
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
        String text = groupMessage.getText();
        if (Utils.isNotBlank(text)) {
            String result = getResult(groupMessage.getText());
            groupMessage.sendText(result, groupMessage.getGroupId());
        }
    }

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private String getResult(String question) {

        Map<String, Object> data = new HashMap<String, Object>(2);
        data.put("key", apiKey);
        data.put("info", question);

        //获取时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        //生成密钥
        String keyParam = apiSecret + timestamp + apiKey;
        String key = Md5.MD5(keyParam);

        //加密
        Aes mc = new Aes(key);
        String dataStr = mc.encrypt(Utils.toJson(data));

        //封装请求参数
        Map<String, Object> json = new HashMap<String, Object>(3);
        json.put("key", apiKey);
        json.put("timestamp", timestamp);
        json.put("data", dataStr);

        RequestBody requestBody = RequestBody.create(JSON, Utils.toJson(json));
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            TulingRet tulingRet = Utils.fromJson(response.body().string(), TulingRet.class);
            if (tulingRet.code == 100000) {
                return tulingRet.text;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    class TulingRet {
        int code;
        String text;

    }

}
