package com.lehealth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;
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
		Map<Integer,MedicineConfig> map=this.settingsDao.selectMedicineSettings(userId);
		List<MedicineConfig> list=new ArrayList<MedicineConfig>(map.values());
		return list;
	}
	@Override
	public boolean modifyMedicineSetting(MedicineConfig mConfig) {
		//先删除
		this.settingsDao.deleteMedicineSetting(mConfig.getUserid(),mConfig.getMedicineid());
		//再插入
		return this.settingsDao.insertMedicineSetting(mConfig);
	}

	@Override
	public boolean delMedicineSetting(String userId, int medicineId) {
		return this.settingsDao.deleteMedicineSetting(userId,medicineId);
	}

	//获取个人信息
	@Override
	public UserInfo getUserInfo(String userId) {
		return this.settingsDao.selectUserInfo(userId);
	}

	//更新个人信息
	@Override
	public boolean modifyUserInfo(UserInfo info) {
		return this.settingsDao.updateUserInfo(info);
	}

	@Override
	public UserGuardianInfo getUserGuardianInfo(String userId) {
		return this.settingsDao.selectUserGuardianInfo(userId);
	}

	@Override
	public boolean modifyUserGuardianInfo(UserGuardianInfo info) {
		return this.settingsDao.updateUserGuardianInfo(info);
	}
}
