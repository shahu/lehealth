package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.bean.PanientGuardianInfo;
import com.lehealth.bean.PanientInfo;

public interface PanientDao {
	
	//获取个人信息
	public PanientInfo selectInfo(String userId);
	
	//更新个人信息
	public boolean updateInfo(PanientInfo info);
	
	//获取监护人信息
	public List<PanientGuardianInfo> selectGuardianInfos(String userId);
	
	//新增监护人信息
	public boolean insertGuardianInfo(PanientGuardianInfo info);
	
	//删除监护人信息
	public boolean deleteGuardianInfo(String userId,String guardianNumber);
	
	//获取被关注病人列表
	public List<PanientInfo> selectPanients(String doctorId);
}
