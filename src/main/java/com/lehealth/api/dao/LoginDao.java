package com.lehealth.api.dao;

import com.lehealth.api.entity.UserBaseInfo;

public interface LoginDao {
	
	//获取用户基本信息
	public UserBaseInfo selectUserBaseInfo(String loginId);
	
	//获取用户基本信息
	public UserBaseInfo selectUserBaseInfo(String loginId, String pwdmd5);
	
	//注册新用户
	public boolean insertUser(UserBaseInfo user);
	
}
