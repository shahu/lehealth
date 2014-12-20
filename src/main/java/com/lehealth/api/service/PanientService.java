package com.lehealth.api.service;

import java.util.List;

import com.lehealth.bean.PanientGuardianInfo;
import com.lehealth.bean.PanientInfo;

public interface PanientService {

	//获取个人信息
	public PanientInfo getInfo(String userId);
	
	//更新个人信息
	public boolean modifyInfo(PanientInfo info);
	
	//获取监护人信息
	public List<PanientGuardianInfo> getGuardianInfos(String userId);
	
	//新增监护人信息
	public boolean modifyGuardianInfo(PanientGuardianInfo info);
	
	//删除监护人信息
	public boolean deleteGuardianInfo(String userId,String guardianNumber);
	
	//获取被关注病人列表
	public List<PanientInfo> getPanientList(String doctorId);
}
