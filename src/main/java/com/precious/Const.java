package com.precious;

import java.util.List;
import java.util.Map;

import jetbrick.template.JetGlobalContext;

import com.precious.model.Menu;

public class Const {

	public static final String LOGIN_SESSION = "login_user";
	
	// AES 盐值
	public static final String AES_SLAT = "precious_biezhi";
	
	// 文件上传目录
	public static String UPLOAD_DIR = "static/upload";
	
	public static JetGlobalContext CONTEXT = null;
	
	// 幻灯片key
	public static final String OPT_KEY_SLIDER = "site_silder";
	
	// 站点信息
	public static final String OPT_KEY_SITE = "sysinfo";
	
	// 菜单key
	public static final String MENU_KEY = "site_menus";
	
	// 记住我Token
	public static final String REMEBERME_TOKEN = "PRECIOUS_ID";
	
	/**
	 * 设置系统信息
	 * @param options
	 */
	public static void sysinfo(Map<String, String> options){
		Const.CONTEXT.set(Const.OPT_KEY_SITE, options);
	}
	
	/**
	 * 设置菜单信息
	 * @param options
	 */
	public static void sysmenu(List<Menu> menus){
		Const.CONTEXT.set(Const.MENU_KEY, menus);
	}
}
