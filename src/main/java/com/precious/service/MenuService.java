package com.precious.service;

import java.util.List;

import blade.plugin.sql2o.WhereParam;

import com.precious.model.Menu;

public interface MenuService {
	
	public Menu getMenu(String slug);
	
	public Menu getMenu(WhereParam where);
	
	public List<Menu> getMenuList(WhereParam where, String order);
	
	public boolean save(String name, String slug, Integer displayOrder);
	
	public boolean delete(Integer id);
		
}
