package com.lehealth.dao;

import com.lehealth.bean.UserInfo;

public interface LoginDao {
	
	//获取用户信息
	public UserInfo selectUserInfo();
	
	//注册新用户
	public String insertUser();
	
}
