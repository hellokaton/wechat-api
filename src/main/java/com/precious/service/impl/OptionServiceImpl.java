package com.precious.service.impl;

import java.util.List;

import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

import com.blade.annotation.Component;
import com.precious.model.Option;
import com.precious.service.OptionService;

@Component
public class OptionServiceImpl implements OptionService {
	
	private Model<Option> model = Model.create(Option.class);
	
	@Override
	public Option getOption(String optKey) {
		return model.fetchByPk(optKey);
	}
	
	@Override
	public Option getOption(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<Option> getOptionList(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchList();
		}
		return null;
	}
	
	@Override
	public Page<Option> getPageList(WhereParam where, Integer page, Integer pageSize, String order) {
		Page<Option> pageOption = model.select().where(where).orderBy(order).fetchPage(page, pageSize);
		return pageOption;
	}
	
	@Override
	public boolean save( String optValue ) {
		return false;
	}
	
	@Override
	public boolean delete(String optKey) {
		if(null != optKey){
			return model.delete().eq("optKey", optKey).executeAndCommit() > 0;
		}
		return false;
	}
		
}
