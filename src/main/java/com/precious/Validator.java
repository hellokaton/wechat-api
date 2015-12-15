package com.precious;

import java.util.HashMap;
import java.util.Map;

import blade.kit.PropertyKit;

public final class Validator {
	
	private static Map<String, String> errorMsg = new HashMap<String, String>();
	
	static {
		errorMsg = PropertyKit.getPropertyMap("validation.lang");
	}
	
	public static String required(String val){
		return String.format(errorMsg.get("required"), val);
	}
	
	public static String email(String val){
		return String.format(errorMsg.get("email"), val);
	}
	
	public static String date(String val){
		return String.format(errorMsg.get("date"), val);
	}
	
	public static String number(String val){
		return String.format(errorMsg.get("number"), val);
	}
	
	public static String phone(String val){
		return String.format(errorMsg.get("phone"), val);
	}
	
	public static String url(String val){
		return String.format(errorMsg.get("url"), val);
	}
	
	public static String min(Long val){
		return String.format(errorMsg.get("min"), val);
	}
	
	public static String max(Long val){
		return String.format(errorMsg.get("max"), val);
	}
	
	public static String minLen(Long val){
		return String.format(errorMsg.get("minLen"), val);
	}
	
	public static String maxLen(Long val){
		return String.format(errorMsg.get("maxLen"), val);
	}
	
}
