package com.lehealth.api.service;

import java.util.List;

import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineRecord;

public interface MedicineService {
	
	//获取今日用药记录
	public List<MedicineRecord> getTodayRecords(String userId);
	
	//获取过去几天用药记录
	public List<MedicineRecord> getRecords(String userId,int days);
	
	//更新用药记录
	public boolean addRecord(MedicineRecord info);
	
	//获取用药设置
	public List<MedicineConfig> getConfigs(String userId);
	
	//更新用药设置
	public boolean modifyConfig(MedicineConfig mConfig);
	
	//删除用药设置
	public boolean deleteConfig(String userId,int medicineId);
}
