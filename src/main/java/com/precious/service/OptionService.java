package com.precious.service;

import java.util.List;

import com.precious.model.Option;

import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

public interface OptionService {
	
	public Option getOption(String optKey);
	
	public Option getOption(WhereParam where);
	
	public List<Option> getOptionList(WhereParam where);
	
	public Page<Option> getPageList(WhereParam where, Integer page, Integer pageSize, String order);
	
	public boolean save( String optValue );
	
	public boolean delete(String optKey);
		
}
