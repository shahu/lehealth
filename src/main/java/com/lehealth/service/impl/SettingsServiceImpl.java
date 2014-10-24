package com.lehealth.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.service.SettingsService;

@Service("settingsService")
public class SettingsServiceImpl implements SettingsService{

	private static Logger logger = Logger.getLogger(SettingsServiceImpl.class);

	public BloodpressureConfig getBloodpressureSetting(String userId) {
		
		return null;
	}

	public boolean modifyBloodpressureSetting(BloodpressureConfig bpConfig) {
		
		return null;
	}

	public List<MedicineConfig> getMedicineSettings(String userId) {
		
		return null;
	}

	public boolean modifyMedicineSetting() {
		
		return null;
	}
	
	//TODO 获取个人信息
	
	//TODO 更新个人信息
	
}
