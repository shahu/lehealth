package com.lehealth.api.service;

import java.util.List;

import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineInfo;

public interface MedicineService {
	
	//获取用药记录
	public List<MedicineInfo> getMedicineHistory(String userId);
	
	//更新用药记录
	public boolean updateMedicineHistory(MedicineInfo info);
	
	//获取用药设置
	public List<MedicineConfig> getMedicineSettings(String userId);
	
	//更新用药设置
	public boolean modifyMedicineSetting(MedicineConfig mConfig);
	
	//删除用药设置
	public boolean delMedicineSetting(String userId,int medicineId);
}
