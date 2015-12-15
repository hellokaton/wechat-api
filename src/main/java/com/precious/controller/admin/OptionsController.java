package com.precious.controller.admin;

import blade.kit.StringKit;

import com.blade.annotation.Inject;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.precious.controller.BaseController;
import com.precious.service.OptionService;

public class OptionsController extends BaseController {
	
	@Inject
	private OptionService optionService;
	
	/**
	 * 保存设置
	 * @param request
	 * @param response
	 */
	public void save_option(Request request, Response response){
		String key = request.query("key");
		String data = request.query("data");
		boolean flag = false;
		if(StringKit.isNotBlank(key)){
			flag = optionService.saveOrUpdate(key, data);
		}
		if(flag){
			response.text(this.SUCCESS);
		} else {
			response.text(this.FAILURE);
		}
	}
	
}
