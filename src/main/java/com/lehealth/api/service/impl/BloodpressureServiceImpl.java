package com.lehealth.api.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.lehealth.api.dao.BloodpressureDao;
import com.lehealth.api.dao.LoginDao;
import com.lehealth.api.dao.PanientDao;
import com.lehealth.api.entity.BloodpressureConfig;
import com.lehealth.api.entity.BloodpressureRecord;
import com.lehealth.api.entity.BloodpressureResult;
import com.lehealth.api.entity.PanientGuardianInfo;
import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.api.service.BloodpressureService;
import com.lehealth.common.service.SendSMSService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.common.util.CheckStatusUtils;
import com.lehealth.common.util.ComparatorUtils;
import com.lehealth.common.util.Constant;
import com.lehealth.data.type.BloodPressStatusType;
import com.lehealth.data.type.SystemVariableKeyType;

@Service("bloodpressureService")
public class BloodpressureServiceImpl implements BloodpressureService{
	
	@Autowired
	@Qualifier("bloodpressureDao")
	private BloodpressureDao bloodpressureDao;
	
	@Autowired
	@Qualifier("panientDao")
	private PanientDao panientDao;
	
	@Autowired
	@Qualifier("sendSMSService")
	private SendSMSService sendSMSService;
	
	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;
	
	@Autowired
	@Qualifier("loginDao")
	private LoginDao loginDao;
	
	private static Logger logger = Logger.getLogger(BloodpressureServiceImpl.class);
	
	@Override
	public BloodpressureResult getRecords(String userId,int days) {
		BloodpressureResult result=new BloodpressureResult();
		List<BloodpressureRecord> list=this.bloodpressureDao.selectRecords(userId,days);
		if(!list.isEmpty()){
			Collections.sort(list, ComparatorUtils.bpComparator);
			result.setRecords(list);
		}
		BloodpressureConfig config=this.bloodpressureDao.selectConfig(userId);
		if(StringUtils.isNotBlank(config.getUserId())){
			result.setConfig(config);
		}
		return result;
	}

	@Override
	public boolean addRecord(BloodpressureRecord bpInfo, String phoneNumber) {
		boolean flag=this.bloodpressureDao.insertRecord(bpInfo);
		if(flag){
			this.noticeBloodpressureStatus(bpInfo.getSbp(), bpInfo.getDbp(), bpInfo.getHeartrate(), phoneNumber);
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
	public void noticeBloodpressureStatus(int sbp, int dbp, int heartrate, String phoneNumber){
		UserBaseInfo user = this.loginDao.selectUserBaseInfo(phoneNumber);
		if(user != null){
			BloodpressureConfig config=this.bloodpressureDao.selectConfig(user.getUserId());
			if(StringUtils.isNotBlank(config.getUserId())){
				BloodPressStatusType statusCode = CheckStatusUtils.bloodpress(sbp, dbp, heartrate, config);
				if(statusCode != BloodPressStatusType.normal){
					//获取监护人手机
					List<PanientGuardianInfo> list = this.panientDao.selectGuardianList(user.getUserId());
					//调用短信通知监护人
					if(!CollectionUtils.isEmpty(list)){
						for(PanientGuardianInfo info : list){
							this.sendSituationNoticeSMS(info.getGuardianNumber(), phoneNumber, String.valueOf(sbp), String.valueOf(dbp));
						}
					}
				}
			}
		}
	}

	@Override
	public boolean delRecord(String id) {
		return this.bloodpressureDao.delRecord(id);
	}
	
	private void sendSituationNoticeSMS(String phoneNumber,String userPhoneNumber, String sbp, String dbp){
		String flag = this.systemVariableService.getValue(SystemVariableKeyType.sendSituationNoticeMessageSwitch);
		logger.info("sendSituationNoticeSMS flag is : " + flag + "!");
		if(StringUtils.equals(Constant.switchFlag, flag)){
			String[] datas = {userPhoneNumber, sbp, dbp};
			if(this.sendSMSService.sendTemplateSMS(phoneNumber, Constant.sendSituationNoticeTempleteId, datas)){
				logger.info("sendSituationNoticeSMS success,phone=" + phoneNumber);
			} else {
				logger.info("sendSituationNoticeSMS failed,phone=" + phoneNumber);
			}
		}
	}
}
