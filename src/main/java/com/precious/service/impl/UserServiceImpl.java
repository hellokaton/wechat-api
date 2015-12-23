package com.precious.service.impl;

import java.util.List;

import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

import com.blade.annotation.Component;
import com.precious.kit.SecretKit;
import com.precious.model.User;
import com.precious.service.UserService;

@Component
public class UserServiceImpl implements UserService {
	
	private Model<User> model = Model.create(User.class);
	
	@Override
	public User getUser(Integer uid) {
		return model.fetchByPk(uid);
	}
	
	@Override
	public User getUser(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<User> getUserList(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchList();
		}
		return null;
	}
	
	@Override
	public Page<User> getPageList(WhereParam where, Integer page, Integer pageSize, String order) {
		Page<User> pageUser = model.select().where(where).orderBy(order).fetchPage(page, pageSize);
		return pageUser;
	}
	
	@Override
	public boolean save( String loginName, String passWord, String avatar, Boolean isAdmin, Boolean isDel, Integer dateline ) {
		return false;
	}
	
	@Override
	public boolean delete(Integer uid) {
		if(null != uid){
			return model.delete().eq("uid", uid).executeAndCommit() > 0;
		}
		return false;
	}

	@Override
	public User signin(String login_name, String pass_word) {
		String pass = SecretKit.password(login_name, pass_word);
		WhereParam whereParam = WhereParam.me();
		whereParam.eq("login_name", login_name);
		whereParam.eq("pass_word", pass);
		whereParam.eq("is_del", false);
		return this.getUser(whereParam);
	}
		
}
