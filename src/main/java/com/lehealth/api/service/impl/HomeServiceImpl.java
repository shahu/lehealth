package com.lehealth.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.BloodpressureDao;
import com.lehealth.api.dao.MedicineDao;
import com.lehealth.api.dao.PanientDao;
import com.lehealth.api.service.HomeService;
import com.lehealth.data.bean.BloodpressureConfig;
import com.lehealth.data.bean.BloodpressureRecord;
import com.lehealth.data.bean.HomeResult;
import com.lehealth.data.bean.MedicineRecord;
import com.lehealth.data.bean.PanientInfo;
import com.lehealth.data.bean.UserBaseInfo;

@Service("homeService")
public class HomeServiceImpl implements HomeService{
	
	@Autowired
	@Qualifier("bloodpressureDao")
	private BloodpressureDao bloodpressureDao;
	
	@Autowired
	@Qualifier("medicineDao")
	private MedicineDao medicineDao;
	
	@Autowired
	@Qualifier("panientDao")
	private PanientDao panientDao;
	
	@Override
	public HomeResult getHomeData(String userId, int days) {
		List<BloodpressureRecord> bpRecords = this.bloodpressureDao.selectRecords(userId,days);
		BloodpressureConfig bpConfig = this.bloodpressureDao.selectConfig(userId);
		List<MedicineRecord> medicineRecords = this.medicineDao.selectRecords(userId, days);
		PanientInfo info = this.panientDao.selectPanient(userId);
		HomeResult result = new HomeResult(bpRecords, bpConfig, medicineRecords, days, info);
		return result;
	}
	
	@Override
	public HomeResult getHomeData(UserBaseInfo user, int days) {
		List<BloodpressureRecord> bpRecords = this.bloodpressureDao.selectRecords(user.getUserId(),days);
		BloodpressureConfig bpConfig = this.bloodpressureDao.selectConfig(user.getUserId());
		List<MedicineRecord> medicineRecords = this.medicineDao.selectRecords(user.getUserId(), days);
		List<PanientInfo> guardedInfos = this.panientDao.selectPanientListByGuardian(user.getLoginId());
		PanientInfo info = this.panientDao.selectPanient(user.getUserId());
		HomeResult result = new HomeResult(bpRecords, bpConfig, medicineRecords, days, guardedInfos, info);
		return result;
	}

}
