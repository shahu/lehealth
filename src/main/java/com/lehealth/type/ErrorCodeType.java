package com.lehealth.type;

public enum ErrorCodeType {
	normal(0,""),
	invalidUser(1,"invalid user"),
	invalidToken(1,"invalid Token"),
	repeatUser(2,"repeat loginid"),
	abnormal(3,"eroor");
	
	private ErrorCodeType(int code,String message) {
		this.code = code;
		this.message = message;
	}
	
	private final int code;
	private final String message;
	
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
