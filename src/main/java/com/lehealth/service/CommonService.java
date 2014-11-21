package com.lehealth.service;

import java.util.List;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.DiseaseCategroy;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineCategroy;

public interface CommonService {
	
	//获取医生列表
	public List<Doctor> getDoctors();
	
	//获取医生列表
	public Doctor getDoctor(int doctorId);
	
	//获取活动列表
	public List<Activitie> getAtivities();
	
	//获取药物列表
	public List<MedicineCategroy> getMedicines();
	
	//获取疾病列表
	public List<DiseaseCategroy> getDiseases();
}
