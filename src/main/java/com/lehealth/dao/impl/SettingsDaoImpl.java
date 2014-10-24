package com.lehealth.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.dao.SettingsDao;

@Repository("settingsDao")
public class SettingsDaoImpl extends BaseJdbcDao implements SettingsDao {

	public BloodpressureConfig selectBloodpressureSetting() {
		String sql="";
		return null;
	}

	public String updateBloodpressureSetting() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MedicineConfig> selectMedicineSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	public String updateMedicineSetting() {
		// TODO Auto-generated method stub
		return null;
	}

}
