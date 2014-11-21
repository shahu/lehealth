package com.lehealth.service;

import java.util.List;

import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineInfo;

public interface MedicineService {
	
	//获取用药记录-新
	public List<MedicineInfo> getMedicineHistory(String userId);
	//更新用药记录-新
	public boolean updateMedicineHistory(MedicineInfo info);
	
}
