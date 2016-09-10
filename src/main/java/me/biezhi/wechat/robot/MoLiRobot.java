package me.biezhi.wechat.robot;

import com.blade.kit.base.Config;
import com.blade.kit.http.HttpRequest;

import me.biezhi.wechat.Constant;

public class MoLiRobot implements Robot {

	private String apiUrl;

	public MoLiRobot() {
		Config config = Config.load("classpath:config.properties");
		String api_key = config.get("itpk.api_key");
		String api_secret = config.get("itpk.api_secret");
		this.apiUrl = Constant.ITPK_API + "?api_key=" + api_key + "&api_secret=" + api_secret;
	}

	@Override
	public String talk(String msg) {
		String url = apiUrl + "&question=" + msg;
		String result = HttpRequest.get(url).connectTimeout(3000).body();
		return result;
	}

}
