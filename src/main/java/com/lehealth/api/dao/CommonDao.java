package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.data.bean.Activity;
import com.lehealth.data.bean.DiseaseCategroy;
import com.lehealth.data.bean.MedicineCategroy;

public interface CommonDao {
	
	//获取活动列表
	public List<Activity> selectAtivities();
	
	//获取药物列表
	public List<MedicineCategroy> selectMedicines();
	
	//获取疾病列表
	public List<DiseaseCategroy> selectDiseases();
}
