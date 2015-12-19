package com.precious.service.impl;

import java.util.List;

import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.WhereParam;

import com.blade.annotation.Component;
import com.precious.model.Menu;
import com.precious.service.MenuService;

@Component
public class MenuServiceImpl implements MenuService {
	
	private Model<Menu> model = Model.create(Menu.class);
	
	@Override
	public Menu getMenu(String slug) {
		WhereParam whereParam = WhereParam.me();
		whereParam.eq("slug", slug);
		return this.getMenu(whereParam);
	}
	
	@Override
	public Menu getMenu(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<Menu> getMenuList(WhereParam where, String order) {
		return model.select().where(where).orderBy(order).fetchList();
	}
	
	@Override
	public boolean save(String name, String slug, Integer displayOrder) {
		try {
			model.insert().param("name", name).param("slug", slug).param("display_order", displayOrder).executeAndCommit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
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
