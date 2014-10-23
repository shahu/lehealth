package com.lehealth.service;

import java.util.List;
import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineResult;

public interface MedicineService {
	
	//获取用药记录
	public MedicineResult getMedicineRecords();
	
	//更新用药记录
	public String modifyMedicineRecord();
	
	//获取药物列表
	public List<MedicineCategroy> getMedicines();
	
}
