package com.lehealth.service;

import com.lehealth.type.ErrorCodeType;

public interface LoginService {
	
	//注册新用户
	public ErrorCodeType registerNewUser(String loginId,String password);
	
	//获取用户token
	public ErrorCodeType getUser(String loginId,String password);
	
	//校验用户
	public String getUserId(String loginId,String token);
}
