package com.lehealth.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.bean.BloodpressureResult;
import com.lehealth.dao.BloodpressureDao;
import com.lehealth.service.BloodpressureService;

@Service("bloodpressureService")
public class BloodpressureServiceImpl implements BloodpressureService{
	
	@Autowired
	@Qualifier("bloodpressureDao")
	private BloodpressureDao bloodpressureDao;
	
	private static Logger logger = Logger.getLogger(BloodpressureServiceImpl.class);

	@Override
	public BloodpressureResult getBloodpressureRecords(String userId) {
		BloodpressureResult result=new BloodpressureResult();
		result.setScore(98);
		result.setStatus("2");
		List<BloodpressureInfo> list=this.bloodpressureDao.selectBloodpressureRecords(userId);
		Collections.sort(list, new Comparator<BloodpressureInfo>() {
			@Override
			public int compare(BloodpressureInfo o1, BloodpressureInfo o2) {
				return (int) (o1.getDate()-o2.getDate());
			}
		});
		result.setRecords(list);
		return result;
	}

	@Override
	public boolean modifyBloodpressureRecord(BloodpressureInfo bpInfo) {
		return this.bloodpressureDao.updateBloodpressureRecord(bpInfo);
	}

}
