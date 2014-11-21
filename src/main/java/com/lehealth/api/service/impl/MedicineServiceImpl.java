package com.lehealth.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.MedicineDao;
import com.lehealth.api.service.MedicineService;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineInfo;

@Service("medicineService")
public class MedicineServiceImpl implements MedicineService{
	
	@Autowired
	@Qualifier("medicineDao")
	private MedicineDao medicineDao;
	
	private static Logger logger = Logger.getLogger(MedicineServiceImpl.class);

	@Override
	public List<MedicineInfo> getMedicineHistory(String userId){
		Map<Integer,MedicineInfo> map=this.medicineDao.selectMedicineHistory(userId);
		List<MedicineInfo> list=new ArrayList<MedicineInfo>(map.values());
		return list;
	}
	
	@Override
	public boolean updateMedicineHistory(MedicineInfo info){
		return this.medicineDao.updateMedicineHistory(info);
	}
	
	@Override
	public List<MedicineConfig> getMedicineSettings(String userId) {
		Map<Integer,MedicineConfig> map=this.medicineDao.selectMedicineSettings(userId);
		List<MedicineConfig> list=new ArrayList<MedicineConfig>(map.values());
		return list;
	}
	
	@Override
	public boolean modifyMedicineSetting(MedicineConfig mConfig) {
		//先删除
		this.medicineDao.deleteMedicineSetting(mConfig.getUserId(),mConfig.getMedicineId());
		//再插入
		return this.medicineDao.insertMedicineSetting(mConfig);
	}

	@Override
	public boolean delMedicineSetting(String userId, int medicineId) {
		return this.medicineDao.deleteMedicineSetting(userId,medicineId);
	}
}
