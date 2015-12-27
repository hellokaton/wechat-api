package com.precious.service;

import com.precious.model.Tag;

public interface TagService {
	
	public Tag getTag(Integer id);
	
	public Tag getTag(String tagName);
	
	public String getTags(String order);
	
	public Tag save( String name, Integer count);
	
	public boolean updateCount(Integer id, Integer type);
	
	public boolean delete(Integer id);
		
}
