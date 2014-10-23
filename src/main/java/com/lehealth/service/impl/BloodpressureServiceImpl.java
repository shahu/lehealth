package com.lehealth.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.bean.BloodpressureResult;
import com.lehealth.dao.BloodpressureDao;
import com.lehealth.service.BloodpressureService;

@Service("bloodpressureService")
public class BloodpressureServiceImpl implements BloodpressureService,InitializingBean{
	
	
	@Autowired
	@Qualifier("bloodpressureDao")
	private BloodpressureDao bloodpressureDao;
	
	private static Logger logger = Logger.getLogger(BloodpressureServiceImpl.class);

	public BloodpressureResult getBloodpressureRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	public String modifyBloodpressureRecord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void test(){
		this.bloodpressureDao.test();
	}

	public void afterPropertiesSet() throws Exception {
		this.bloodpressureDao.test();
	}
}
