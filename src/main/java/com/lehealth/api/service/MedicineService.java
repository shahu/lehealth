package com.lehealth.api.service;

import java.util.List;

import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineInfo;

public interface MedicineService {
	
	//获取今日用药记录
	public List<MedicineInfo> getMedicineTodayRecords(String userId);
	
	//更新用药记录
	public boolean updateMedicineRecord(MedicineInfo info);
	
	//获取用药设置
	public List<MedicineConfig> getMedicineConfigs(String userId);
	
	//更新用药设置
	public boolean modifyMedicineConfig(MedicineConfig mConfig);
	
	//删除用药设置
	public boolean delMedicineConfig(String userId,int medicineId);
}
