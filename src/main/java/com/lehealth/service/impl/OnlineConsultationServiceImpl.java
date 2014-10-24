package com.lehealth.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;
import com.lehealth.dao.OnlineConsultationDao;
import com.lehealth.service.OnlineConsultationService;

@Service("onlineConsultationService")
public class OnlineConsultationServiceImpl implements OnlineConsultationService{

	@Autowired
	@Qualifier("onlineConsultationDao")
	private OnlineConsultationDao onlineConsultationDao;
	
	private static Logger logger = Logger.getLogger(OnlineConsultationServiceImpl.class);

	public List<Doctor> getDoctors() {
		return this.onlineConsultationDao.selectDoctors();
	}

	public List<Activitie> getAtivities() {
		return this.onlineConsultationDao.selectAtivities();
	}
	
}
