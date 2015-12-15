package com.jewel.service.impl;

import java.util.List;

import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

import com.blade.annotation.Component;
import com.jewel.model.Menu;
import com.jewel.service.MenuService;

@Component
public class MenuServiceImpl implements MenuService {
	
	private Model<Menu> model = Model.create(Menu.class);
	
	@Override
	public Menu getMenu(Integer id) {
		return model.fetchByPk(id);
	}
	
	@Override
	public Menu getMenu(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<Menu> getMenuList(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchList();
		}
		return null;
	}
	
	@Override
	public Page<Menu> getPageList(WhereParam where, Integer page, Integer pageSize, String order) {
		Page<Menu> pageMenu = model.select().where(where).orderBy(order).fetchPage(page, pageSize);
		return pageMenu;
	}
	
	@Override
	public boolean save( String name, String slug, Integer displayOrder ) {
		return false;
	}
	
	@Override
	public boolean delete(Integer id) {
		if(null != id){
			return model.delete().eq("id", id).executeAndCommit() > 0;
		}
		return false;
	}
		
}
