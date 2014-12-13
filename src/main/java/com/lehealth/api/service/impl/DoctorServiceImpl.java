package com.lehealth.api.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.DoctorDao;
import com.lehealth.api.service.DoctorService;
import com.lehealth.bean.Doctor;

@Service("doctorService")
public class DoctorServiceImpl implements DoctorService{

	@Autowired
	@Qualifier("doctorDao")
	private DoctorDao doctorDao;
	
	private static Logger logger = Logger.getLogger(DoctorServiceImpl.class);

	@Override
	public Doctor getDoctor(String userId,String doctorId) {
		return this.doctorDao.selectDoctor(userId,doctorId);
	}

	@Override
	public List<Doctor> getDoctors(String userId) {
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
