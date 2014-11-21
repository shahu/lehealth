package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.bean.Doctor;

public interface DoctorDao {
	
	//获取医生列表
	public List<Doctor> selectDoctors(String userId);
	
	//获取医生信息
	public Doctor selectDoctor(String userId,int doctorId);
	
	//关注医生
	public boolean cancelAttentionDoctor(String userId,int doctorId);
	
	//取消关注医生
	public boolean attentionDoctor(String userId,int doctorId);
}
