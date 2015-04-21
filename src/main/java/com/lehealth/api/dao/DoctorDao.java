package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.api.entity.DoctorInfo;

public interface DoctorDao {
	
	//获取医生列表
	public List<DoctorInfo> selectDoctors(String userId);
	
	//获取医生信息
	public DoctorInfo selectDoctor(String userId,String doctorId);
	
	//关注医生
	public boolean cancelAttentionDoctor(String userId,String doctorId);
	
	//取消关注医生
	public boolean attentionDoctor(String userId,String doctorId);
}
