package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.api.entity.PanientGuardianInfo;
import com.lehealth.api.entity.PanientInfo;
import com.lehealth.api.entity.UserBaseInfo;

public interface PanientDao {
	
	// 获取个人信息
	public PanientInfo selectPanient(String userId);
	
	// 更新个人信息
	public boolean updatePanient(PanientInfo info);
	
	// 获取监护人列表
	public List<PanientGuardianInfo> selectGuardianList(String userId);
	
	// 新增监护人信息
	public boolean insertGuardian(PanientGuardianInfo info);
	
	// 删除监护人信息
	public boolean deleteGuardian(String userId,String guardianNumber);
	
	// 获取被监护人列表
	public List<PanientInfo> selectPanientListByGuardian(String guardianPhoneNumber);
	
	//获取被关注病人列表
	public List<PanientInfo> selectPanientListByRole(UserBaseInfo user);
}
