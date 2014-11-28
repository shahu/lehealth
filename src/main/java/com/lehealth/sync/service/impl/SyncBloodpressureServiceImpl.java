package com.lehealth.sync.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.sync.dao.SyncBloodpressureDao;
import com.lehealth.sync.service.SyncBloodpressureService;

@Service("syncBloodpressureService")
public class SyncBloodpressureServiceImpl implements SyncBloodpressureService{

	@Autowired
	@Qualifier("syncBloodpressureDao")
	private SyncBloodpressureDao syncBloodpressure;
	
	private static Logger logger = Logger.getLogger(SyncBloodpressureServiceImpl.class);

	@Override
	public void syncFromYundf() {
		//从云大夫获取好友列表
		
		//从郁达夫获取好友数据
		
		//存入数据库
	}

}
