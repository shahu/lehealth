package com.lehealth.api.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.CommonDao;
import com.lehealth.api.dao.DiseaseDao;
import com.lehealth.api.service.CommonService;
import com.lehealth.api.service.DiseaseService;
import com.lehealth.bean.Activitie;
import com.lehealth.bean.DiseaseCategroy;
import com.lehealth.bean.DiseaseHistory;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineCategroy;

@Service("diseaseService")
public class DiseaseServiceImpl implements DiseaseService{

	@Autowired
	@Qualifier("diseaseDao")
	private DiseaseDao diseaseDao;
	
	private static Logger logger = Logger.getLogger(DiseaseServiceImpl.class);

	@Override
	public List<DiseaseHistory> getDiseaseHistorys(String userId) {
		return this.diseaseDao.selectDiseaseHistorys(userId);
	}

	@Override
	public boolean modifyDiseaseHistory(DiseaseHistory diseaseHistory) {
		return this.diseaseDao.updateDiseaseHistory(diseaseHistory);
	}
}
