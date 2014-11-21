package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.DiseaseCategroy;
import com.lehealth.bean.MedicineCategroy;

public interface CommonDao {
	
	//获取活动列表
	public List<Activitie> selectAtivities();
	
	//获取药物列表
	public List<MedicineCategroy> selectMedicines();
	
	//获取疾病列表
	public List<DiseaseCategroy> selectDiseases();
}
