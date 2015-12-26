package com.precious.controller.admin;

import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.precious.Const;
import com.precious.controller.BaseController;
import com.precious.kit.SessionKit;

import blade.kit.StringKit;

public class IndexController extends BaseController {
	
	/**
	 * 首页
	 */
	public void home(Request request, Response response){
		response.render(this.getAdmin("index"));
	}
	
	/**
	 * 注销
	 * @param request
	 * @param response
	 */
	public void signin_out(Request request, Response response){
		SessionKit.removeLoginUser(request);
		String token = request.cookie(Const.REMEBERME_TOKEN);
		if(StringKit.isNotBlank(token)){
			response.removeCookie(Const.REMEBERME_TOKEN);
		}
		response.go("/");
	}
	
}
