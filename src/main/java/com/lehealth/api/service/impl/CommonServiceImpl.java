package com.lehealth.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.CommonDao;
import com.lehealth.api.entity.Activity;
import com.lehealth.api.entity.DiseaseCategroy;
import com.lehealth.api.entity.MedicineCategroy;
import com.lehealth.api.service.CommonService;

@Service("commonService")
public class CommonServiceImpl implements CommonService{

	@Autowired
	@Qualifier("commonDao")
	private CommonDao commonDao;
	
	@Override
	public List<Activity> getAtivities() {
		return this.commonDao.selectAtivities();
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
