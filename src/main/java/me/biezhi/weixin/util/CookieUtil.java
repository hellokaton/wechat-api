package me.biezhi.weixin.util;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import blade.kit.http.HttpRequest;

public class CookieUtil {

	public static String getCookie(HttpRequest request) {
		HttpURLConnection conn = request.getConnection();
		Map<String, List<String>> resHeaders = conn.getHeaderFields();
		StringBuffer sBuffer = new StringBuffer();
		for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
			String name = entry.getKey();
			if (name == null)
				continue; // http/1.1 line
			List<String> values = entry.getValue();
			if (name.equalsIgnoreCase("Set-Cookie")) {
				for (String value : values) {
					if (value == null) {
						continue;
					}
					String cookie = value.substring(0, value.indexOf(";") + 1);
					sBuffer.append(cookie);
				}
			}
		}
		if(sBuffer.length() > 0){
			return sBuffer.toString();
		}
		return sBuffer.toString();
	}
	
}
