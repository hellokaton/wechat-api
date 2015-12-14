package me.biezhi.film.service;

import java.util.List;

import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

import me.biezhi.film.model.Post;

public interface PostService {
	
	public Post getPost(Integer pid);
	
	public Post getPost(WhereParam where);
	
	public List<Post> getPostList(WhereParam where);
	
	public Page<Post> getPageList(WhereParam where, Integer page, Integer pageSize, String order);
	
	public boolean save( String title, String slug, String cover, Integer categoryId, String content, Integer views, Boolean isDel, Integer dateline );
	
	public boolean delete(Integer pid);
		
}
