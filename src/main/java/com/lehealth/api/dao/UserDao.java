package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;

public interface UserDao {
	
	//获取个人信息
	public UserInfo selectUserInfo(String userId);
	
	//更新个人信息
	public boolean updateUserInfo(UserInfo info);
	
	//获取监护人信息
	public List<UserGuardianInfo> selectUserGuardianInfos(String userId);
	
	//新增监护人信息
	public boolean insertUserGuardianInfo(UserGuardianInfo info);
	
	//删除监护人信息
	public boolean deleteUserGuardianInfo(String userId,String guardianNumber);
}
