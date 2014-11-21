package com.lehealth.api.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.CommonDao;
import com.lehealth.api.service.CommonService;
import com.lehealth.bean.Activitie;
import com.lehealth.bean.DiseaseCategroy;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineCategroy;

@Service("commonService")
public class CommonServiceImpl implements CommonService{

	@Autowired
	@Qualifier("commonDao")
	private CommonDao commonDao;
	
	private static Logger logger = Logger.getLogger(CommonServiceImpl.class);

	@Override
	public List<Doctor> getDoctors() {
		return this.commonDao.selectDoctors();
	}

	@Override
	public List<Activitie> getAtivities() {
		return this.commonDao.selectAtivities();
	}

	@Override
	public Doctor getDoctor(int doctorId) {
		return this.commonDao.selectDoctorById(doctorId);
	}
	
	@Override
	public List<MedicineCategroy> getMedicines() {
		return this.commonDao.selectMedicines();
	}
	
	@Override
	public List<DiseaseCategroy> getDiseases() {
		return this.commonDao.selectDiseases();
	}
}
