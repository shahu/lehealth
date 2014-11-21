package com.lehealth.api.service;

import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;

public interface UserService {

	//获取个人信息
	public UserInfo getUserInfo(String userId);
	
	//更新个人信息
	public boolean modifyUserInfo(UserInfo info);
	
	//获取个人信息
	public UserGuardianInfo getUserGuardianInfo(String userId);
	
	//更新个人信息
	public boolean modifyUserGuardianInfo(UserGuardianInfo info);
	
}
