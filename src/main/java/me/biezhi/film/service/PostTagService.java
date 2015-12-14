package me.biezhi.film.service;

import java.util.List;

import me.biezhi.film.model.PostTag;
import blade.plugin.sql2o.WhereParam;

public interface PostTagService {
	
	public PostTag getPostTag(Integer id);
	
	public PostTag getPostTag(WhereParam where);
	
	public List<PostTag> getPostTagList(WhereParam where, String order);
	
	public boolean save( Integer pid, Integer tid );
	
	public boolean deleteByPid(Integer pid);

	public boolean delete(Integer pid, Integer tid);
		
}
