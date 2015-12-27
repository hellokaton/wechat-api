package com.precious.controller.admin;

import java.util.List;

import com.blade.annotation.Inject;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.precious.Validator;
import com.precious.controller.BaseController;
import com.precious.kit.Errors;
import com.precious.model.Menu;
import com.precious.model.Post;
import com.precious.service.MenuService;
import com.precious.service.PostService;
import com.precious.service.PostTagService;

import blade.kit.StringKit;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

public class FilmController extends BaseController {
	
	@Inject
	private PostService postService;
	
	@Inject
	private PostTagService postTagService;
	
	@Inject
	private MenuService menuService;
	
	/**
	 * 电影列表
	 * @param request
	 * @param response
	 */
	public void films(Request request, Response response){
		
		Integer page = request.paramAsInt("page");
		if(null == page || page < 1){
			page = 1;
		}
		WhereParam where = WhereParam.me();
		// 电影列表
		Page<Post> pagePost = postService.getPageList(where, page, this.pageSize, "dateline desc");
		request.attribute("films", pagePost);
		
		request.attribute(PAGE_ACTIEV_MENU, "films");
		response.render(this.getAdmin("films"));
	}
	
	/**
	 * 添加/编辑电影页面
	 */
	public void film_page(Request request, Response response){
		Integer pid = request.paramAsInt("pid");
		List<Menu> menus = menuService.getMenuList(null, "id desc");
		request.attribute("menus", menus);
		request.attribute(PAGE_ACTIEV_MENU, "films");
		
		if(null != pid){
			Post film = postService.getPost(pid);
			if(null != film){
				request.attribute("film", film);
				response.render(this.getAdmin("edit_film"));
				return;
			}
		}
		response.render(this.getAdmin("add_film"));
	}
	
	/**
	 * 保存电影
	 */
	public void save_film(Request request, Response response){
		String title = request.query("title");
		String slug = request.query("slug");
		String cover = request.query("cover");
		Integer menu_id = request.queryAsInt("menu_id");
		String content = request.query("content");
		String links = request.query("links");
		String tags = request.query("tags");
		
		request.attribute(PAGE_ACTIEV_MENU, "films");
		
		Errors errors = Errors.empty();
		if(StringKit.isBlank(title)){
			errors.add(Validator.required("标题"));
		}
		if(StringKit.isBlank(cover)){
			errors.add(Validator.required("封面"));
		}
		if(null == menu_id){
			errors.add(Validator.required("所属菜单"));
		}
		if(StringKit.isBlank(content)){
			errors.add(Validator.required("内容"));
		}
		if(StringKit.isBlank(tags)){
			errors.add("请选择标签");
		}
		if(errors.hasError()){
			request.attribute("errors", errors.getErrors());
			request.attribute(PAGE_ACTIEV_MENU, "films");
		} else {
			boolean flag = postService.save(title, slug, cover, menu_id, content, links, tags);
			if(flag){
				response.go("/admin/flims");
				return;
			} else {
				errors.add("发布出现异常!");
			}
		}
		response.render(this.getAdmin("add_film"));
		
	}
	
	/**
	 * 修改电影
	 */
	public void update_film(Request request, Response response){
		Integer pid = request.queryAsInt("pid");
		String title = request.query("title");
		String slug = request.query("slug");
		String cover = request.query("cover");
		Integer menu_id = request.queryAsInt("menu_id");
		String content = request.query("content");
		String links = request.query("links");
		
		Errors errors = Errors.empty();
		
		request.attribute(PAGE_ACTIEV_MENU, "films");
		
		if(null == pid){
			errors.add(Validator.required("唯一标识"));
		}
		
		if(StringKit.isBlank(title)){
			errors.add(Validator.required("标题"));
		}
		
		if(StringKit.isBlank(cover)){
			errors.add(Validator.required("封面"));
		}
		
		if(null == menu_id){
			errors.add(Validator.required("所属菜单"));
		}
		
		if(StringKit.isBlank(content)){
			errors.add(Validator.required("内容"));
		}
		
		if(errors.hasError()){
			request.attribute("errors", errors.getErrors());
		} else {
			boolean flag = postService.update(pid, title, slug, cover, menu_id, content, links);
			if(flag){
				response.go("/admin/flims");
				return;
			} else {
				errors.add("更新出现异常!");
			}
		}
		
		response.render(this.getAdmin("edit_film"));
	}
	
	/**
	 * 删除电影
	 */
	public void delete_film(Request request, Response response){
		Integer pid = request.queryAsInt("pid");
		boolean flag = postService.delete(pid);
		if(flag){
			response.text(this.SUCCESS);
		} else {
			response.text(this.FAILURE);
		}
	}
	
	/**
	 * 删除电影标签
	 */
	public void delete_film_tag(Request request, Response response){
		Integer pid = request.queryAsInt("pid");
		Integer tid = request.queryAsInt("tid");
		if(null != pid && null != tid){
			boolean flag = postTagService.delete(pid, tid);
			if(flag){
				response.text(this.SUCCESS);
			} else {
				response.text(this.FAILURE);
			}
		}
	}
}
