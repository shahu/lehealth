package com.lehealth.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.DoctorDao;
import com.lehealth.api.entity.DoctorInfo;
import com.lehealth.api.service.DoctorService;

@Service("doctorService")
public class DoctorServiceImpl implements DoctorService{

	@Autowired
	@Qualifier("doctorDao")
	private DoctorDao doctorDao;
	
	@Override
	public DoctorInfo getInfo(String userId,String doctorId) {
		return this.doctorDao.selectDoctor(userId,doctorId);
	}

	@Override
	public List<DoctorInfo> getInfoList(String userId) {
		return this.doctorDao.selectDoctors(userId);
	}
	
	@Override
	public boolean modifyAttentionStatus(String userId,String doctorId,int status){
		//取消关注
		if(status==0){
			return this.doctorDao.cancelAttentionDoctor(userId,doctorId);
		//关注
		}else{
			return this.doctorDao.attentionDoctor(userId,doctorId);
		}
	}
}
