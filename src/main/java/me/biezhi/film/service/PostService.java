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
	
	public boolean save(String title, String slug, String cover, Integer menu_id, String content, String links, String tags);
	
	public boolean update(Integer pid, String title, String slug, String cover, Integer menu_id, String content, String links);
	
	public boolean delete(Integer pid);
		
}
