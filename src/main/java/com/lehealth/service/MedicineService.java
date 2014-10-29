package com.lehealth.service;

import java.util.List;

import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineInfo;
import com.lehealth.bean.MedicineResult;

public interface MedicineService {
	
	//获取用药记录
	public MedicineResult getMedicineRecords(String userId);
	//更新用药记录-新
	public boolean updateMedicineHistory(MedicineInfo info);
	//更新用药记录
	public boolean modifyMedicineRecord(MedicineInfo mInfo);
	
	//获取药物列表
	public List<MedicineCategroy> getMedicines();
	
}
