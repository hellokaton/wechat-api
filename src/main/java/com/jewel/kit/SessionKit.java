package com.jewel.kit;

import com.blade.web.http.Request;
import com.jewel.Const;
import com.jewel.model.User;

public class SessionKit {

	public static User getLoginUser(Request request){
		return request.session().attribute(Const.LOGIN_SESSION);
	}
	
	public static void setLoginUser(Request request, User user){
		request.session().attribute(Const.LOGIN_SESSION, user);
	}
	
	public static void removeLoginUser(Request request){
		request.session().removeAttribute(Const.LOGIN_SESSION);
	}
}
