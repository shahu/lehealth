package com.lehealth.api.service;

import java.util.List;

import com.lehealth.data.bean.Activity;
import com.lehealth.data.bean.DiseaseCategroy;
import com.lehealth.data.bean.MedicineCategroy;

public interface CommonService {
	
	//获取活动列表
	public List<Activity> getAtivities();
	
	//获取药物列表
	public List<MedicineCategroy> getMedicines();
	
	//获取疾病列表
	public List<DiseaseCategroy> getDiseases();
}
