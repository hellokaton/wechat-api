package io.github.biezhi.wechat.robot;

import com.github.kevinsawicki.http.HttpRequest;
import io.github.biezhi.wechat.Constant;
import io.github.biezhi.wechat.util.StringUtils;

public class MoLiRobot implements Robot {

    private String apiUrl;

    public MoLiRobot() {
        String api_key = Constant.environment.get("itpk.api_key");
        String api_secret = Constant.environment.get("itpk.api_secret");
        if (StringUtils.isNotBlank(api_key) && StringUtils.isNotBlank(api_secret)) {
            this.apiUrl = Constant.ITPK_API + "?api_key=" + api_key + "&api_secret=" + api_secret;
        }
    }

    @Override
    public String talk(String msg) {
        if (null == this.apiUrl) {
            return "机器人未配置";
        }
        String url = apiUrl + "&question=" + msg;
        String result = HttpRequest.get(url).connectTimeout(3000).body();
        return result;
    }

}
