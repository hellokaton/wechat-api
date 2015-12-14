package me.biezhi.film.controller.admin;

import java.util.List;

import me.biezhi.film.controller.BaseController;
import me.biezhi.film.model.Tag;
import me.biezhi.film.service.TagService;

import com.blade.annotation.Inject;
import com.blade.web.http.Request;
import com.blade.web.http.Response;

public class TagController extends BaseController {
	
	@Inject
	private TagService tagService;
	
	/**
	 * 标签列表
	 * @param request
	 * @param response
	 */
	public void tags(Request request, Response response){
		List<Tag> tags = tagService.getTagList(null, "id desc");
		request.attribute("tags", tags);
		response.render(this.getAdmin("tags"));
	}
	
	/**
	 * 删除标签
	 */
	public void delete_tag(Request request, Response response){
		
	}
}
