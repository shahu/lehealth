package com.lehealth.api.service;

import com.lehealth.type.ErrorCodeType;

public interface LoginService {
	
	//注册新用户
	public ErrorCodeType registerNewUser(String loginId,String password);
	
	//登录判断
	public ErrorCodeType checkUser4Login(String loginId,String password);
	
	//token校验获取userid
	public String checkUser4Token(String loginId,String token);
		
}
