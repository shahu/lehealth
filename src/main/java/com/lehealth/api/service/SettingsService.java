package com.lehealth.api.service;

import java.util.List;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.DiseaseHistory;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;

public interface SettingsService {

	//获取血压控制设置
	public BloodpressureConfig getBloodpressureSetting(String userId);
	
	//更新血压控制设置
	public boolean modifyBloodpressureSetting(BloodpressureConfig bpConfig);
	
	//获取用药设置
	public List<MedicineConfig> getMedicineSettings(String userId);
	
	//更新用药设置
	public boolean modifyMedicineSetting(MedicineConfig mConfig);
	
	//删除用药设置
	public boolean delMedicineSetting(String userId,int medicineId);
	
	//获取个人信息
	public UserInfo getUserInfo(String userId);
	
	//更新个人信息
	public boolean modifyUserInfo(UserInfo info);
	
	//获取个人信息
	public UserGuardianInfo getUserGuardianInfo(String userId);
	
	//更新个人信息
	public boolean modifyUserGuardianInfo(UserGuardianInfo info);
	
	//获取病例
	public List<DiseaseHistory> getDiseaseHistorys(String userId);
	
	//更新病例
	public boolean modifyDiseaseHistory(DiseaseHistory diseaseHistory);
	
	//获取关注医生
	public List<Doctor> getAttentionDoctor(String userId);
	
	//关注或取消关注医生
	public boolean attentionDoctor(String userId,int doctorId,int status);
}
