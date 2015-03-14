package com.lehealth.api.service;

import com.lehealth.data.bean.UserInfomation;
import com.lehealth.data.type.ErrorCodeType;

public interface LoginService {
	
	//注册新用户
	public ErrorCodeType registerUser(String loginId,String password,int roleId);
	
	//登录判断
	public ErrorCodeType checkUser4Login(String loginId,String password);
	
	//token校验获取userid
	public UserInfomation getUserBaseInfo(String loginId,String token);
	
	//获取用户身份信息
	public UserInfomation getUserBaseInfo(String loginId);
		
}
