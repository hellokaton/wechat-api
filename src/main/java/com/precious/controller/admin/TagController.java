package com.precious.controller.admin;

import com.blade.annotation.Inject;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.precious.controller.BaseController;
import com.precious.model.Tag;
import com.precious.service.TagService;

import blade.kit.StringKit;

public class TagController extends BaseController {
	
	@Inject
	private TagService tagService;
	
	/**
	 * 标签列表
	 * @param request
	 * @param response
	 */
	public void tags(Request request, Response response){
		String tags = tagService.getTags("count,id desc");
		request.attribute("tags", tags);
		request.attribute(PAGE_ACTIEV_MENU, "tags");
		response.render(this.getAdmin("tags"));
	}
	
	/**
	 * 保存标签
	 */
	public void save_tag(Request request, Response response){
		String name = request.query("name");
		
		if(StringKit.isBlank(name)){
			response.text(this.FAILURE);
			return;
		}
		
		Tag tag = tagService.getTag(name);
		if(null == tag){
			tagService.save(name, 0);
			response.text(this.SUCCESS);
			return;
		}
		response.text(this.EXIST);
		return;
	}
	
	/**
	 * 删除标签
	 */
	public void delete_tag(Request request, Response response){
		String name = request.query("name");
		
		if(StringKit.isBlank(name)){
			response.text(this.FAILURE);
			return;
		}
		Tag tag = tagService.getTag(name);
		if(null == tag){
			response.text(this.NOT_EXIST);
			return;
		}
		tagService.delete(tag.getId());
		response.text(this.SUCCESS);
	}
}
