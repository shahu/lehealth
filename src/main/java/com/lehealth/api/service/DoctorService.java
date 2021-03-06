package com.lehealth.api.service;

import java.util.List;

import com.lehealth.api.entity.DoctorInfo;

public interface DoctorService {

	//获取医生详情
	public DoctorInfo getInfo(String userId,String doctorId);
	
	//获取关注医生
	public List<DoctorInfo> getInfoList(String userId);
	
	//关注或取消关注医生
	public boolean modifyAttentionStatus(String userId,String doctorId,int status);
}
