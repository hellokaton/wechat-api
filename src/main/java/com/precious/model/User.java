package com.precious.model;

import java.io.Serializable;

import blade.plugin.sql2o.Table;

/**
 * User对象
 */
@Table(value = "t_user", PK = "uid")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer uid;
	
	//登录名
	private String login_name;
	
	//密码
	private String pass_word;
	
	//头像
	private String avatar;
	
	//是否是管理员
	private Boolean is_admin;
	
	//是否删除
	private Boolean is_del;
	
	//注册时间
	private Integer dateline;
	
	public User(){}
	
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	
	public String getPass_word() {
		return pass_word;
	}

	public void setPass_word(String pass_word) {
		this.pass_word = pass_word;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Boolean getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(Boolean is_admin) {
		this.is_admin = is_admin;
	}
	
	public Boolean getIs_del() {
		return is_del;
	}

	public void setIs_del(Boolean is_del) {
		this.is_del = is_del;
	}
	
	public Integer getDateline() {
		return dateline;
	}

	public void setDateline(Integer dateline) {
		this.dateline = dateline;
	}
	
}