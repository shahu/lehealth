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
	public List<MedicineInfo> getMedicineHistory(String userId){
		return this.medicineDao.selectMedicineHistory(userId);
	}
	
	@Override
	public boolean updateMedicineHistory(MedicineInfo info){
		return this.medicineDao.updateMedicineHistory(info);
	}
	@Override
	public List<MedicineCategroy> getMedicines() {
		return this.medicineDao.selectMedicines();
	}

	
}
