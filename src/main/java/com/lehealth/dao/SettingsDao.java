package com.lehealth.dao;

import java.util.List;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;

public interface SettingsDao {

	//获取血压控制设置
	public BloodpressureConfig selectBloodpressureSetting();
	
	//更新血压控制设置
	public String updateBloodpressureSetting();
	
	//获取用药设置
	public List<MedicineConfig> selectMedicineSettings();
	
	//更新用药设置
	public String updateMedicineSetting();
	
	//TODO 获取个人信息
	
	//TODO 更新个人信息
	
}
