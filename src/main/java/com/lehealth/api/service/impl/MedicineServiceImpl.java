package com.lehealth.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.MedicineDao;
import com.lehealth.api.entity.MedicineConfig;
import com.lehealth.api.entity.MedicineRecord;
import com.lehealth.api.service.MedicineService;
import com.lehealth.common.util.ComparatorUtils;

@Service("medicineService")
public class MedicineServiceImpl implements MedicineService{
	
	@Autowired
	@Qualifier("medicineDao")
	private MedicineDao medicineDao;
	
	@Override
	public List<MedicineRecord> getTodayRecords(String userId){
		Map<Integer,MedicineRecord> map = this.medicineDao.selectTodayRecords(userId);
		List<MedicineRecord> list = new ArrayList<MedicineRecord>(map.values());
		Collections.sort(list, ComparatorUtils.medicineComparator);
		return list;
	}
	
	@Override
	public List<MedicineRecord> getHistoryRecords(String userId,int days){
		List<MedicineRecord> list = this.medicineDao.selectRecords(userId, days);
		Collections.sort(list, ComparatorUtils.medicineComparator);
		return list;
	}
	
	@Override
	public boolean addRecord(MedicineRecord info){
		return this.medicineDao.updateRecord(info);
	}
	
	@Override
	public List<MedicineConfig> getConfigs(String userId) {
		Map<Integer,MedicineConfig> map=this.medicineDao.selectConfigs(userId);
		List<MedicineConfig> list=new ArrayList<MedicineConfig>(map.values());
		return list;
	}
	
	@Override
	public boolean modifyConfig(MedicineConfig mConfig) {
		//先删除
		this.medicineDao.deleteConfig(mConfig.getUserId(),mConfig.getMedicineId());
		//再插入
		return this.medicineDao.insertConfig(mConfig);
	}

	@Override
	public boolean deleteConfig(String userId, int medicineId) {
		return this.medicineDao.deleteConfig(userId,medicineId);
	}
}
