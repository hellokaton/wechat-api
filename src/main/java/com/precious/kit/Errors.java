package com.precious.kit;

import java.util.ArrayList;
import java.util.List;

public class Errors {

	private List<String> errors = new ArrayList<String>();
	
	public Errors() {
	}
	
	public static Errors empty(){
		return new Errors();
	}
	
	public boolean hasError() {
		return errors.size() > 0;
	}

	public void add(String error_msg){
		errors.add(error_msg);
	}

	public List<String> getErrors() {
		return errors;
	}
	
}
