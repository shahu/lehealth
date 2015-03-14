package com.lehealth.api.dao;

import com.lehealth.data.bean.UserInfomation;

public interface LoginDao {
	
	//获取用户信息
	public boolean checkUser4Login(String loginId,String pwdmd5);
	
	//获取用户id
	public UserInfomation getUser(String loginId);
	
	//注册新用户
	public boolean insertUser(UserInfomation user);
	
}
