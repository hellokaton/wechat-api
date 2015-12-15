package com.jewel.controller;

import com.blade.render.ModelAndView;
import com.blade.web.http.Request;
import com.jewel.kit.SessionKit;
import com.jewel.model.User;

public class BaseController {
	
	/**
	 * 默认分页条数
	 */
	protected Integer pageSize = 10;
	
	/**
	 * 状态字段
	 */
	protected String STATUS = "status";
	
	/**
	 * 成功
	 */
	protected String SUCCESS = "success";
	
	/**
	 * 服务器异常
	 */
	protected String ERROR = "error";
	
	/**
	 * 已经存在
	 */
	protected String EXIST = "exist";
	
	/**
	 * 失败
	 */
	protected String FAILURE = "failure";
	
	protected ModelAndView getFront(String view){
		return new ModelAndView(view);
	}
	
	protected ModelAndView getAdmin(String view){
		view = "admin/" + view;
		return new ModelAndView(view);
	}
	
	/**
	 * @return	返回是否登录
	 */
	public boolean isSignin(Request request){
		return null != SessionKit.getLoginUser(request);
	}
	
	/**
	 * @return	返回是否登录并且是管理员
	 */
	public boolean isAdmin(Request request){
		User user= SessionKit.getLoginUser(request);
		if(null != user && user.getIs_admin()){
			return true;
		}
		return false;
	}
	
}
