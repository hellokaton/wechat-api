package com.precious.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blade.annotation.Component;
import com.precious.model.Option;
import com.precious.service.OptionService;

import blade.kit.CollectionKit;
import blade.kit.StringKit;
import blade.plugin.sql2o.Model;

@Component
public class OptionServiceImpl implements OptionService {
	
	private Model<Option> model = Model.create(Option.class);
	
	@Override
	public String getOption(String optKey) {
		Option option = model.fetchByPk(optKey);
		if(null != option){
			return option.getOpt_value();
		}
		return null;
	}
	
	@Override
	public Map<String, String> getOptions() {
		Map<String, String> options = new HashMap<String, String>();
		List<Option> optionList = getOptionList();
		if(CollectionKit.isNotEmpty(optionList)){
			for(Option option : optionList){
				options.put(option.getOpt_key(), option.getOpt_value());
			}
		}
		return options;
	}
	
	private List<Option> getOptionList() {
		return model.select().fetchList();
	}
	
	
	@Override
	public boolean saveOrUpdate(String optKey, String optValue) {
		if(StringKit.isNotBlank(optKey)){
			Option option = model.fetchByPk(optKey);
			if(null == option){
				model.insert().param("opt_key", optKey).param("opt_value", optValue).executeAndCommit();
			} else {
				model.update().param("opt_value", optValue).eq("opt_key", optKey).executeAndCommit();
			}
			return true;
		}
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
