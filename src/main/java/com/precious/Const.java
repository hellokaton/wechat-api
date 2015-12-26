package com.precious;

import java.util.List;
import java.util.Map;

import jetbrick.template.JetGlobalContext;

import com.precious.model.Menu;

public class Const {

	public static final String LOGIN_SESSION = "login_user";
	
	// AES 盐值
	public static final String AES_SLAT = "precious_biezhi";
	
	public static JetGlobalContext CONTEXT = null;
	
	// 幻灯片key
	public static final String OPT_KEY_SLIDER = "site_silder";
	
	// 站点信息
	public static final String OPT_KEY_SITE = "sysinfo";
	
	// 菜单key
	public static final String MENU_KEY = "site_menus";
	
	// 存储菜单
	public static List<Menu> SITE_MENUS = null;
	
	// 存储站点信息
	public static Map<String, String> SITE_OPTIONS = null;
	
	// 记住我Token
	public static final String REMEBERME_TOKEN = "PRECIOUS_ID";
}
