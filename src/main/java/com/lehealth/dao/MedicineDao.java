package com.lehealth.dao;

import java.util.List;

import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineInfo;

public interface MedicineDao {
	
	//获取用药记录
	public List<MedicineInfo> selectMedicineRecords(String userId);
	
	//更新用药记录
	public boolean updateMedicineRecord(MedicineInfo info);
	
	//获取药物列表
	public List<MedicineCategroy> selectMedicines();
	
}
