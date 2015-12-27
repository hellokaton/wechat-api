package com.precious.service;

import java.util.Map;

public interface OptionService {
	
	public String getOption(String optKey);
	
	public Map<String, String> getOptions();
	
	public boolean saveOrUpdate(String optKey, String optValue);
	
	public boolean delete(String optKey);

	public boolean updateSetting(String site_name, String site_title, String site_keywords, String site_description);
		
}
