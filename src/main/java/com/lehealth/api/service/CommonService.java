package com.lehealth.api.service;

import java.util.List;

import com.lehealth.api.entity.Activity;
import com.lehealth.api.entity.DiseaseCategroy;
import com.lehealth.api.entity.MedicineCategroy;

public interface CommonService {
	
	//获取活动列表
	public List<Activity> getAtivities();
	
	//获取药物列表
	public List<MedicineCategroy> getMedicines();
	
	//获取疾病列表
	public List<DiseaseCategroy> getDiseases();
	
}
