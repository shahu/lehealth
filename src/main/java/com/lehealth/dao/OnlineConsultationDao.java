package com.lehealth.dao;

import java.util.List;
import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;

public interface OnlineConsultationDao {
	
	//获取医生列表
	public List<Doctor> selectDoctors();
	
	//获取医生信息
	public Doctor selectDoctorById(int doctorId);
	
	//获取活动列表
	public List<Activitie> selectAtivities();
	
}
