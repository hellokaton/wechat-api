package com.precious;

import java.util.List;
import java.util.Map;

import jetbrick.template.JetGlobalContext;

import com.precious.model.Menu;

public class Const {

	public static final String LOGIN_SESSION = "login_user";
	
	public static JetGlobalContext CONTEXT = null;
	
	// 幻灯片key
	public static final String OPT_KEY_SLIDER = "site_silder";
	
	// 站点信息
	public static final String OPT_KEY_SITE = "site_options";
	
	// 菜单key
	public static final String MENU_KEY = "site_menu";
	
	// 存储菜单
	public static List<Menu> SITE_MENUS = null;
	
	// 存储站点信息
	public static Map<String, Object> SITE_OPTIONS = null;
	
}
