package com.lehealth.api.service;

import java.util.List;

import com.lehealth.bean.PanientGuardianInfo;
import com.lehealth.bean.PanientInfo;

public interface PanientService {

	//获取个人信息
	public PanientInfo getUserInfo(String userId);
	
	//更新个人信息
	public boolean modifyUserInfo(PanientInfo info);
	
	//获取监护人信息
	public List<PanientGuardianInfo> getUserGuardianInfos(String userId);
	
	//新增监护人信息
	public boolean modifyUserGuardianInfo(PanientGuardianInfo info);
	
	//删除监护人信息
	public boolean delUserGuardianInfo(String userId,String guardianNumber);
}
