package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.bean.PanientGuardianInfo;
import com.lehealth.bean.PanientInfo;

public interface PanientDao {
	
	//获取个人信息
	public PanientInfo selectUserInfo(String userId);
	
	//更新个人信息
	public boolean updateUserInfo(PanientInfo info);
	
	//获取监护人信息
	public List<PanientGuardianInfo> selectUserGuardianInfos(String userId);
	
	//新增监护人信息
	public boolean insertUserGuardianInfo(PanientGuardianInfo info);
	
	//删除监护人信息
	public boolean deleteUserGuardianInfo(String userId,String guardianNumber);
}
