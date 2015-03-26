package com.lehealth.api.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.lehealth.api.dao.BloodpressureDao;
import com.lehealth.api.dao.LoginDao;
import com.lehealth.api.dao.PanientDao;
import com.lehealth.api.service.BloodpressureService;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.data.bean.BloodpressureConfig;
import com.lehealth.data.bean.BloodpressureRecord;
import com.lehealth.data.bean.BloodpressureResult;
import com.lehealth.data.bean.PanientGuardianInfo;
import com.lehealth.data.bean.UserBaseInfo;

@Service("bloodpressureService")
public class BloodpressureServiceImpl implements BloodpressureService{
	
	@Autowired
	@Qualifier("bloodpressureDao")
	private BloodpressureDao bloodpressureDao;
	
	@Autowired
	@Qualifier("panientDao")
	private PanientDao panientDao;
	
	@Autowired
	@Qualifier("sendTemplateSMSService")
	private SendTemplateSMSService sendTemplateSMSService;
	
	@Autowired
	@Qualifier("loginDao")
	private LoginDao loginDao;
	
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
		return result;
	}

	@Override
	public boolean addRecord(BloodpressureRecord bpInfo, String phoneNumber) {
		boolean flag=this.bloodpressureDao.insertRecord(bpInfo);
		if(flag){
			this.checkBloodpressureConfig(bpInfo.getSbp(), bpInfo.getDbp(), bpInfo.getHeartrate(), phoneNumber);
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
	
	@Override
	public void checkBloodpressureConfig(int sbp, int dbp, int heartrate, String phoneNumber){
		UserBaseInfo user = this.loginDao.selectUserBaseInfo(phoneNumber);
		if(user != null){
			BloodpressureConfig config=this.bloodpressureDao.selectConfig(user.getUserId());
			if(StringUtils.isNotBlank(config.getUserId())){
				if(dbp >= config.getDbp2()
					|| sbp >= config.getSbp2()
					|| heartrate >= config.getHeartrate2()
					|| dbp <= config.getDbp1()
					|| sbp <= config.getSbp1()
					|| heartrate <= config.getHeartrate1()){
					//获取监护人手机
					List<PanientGuardianInfo> list = this.panientDao.selectGuardianList(user.getUserId());
					//调用短信通知监护人
					if(!CollectionUtils.isEmpty(list)){
						for(PanientGuardianInfo info : list){
							this.sendTemplateSMSService.sendNoticeSMS(info.getGuardianNumber(), phoneNumber, String.valueOf(sbp), String.valueOf(dbp));
						}
					}
				}
			}
		}
	}
}
