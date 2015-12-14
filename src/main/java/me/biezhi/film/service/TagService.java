package me.biezhi.film.service;

import java.util.List;

import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

import me.biezhi.film.model.Tag;

public interface TagService {
	
	public Tag getTag(Integer id);
	
	public Tag getTag(WhereParam where);
	
	public List<Tag> getTagList(WhereParam where);
	
	public Page<Tag> getPageList(WhereParam where, Integer page, Integer pageSize, String order);
	
	public boolean save( String name, Integer count );
	
	public boolean delete(Integer id);
		
}
