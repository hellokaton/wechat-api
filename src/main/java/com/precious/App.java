package com.precious;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import jetbrick.template.JetEngine;
import jetbrick.template.JetGlobalContext;
import jetbrick.template.resolver.GlobalResolver;
import blade.kit.PropertyKit;
import blade.kit.StringKit;
import blade.kit.json.JSONKit;
import blade.kit.json.Json;
import blade.kit.json.JsonObject;
import blade.plugin.sql2o.Sql2oPlugin;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.blade.Blade;
import com.blade.Bootstrap;
import com.blade.annotation.Inject;
import com.blade.render.JetbrickRender;
import com.blade.route.RouteHandler;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.precious.func.Funcs;
import com.precious.service.MenuService;
import com.precious.service.OptionService;

public class App extends Bootstrap {

	@Inject
	private MenuService menuService;
	
	@Inject
	private OptionService optionService;
	
	@Override
	public void init(Blade blade) {
		// 加载配置文件
		blade.config("blade.conf");
		blade.routeConf("com.precious.controller", "route_front.conf");
		blade.routeConf("com.precious.controller.admin", "route_admin.conf");
		
		// 设置模板引擎
		JetbrickRender jetbrickRender = new JetbrickRender();
		JetEngine jetEngine = jetbrickRender.getJetEngine();
		
		JetGlobalContext globalContext = jetEngine.getGlobalContext();
		Const.CONTEXT = globalContext;
		
		GlobalResolver resolver = jetEngine.getGlobalResolver();
		resolver.registerFunctions(Funcs.class);
		
		blade.viewEngin(jetbrickRender);
		
		// 拦截器
		blade.before("/admin/.*", new RouteHandler() {
			@Override
			public void handle(Request request, Response response) {
				System.out.println("访问admin...");
			}
		});
		
		// 配置数据库
		try {
			Properties props = PropertyKit.getProperty("ds.conf");
			DataSource dataSource = DruidDataSourceFactory.createDataSource(props);
			Sql2oPlugin sql2oPlugin = blade.plugin(Sql2oPlugin.class);
			sql2oPlugin.config(dataSource).run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void contextInitialized(Blade blade) {
		
		// 查菜单
		Const.SITE_MENUS = menuService.getMenuList(null, "display_order, id desc");
		// 查站点信息
		String site_json = optionService.getOption(Const.OPT_KEY_SITE);
		
		if(StringKit.isNotBlank(site_json)){
			JsonObject jsonObject = Json.parse(site_json).asObject();
			Const.SITE_OPTIONS = JSONKit.toMap(jsonObject);
			Const.CONTEXT.set(Map.class, Const.OPT_KEY_SITE, Const.SITE_OPTIONS);
		}
		
		if(null != Const.SITE_MENUS){
			Const.CONTEXT.set(List.class, Const.MENU_KEY, Const.SITE_MENUS);
		}
		
	}
}
