package com.lehealth.api.dao;

import java.util.List;
import java.util.Map;

import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineInfo;

public interface MedicineDao {
	
	//获取用药记录
	public Map<Integer,MedicineInfo> selectMedicineTodayRecords(String userId);
	
	//获取今日用药记录
	public List<MedicineInfo> selectMedicineRecords(String userId,int days);
	
	//更新用药记录
	public boolean updateMedicineRecord(MedicineInfo info);
	
	//获取用药设置
	public Map<Integer,MedicineConfig> selectMedicineConfigs(String userId);
	
	//删除用药设置
	public boolean deleteMedicineConfig(String userId,int medicineId);
	
	//插入用药设置
	public boolean insertMedicineConfig(MedicineConfig mConfig);
}
