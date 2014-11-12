package com.lehealth.dao;

import java.util.List;
import java.util.Map;

import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineInfo;

public interface MedicineDao {
	
	//获取用药记录
	public Map<Integer,MedicineInfo> selectMedicineHistory(String userId);
	//更新用药记录
	public boolean updateMedicineHistory(MedicineInfo info);
	//获取药物列表
	public List<MedicineCategroy> selectMedicines();
	
}
