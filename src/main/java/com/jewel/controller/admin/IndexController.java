package com.jewel.controller.admin;

import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.jewel.controller.BaseController;
import com.jewel.kit.SessionKit;

public class IndexController extends BaseController {
	
	/**
	 * 首页
	 */
	public void home(Request request, Response response){
		
	}
	
	/**
	 * 注销
	 * @param request
	 * @param response
	 */
	public void signin_out(Request request, Response response){
		SessionKit.removeLoginUser(request);
		response.go("/");
	}
	
	
	
}
