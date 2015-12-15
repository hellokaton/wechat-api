package com.precious.service;

import java.util.List;

import com.precious.model.Tag;

import blade.plugin.sql2o.WhereParam;

public interface TagService {
	
	public Tag getTag(Integer id);
	
	public Tag getTag(String tagName);
	
	public List<Tag> getTagList(WhereParam where, String order);
	
	public Tag save( String name, Integer count);
	
	public boolean updateCount(Integer id, Integer type);
	
	public boolean delete(Integer id);
		
}
