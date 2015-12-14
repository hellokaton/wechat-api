package me.biezhi.film.controller.admin;

import me.biezhi.film.controller.BaseController;
import me.biezhi.film.kit.SessionKit;

import com.blade.web.http.Request;
import com.blade.web.http.Response;

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
