package com.sunny.teamob.util;

public class SimpleException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private SimpleResponse response = null;
	
	public SimpleException(SimpleResponse response){
		this.response = response;
	}
	
	public SimpleResponse getResponse() {
		return response;
	}

}
