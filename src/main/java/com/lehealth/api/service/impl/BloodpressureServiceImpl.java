package com.lehealth.api.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.BloodpressureDao;
import com.lehealth.api.dao.UserDao;
import com.lehealth.api.service.BloodpressureService;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.BloodpressureRecord;
import com.lehealth.bean.BloodpressureResult;
import com.lehealth.bean.UserGuardianInfo;

@Service("bloodpressureService")
public class BloodpressureServiceImpl implements BloodpressureService{
	
	@Autowired
	@Qualifier("bloodpressureDao")
	private BloodpressureDao bloodpressureDao;
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	private static Logger logger = Logger.getLogger(BloodpressureServiceImpl.class);

	@Override
	public BloodpressureResult getRecords(String userId,int days) {
		BloodpressureResult result=new BloodpressureResult();
		List<BloodpressureRecord> list=this.bloodpressureDao.selectRecords(userId,days);
		Collections.sort(list, new Comparator<BloodpressureRecord>() {
			@Override
			public int compare(BloodpressureRecord o1, BloodpressureRecord o2) {
				return (int) (o1.getDate()-o2.getDate());
			}
		});
		result.setRecords(list);
		BloodpressureConfig config=this.bloodpressureDao.selectConfig(userId);
		result.setConfig(config);
		result.setDays(days);
		return result;
	}

	@Override
	public boolean addRecord(BloodpressureRecord bpInfo) {
		boolean flag=this.bloodpressureDao.insertRecord(bpInfo);
		BloodpressureConfig config=this.bloodpressureDao.selectConfig(bpInfo.getUserId());
		if(bpInfo.getDbp()>=config.getDbp2()
			||bpInfo.getSbp()>=config.getSbp2()
			||bpInfo.getHeartrate()>=config.getHeartrate2()
			||bpInfo.getDbp()<=config.getDbp1()
			||bpInfo.getSbp()<=config.getSbp1()
			||bpInfo.getHeartrate()<=config.getHeartrate1()){
			//获取监护人手机
			List<UserGuardianInfo> list=this.userDao.selectUserGuardianInfos(bpInfo.getUserId());
			//调用短信通知监护人
			//TODO
			//sendMessage(guardian.getGuardianNumber());
		}
		return flag;
	}

	@Override
	public BloodpressureConfig getConfig(String userId) {
		return this.bloodpressureDao.selectConfig(userId);
	}

	@Override
	public boolean modifyConfig(BloodpressureConfig bpConfig) {
		return this.bloodpressureDao.updateConfig(bpConfig);
	}
}
