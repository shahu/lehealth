package com.lehealth.data.type;

public enum ErrorCodeType {
	success(0,""),
	
	invalidPassword(101,"用户名或密码错误"),
	invalidToken(102,"请重新登录"),
	invalidParam(103,"请求参数错误"),
	
	invalidPhoneNumber(111,"不是正确的手机号"),
	repeatPhoneNumber(112,"手机号已注册"),
	
	invalidIdentifyingCode(111,"验证码错误"),
	expireIdentifyingCode(112,"验证码过期"),
	failedIdentifyingCode(113,"获取验证码失败"),
	muchIdentifyingCode(114,"获取验证码频繁"),
	
	failed(199,"数据异常，请稍后再试");
	
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
