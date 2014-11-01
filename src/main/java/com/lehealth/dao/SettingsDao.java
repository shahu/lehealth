package com.lehealth.dao;

import java.util.List;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.UserInfo;

public interface SettingsDao {

	//获取血压控制设置
	public BloodpressureConfig selectBloodpressureSetting(String userId);
	
	//更新血压控制设置
	public boolean updateBloodpressureSetting(BloodpressureConfig bpConfig);
	
	//获取用药设置
	public List<MedicineConfig> selectMedicineSettings(String userId);
	
	//更新用药设置
	public boolean updateMedicineSetting(MedicineConfig mConfig);
	
	//删除用药设置
	public boolean deleteMedicineSetting(String userId,int medicineId);
	
	//获取个人信息
	public UserInfo selectUserInfo(String userId);
	
	//TODO 更新个人信息
	public boolean updateUserInfo(UserInfo info);
}
