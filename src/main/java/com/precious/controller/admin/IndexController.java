package com.precious.controller.admin;

import java.util.Map;

import com.blade.annotation.Inject;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.precious.Const;
import com.precious.Validator;
import com.precious.controller.BaseController;
import com.precious.kit.Errors;
import com.precious.kit.SessionKit;
import com.precious.service.OptionService;

import blade.kit.StringKit;

public class IndexController extends BaseController {
	
	@Inject
	private OptionService optionService;
	
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
	
	/**
	 * 系统设置页面
	 * @param request
	 * @param response
	 */
	public void setting(Request request, Response response){
		Map<String, String> options = optionService.getOptions();
		request.attribute("options", options);
		request.attribute(PAGE_ACTIEV_MENU, "setting");
		response.render(this.getAdmin("setting"));
	}
	
	/**
	 * 保存设置
	 * @param request
	 * @param response
	 */
	public void save_setting(Request request, Response response){
		String site_name = request.query("site_name");
		String site_title = request.query("site_title");
		String site_keywords = request.query("site_keywords");
		String site_description = request.query("site_description");
		request.attribute(PAGE_ACTIEV_MENU, "setting");
		
		Map<String, String> options = optionService.getOptions();
		
		Errors errors = Errors.empty();
		if(StringKit.isBlank(site_name)){
			errors.add(Validator.required("站点名称"));
		}
		if(StringKit.isBlank(site_title)){
			errors.add(Validator.required("站点标题"));
		}
		if(errors.hasError()){
			request.attribute("errors", errors.getErrors());
		} else {
			optionService.updateSetting(site_name, site_title, site_keywords, site_description);
			options = optionService.getOptions();
			Const.sysinfo(options);
			request.attribute("success", 1);
		}
		request.attribute("options", options);
		response.render(this.getAdmin("setting"));
	}
	
	/**
	 * 幻灯片页面
	 * @param request
	 * @param response
	 */
	public void slides(Request request, Response response){
		request.attribute(PAGE_ACTIEV_MENU, "slides");
		response.render(this.getAdmin("slides"));
	}
	
	public void save_slides(Request request, Response response){
		request.attribute(PAGE_ACTIEV_MENU, "slides");
		response.render(this.getAdmin("slides"));
	}
}
