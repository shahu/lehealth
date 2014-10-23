package com.lehealth.dao;

import java.util.List;
import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineResult;

public interface MedicineDao {
	
	//获取用药记录
	public MedicineResult selectMedicineRecords();
	
	//更新用药记录
	public String insertMedicineRecord();
	
	//获取药物列表
	public List<MedicineCategroy> selectMedicines();
	
}
