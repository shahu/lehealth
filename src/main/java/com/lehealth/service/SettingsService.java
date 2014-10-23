package com.lehealth.service;

import java.util.List;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;

public interface SettingsService {

	//获取血压控制设置
	public BloodpressureConfig getBloodpressureSetting();
	
	//更新血压控制设置
	public String modifyBloodpressureSetting();
	
	//获取用药设置
	public List<MedicineConfig> getMedicineSettings();
	
	//更新用药设置
	public String modifyMedicineSetting();
	
	//TODO 获取个人信息
	
	//TODO 更新个人信息
	
}
