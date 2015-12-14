package me.biezhi.film.service.impl;

import java.util.List;
import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;
import com.blade.annotation.Component;
import me.biezhi.film.model.Post;
import me.biezhi.film.service.PostService;

@Component
public class PostServiceImpl implements PostService {
	
	private Model<Post> model = Model.create(Post.class);
	
	@Override
	public Post getPost(Integer pid) {
		return model.fetchByPk(pid);
	}
	
	@Override
	public Post getPost(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<Post> getPostList(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchList();
		}
		return null;
	}
	
	@Override
	public Page<Post> getPageList(WhereParam where, Integer page, Integer pageSize, String order) {
		Page<Post> pagePost = model.select().where(where).orderBy(order).fetchPage(page, pageSize);
		return pagePost;
	}
	
	@Override
	public boolean save( String title, String slug, String cover, Integer categoryId, String content, Integer views, Boolean isDel, Integer dateline ) {
		return false;
	}
	
	@Override
	public boolean delete(Integer pid) {
		if(null != pid){
			return model.delete().eq("pid", pid).executeAndCommit() > 0;
		}
		return false;
	}
		
}
