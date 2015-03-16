package com.lehealth.api.dao;

import java.util.List;
import java.util.Map;

import com.lehealth.data.bean.MedicineConfig;
import com.lehealth.data.bean.MedicineRecord;

public interface MedicineDao {
	
	//获取今日用药记录
	public Map<Integer,MedicineRecord> selectTodayRecords(String userId);
	
	//获取用药记录
	public List<MedicineRecord> selectRecords(String userId,int days);
	
	//更新用药记录
	public boolean updateRecord(MedicineRecord info);
	
	//获取用药设置
	public Map<Integer,MedicineConfig> selectConfigs(String userId);
	
	//删除用药设置
	public boolean deleteConfig(String userId,int medicineId);
	
	//插入用药设置
	public boolean insertConfig(MedicineConfig mConfig);
}
