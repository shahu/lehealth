package com.lehealth.service;

import com.lehealth.bean.UserInfo;

public interface LoginService {
	
	//获取用户信息
	public UserInfo getUserInfo();
	
	//注册新用户
	public String registerNewUser();
	
}
