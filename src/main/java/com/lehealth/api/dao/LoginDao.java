package com.lehealth.api.dao;

import com.lehealth.bean.User;

public interface LoginDao {
	
	//获取用户信息
	public boolean checkUser4Login(String loginId,String pwdmd5);
	
	//获取用户id
	public User getUser(String loginId);
	
	//注册新用户
	public boolean insertUser(User user);
	
}
