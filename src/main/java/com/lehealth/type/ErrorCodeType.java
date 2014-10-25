package com.lehealth.type;

public enum ErrorCodeType {
	normal(0,""),
	invalidUser(1,"用户名或密码错误"),
	invalidToken(1,"请重新登录"),
	repeatUser(2,"用户名重复，请重新输入"),
	abnormal(3,"数据异常，请稍后再试");
	
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
