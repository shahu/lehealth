package com.lehealth.dao;

import java.util.List;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.DiseaseCategroy;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineCategroy;

public interface CommonDao {
	
	//获取医生列表
	public List<Doctor> selectDoctors();
	
	//获取医生信息
	public Doctor selectDoctorById(int doctorId);
	
	//获取活动列表
	public List<Activitie> selectAtivities();
	
	//获取药物列表
	public List<MedicineCategroy> selectMedicines();
	
	//获取疾病列表
	public List<DiseaseCategroy> selectDiseases();
}
