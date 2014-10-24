package com.lehealth.dao;

import com.lehealth.bean.User;

public interface LoginDao {
	
	//获取用户信息
	public boolean checkUser4Login(String loginId,String pwdmd5);
	
	//获取用户id
	public String getUserId(String loginId,String token);
	
	//注册新用户
	public boolean insertUser(User user);
	
	//获取用户
	public boolean checkLoginId(String loginId);
	
}
