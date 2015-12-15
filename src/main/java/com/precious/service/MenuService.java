package com.precious.service;

import java.util.List;

import com.precious.model.Menu;

import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

public interface MenuService {
	
	public Menu getMenu(Integer id);
	
	public Menu getMenu(WhereParam where);
	
	public List<Menu> getMenuList(WhereParam where);
	
	public Page<Menu> getPageList(WhereParam where, Integer page, Integer pageSize, String order);
	
	public boolean save( String name, String slug, Integer displayOrder );
	
	public boolean delete(Integer id);
		
}
