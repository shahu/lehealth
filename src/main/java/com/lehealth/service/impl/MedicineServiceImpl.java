package com.lehealth.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineInfo;
import com.lehealth.bean.MedicineResult;
import com.lehealth.dao.MedicineDao;
import com.lehealth.service.MedicineService;

@Service("medicineService")
public class MedicineServiceImpl implements MedicineService{
	
	@Autowired
	@Qualifier("medicineDao")
	private MedicineDao medicineDao;
	
	private static Logger logger = Logger.getLogger(MedicineServiceImpl.class);

	@Override
	public MedicineResult getMedicineRecords(String userId) {
		MedicineResult result=new MedicineResult();
		result.setScore(47);
		result.setStatus(1);
		List<MedicineInfo> list=this.medicineDao.selectMedicineRecords(userId);
		result.setMedicineTimes(list);
		return result;
	}

	@Override
	public boolean modifyMedicineRecord(MedicineInfo mInfo) {
		return this.medicineDao.insertMedicineRecord(mInfo);
	}

	@Override
	public List<MedicineCategroy> getMedicines() {
		return this.medicineDao.selectMedicines();
	}

	
}