package com.lehealth.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.dao.SettingsDao;
import com.lehealth.service.SettingsService;

@Service("settingsService")
public class SettingsServiceImpl implements SettingsService{

	@Autowired
	@Qualifier("settingsDao")
	private SettingsDao settingsDao;
	
	private static Logger logger = Logger.getLogger(SettingsServiceImpl.class);

	@Override
	public BloodpressureConfig getBloodpressureSetting(String userId) {
		return this.settingsDao.selectBloodpressureSetting(userId);
	}

	@Override
	public boolean modifyBloodpressureSetting(BloodpressureConfig bpConfig) {
		return this.settingsDao.updateBloodpressureSetting(bpConfig);
	}

	@Override
	public List<MedicineConfig> getMedicineSettings(String userId) {
		return this.settingsDao.selectMedicineSettings(userId);
	}

	@Override
	public boolean modifyMedicineSetting(MedicineConfig mConfig) {
		return this.settingsDao.updateMedicineSetting(mConfig);
	}

	@Override
	public boolean delMedicineSetting(String userId, int medicineId) {
		return this.settingsDao.deleteMedicineSetting(userId,medicineId);
	}

	
	//TODO 获取个人信息
	
	//TODO 更新个人信息
	
}
