package com.lehealth.service;

import java.util.List;
import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;

public interface OnlineConsultationService {
	
	//获取医生列表
	public List<Doctor> getDoctors();
	
	//获取活动列表
	public List<Activitie> getAtivities();
	
}
