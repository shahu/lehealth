package com.lehealth.api.dao;

import java.util.List;
import java.util.Map;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.DiseaseHistory;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;

public interface SettingsDao {

	//获取血压控制设置
	public BloodpressureConfig selectBloodpressureSetting(String userId);
	
	//更新血压控制设置
	public boolean updateBloodpressureSetting(BloodpressureConfig bpConfig);
	
	//获取用药设置
	public Map<Integer,MedicineConfig> selectMedicineSettings(String userId);
	
	//删除用药设置
	public boolean deleteMedicineSetting(String userId,int medicineId);
	
	//插入用药设置
	public boolean insertMedicineSetting(MedicineConfig mConfig);
	
	//获取个人信息
	public UserInfo selectUserInfo(String userId);
	
	//更新个人信息
	public boolean updateUserInfo(UserInfo info);
	
	//获取监护人信息
	public UserGuardianInfo selectUserGuardianInfo(String userId);
	
	//更新监护人信息
	public boolean updateUserGuardianInfo(UserGuardianInfo info);
	
	//获取病例
	public List<DiseaseHistory> selectDiseaseHistorys(String userId);
	
	//更新病例
	public boolean updateDiseaseHistory(DiseaseHistory diseaseHistory);
	
	//获取关注医生
	public List<Doctor> selectAttentionDoctor(String userId);
	
	//关注医生
	public boolean cancelAttentionDoctor(String userId,int doctorId);
	
	//取消关注医生
	public boolean attentionDoctor(String userId,int doctorId);
}
