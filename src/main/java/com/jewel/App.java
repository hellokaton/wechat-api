package com.jewel;

import java.util.Properties;

import javax.sql.DataSource;

import jetbrick.template.JetEngine;
import jetbrick.template.JetGlobalContext;
import jetbrick.template.resolver.GlobalResolver;
import blade.kit.PropertyKit;
import blade.plugin.sql2o.Sql2oPlugin;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.blade.Blade;
import com.blade.Bootstrap;
import com.blade.render.JetbrickRender;
import com.blade.route.RouteHandler;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.jewel.func.Funcs;

public class App extends Bootstrap {

	@Override
	public void init(Blade blade) {
		// 加载配置文件
		blade.config("blade.conf");
		blade.routeConf("me.biezhi.film.controller", "route_front.conf");
		blade.routeConf("me.biezhi.film.controller.admin", "route_admin.conf");
		
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

}
