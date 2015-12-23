package com.precious.controller;

import blade.kit.StringKit;
import blade.kit.json.Json;
import blade.kit.json.JsonArray;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

import com.blade.annotation.Inject;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.precious.Const;
import com.precious.Validator;
import com.precious.kit.Errors;
import com.precious.kit.SessionKit;
import com.precious.model.Menu;
import com.precious.model.Post;
import com.precious.model.User;
import com.precious.service.MenuService;
import com.precious.service.OptionService;
import com.precious.service.PostService;
import com.precious.service.UserService;

public class IndexController extends BaseController {

	@Inject
	private UserService userService;
	
	@Inject
	private PostService postService;
	
	@Inject
	private OptionService optionService;
	
	@Inject
	private MenuService menuService;
	
	/**
	 * 首页
	 */
	public void home(Request request, Response response){
		Integer page = request.paramAsInt("page");
		if(null == page || page < 1){
			page = 1;
		}
		
		// 首页幻灯片
		String slider_str = optionService.getOption(Const.OPT_KEY_SLIDER);
		if(StringKit.isNotBlank(slider_str)){
			JsonArray slider = Json.parse(slider_str).asArray();
			request.attribute("slider", slider);
		}
		
		// 查询条件
		WhereParam where = WhereParam.me();
		String tab = request.query("tab");
		if(StringKit.isNotBlank(tab)){
			Menu menu = menuService.getMenu(tab);
			if(null == menu){
				response.render(this.getFront("404"));
				return;
			}
			where.eq("menu_id", menu.getId());
		}
		
		// 电影列表
		Page<Post> pagePost = postService.getPageList(where, page, this.pageSize, "dateline desc");
		request.attribute("pagePost", pagePost);
		response.render(this.getFront("home"));
		
	}
	
	/**
	 * 显示登录页面
	 * @param request
	 * @param response
	 */
	public void show_signin(Request request, Response response){
		response.render(this.getFront("signin"));
	}
	
	/**
	 * 登录
	 * @param request
	 * @param response
	 */
	public void signin(Request request, Response response){
		String login_name = request.query("login_name");
		String pass_word = request.query("pass_word");
		
		Errors errors = Errors.empty();
		
		if(StringKit.isBlank(login_name)){
			errors.add(Validator.required("用户名"));
		}
		if(StringKit.isBlank(pass_word)){
			errors.add(Validator.required("密码"));
		}
		
		if(errors.hasError()){
			request.attribute("errors", errors.getErrors());
		} else {
			User user = userService.signin(login_name, pass_word);
			if (null == user) {
				errors.add("用户名或密码错误");
			} else {
				SessionKit.setLoginUser(request, user);
				response.go("/admin/index");
			}
		}
		if(errors.hasError()){
			request.attribute("errors", errors.getErrors());
		}
		response.render(this.getFront("signin"));
	}
	
	/**
	 * 显示详情页面
	 * @param request
	 * @param response
	 */
	public void show_flim(Request request, Response response){
		String slug = request.param("slug");
		if(StringKit.isBlank(slug)){
			
		}
		WhereParam where = WhereParam.me();
		where.eq("slug", "slug");
		Post post = postService.getPost(where);
		if(null == post){
			
		}
		request.attribute("film", post);
		response.render(this.getFront("film_detail"));
	}
	
	
}
