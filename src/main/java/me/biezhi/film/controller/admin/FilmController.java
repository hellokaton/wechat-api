package me.biezhi.film.controller.admin;

import me.biezhi.film.controller.BaseController;
import me.biezhi.film.model.Post;
import me.biezhi.film.service.PostService;

import com.blade.annotation.Inject;
import com.blade.web.http.Request;
import com.blade.web.http.Response;

public class FilmController extends BaseController {
	
	@Inject
	private PostService postService;
	
	/**
	 * 添加/编辑电影
	 */
	public void film_page(Request request, Response response){
		Integer pid = request.paramAsInt("pid");
		if(null != pid){
			Post film = postService.getPost(pid);
			request.attribute("film", film);
		}
		response.render(this.getAdmin("film_page"));
	}
	
	/**
	 * 保存电影
	 */
	public void save_film(Request request, Response response){
		
	}
	
	/**
	 * 修改电影
	 */
	public void update_film(Request request, Response response){
		
	}
	
	/**
	 * 删除电影
	 */
	public void delete_film(Request request, Response response){
		
	}
}
