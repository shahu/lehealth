package com.lehealth.api.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.BloodpressureDao;
import com.lehealth.api.dao.MedicineDao;
import com.lehealth.api.service.HomeService;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.bean.HomeResult;
import com.lehealth.bean.MedicineInfo;

@Service("homeService")
public class HomeServiceImpl implements HomeService{
	
	@Autowired
	@Qualifier("bloodpressureDao")
	private BloodpressureDao bloodpressureDao;
	
	@Autowired
	@Qualifier("medicineDao")
	private MedicineDao medicineDao;
	
	private static Logger logger = Logger.getLogger(HomeServiceImpl.class);

	@Override
	public HomeResult getHomeData(String userId) {
		HomeResult result=new HomeResult();
		List<BloodpressureInfo> list=this.bloodpressureDao.selectBloodpressureRecords(userId,7);
		Collections.sort(list, new Comparator<BloodpressureInfo>() {
			@Override
			public int compare(BloodpressureInfo o1, BloodpressureInfo o2) {
				return (int) (o1.getDate()-o2.getDate());
			}
		});
		result.setRecords(list);
		BloodpressureConfig config=this.bloodpressureDao.selectBloodpressureConfig(userId);
		result.setConfig(config);
		List<MedicineInfo> medicineecords=this.medicineDao.selectMedicineRecords(userId, 7);
		result.setMedicineecords(medicineecords);
		return result;
	}

}
