package com.precious.kit;

import blade.kit.HashidKit;
import blade.kit.StringKit;

/**
 * 加密解密类
 *
 * @author	<a href="mailto:biezhi.me@gmail.com" target="_blank">biezhi</a>
 * @since	1.0
 */
public class SecretKit {

	/**
	 * 生成用户密码
	 * @param login_name
	 * @param pass_word
	 * @return
	 */
	public static String password(String login_name, String pass_word){
		if(StringKit.isNotBlank(login_name) && StringKit.isNotBlank(pass_word)){
			HashidKit hashidKit = new HashidKit(login_name + pass_word, 32);
			return hashidKit.encode( login_name.length() );
		}
		return null;
	}
	
	public static String createAttachId(String key){
		HashidKit hashidKit = new HashidKit(key, 12);
		return hashidKit.encode( System.currentTimeMillis() );
	}
	
	/**
	 * 创建记住我token
	 * @param uid
	 * @return
	 */
	public static String createRemeberToken(Integer uid){
		if(null != uid && uid != 0){
			return MagicCrypt.aesEncrypt(uid.toString());
		}
		return "";
	}

	/**
	 * 解密记住我的uid
	 * @param token
	 * @return
	 */
	public static Integer decodeRemeberToken(String token){
		if(StringKit.isNotBlank(token)){
			try {
				String decode = MagicCrypt.aesDecrypt(token);
				return Integer.valueOf(decode);
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String login_name = "admin";
		String pass_word = "123456";
		
		System.out.println(password(login_name, pass_word));
		
	}
}
