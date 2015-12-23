package com.precious.controller1;

import com.blade.annotation.Inject;
import com.blade.annotation.Path;
import com.blade.annotation.Route;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.precious.controller.BaseController;
import com.precious.service.UserService;

@Path("/")
public class IndexController extends BaseController {
	
	@Inject
	private UserService userService;
	
	@Route("hello")
	public void hello(Request request, Response response){
		System.out.println("userservice = " + userService);
	}
	
}
