package com.lehealth.api.service;

import java.util.List;
import com.lehealth.bean.Doctor;

public interface DoctorService {

	//获取医生详情
	public Doctor getDoctor(String userId,String doctorId);
	
	//获取关注医生
	public List<Doctor> getDoctors(String userId);
	
	//关注或取消关注医生
	public boolean modifyAttentionStatus(String userId,String doctorId,int status);
}
