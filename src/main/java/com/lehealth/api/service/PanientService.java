package com.lehealth.api.service;

import java.util.List;

import com.lehealth.api.entity.PanientGuardianInfo;
import com.lehealth.api.entity.PanientInfo;

public interface PanientService {

	// 获取个人信息
	public PanientInfo getPanient(String userId);
	
	// 更新个人信息
	public boolean modifyPanient(PanientInfo info);
	
	// 获取监护人列表
	public List<PanientGuardianInfo> getGuardianList(String userId);
	
	// 新增监护人信息
	public boolean modifyGuardian(PanientGuardianInfo info);
	
	// 删除监护人信息
	public boolean deleteGuardian(String userId,String guardianNumber);
	
	// 获取被监护人列表
	public List<PanientInfo> getPanientListByGuardian(String phoneNumber);
	
	// 医生获取被关注病人列表
	public List<PanientInfo> getPanientListByDoctor(String doctorId);
}
