package com.lehealth.api.dao;

import java.util.Map;
import com.lehealth.bean.MedicineInfo;

public interface MedicineDao {
	
	//获取用药记录
	public Map<Integer,MedicineInfo> selectMedicineHistory(String userId);
	//更新用药记录
	public boolean updateMedicineHistory(MedicineInfo info);
	
}
