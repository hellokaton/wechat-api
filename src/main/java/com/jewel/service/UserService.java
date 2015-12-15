package com.jewel.service;

import java.util.List;

import com.jewel.model.User;

import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

public interface UserService {
	
	public User getUser(Integer uid);
	
	public User getUser(WhereParam where);
	
	public User signin(String login_name, String pass_word);
	
	public List<User> getUserList(WhereParam where);
	
	public Page<User> getPageList(WhereParam where, Integer page, Integer pageSize, String order);
	
	public boolean save( String loginName, String passWord, String avatar, Boolean isAdmin, Boolean isDel, Integer dateline );
	
	public boolean delete(Integer uid);
		
}
