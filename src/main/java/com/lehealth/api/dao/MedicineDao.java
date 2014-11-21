package com.lehealth.api.dao;

import java.util.Map;

import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineInfo;

public interface MedicineDao {
	
	//获取用药记录
	public Map<Integer,MedicineInfo> selectMedicineHistory(String userId);
	
	//更新用药记录
	public boolean updateMedicineHistory(MedicineInfo info);
	
	//获取用药设置
	public Map<Integer,MedicineConfig> selectMedicineSettings(String userId);
	
	//删除用药设置
	public boolean deleteMedicineSetting(String userId,int medicineId);
	
	//插入用药设置
	public boolean insertMedicineSetting(MedicineConfig mConfig);
}
