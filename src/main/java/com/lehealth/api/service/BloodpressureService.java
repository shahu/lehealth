package com.lehealth.api.service;

import com.lehealth.api.entity.BloodpressureConfig;
import com.lehealth.api.entity.BloodpressureRecord;
import com.lehealth.api.entity.BloodpressureResult;

public interface BloodpressureService {
	
	//获取用户血压记录
	public BloodpressureResult getRecords(String userId,int days);
	
	//更新用户血压记录
	public boolean addRecord(BloodpressureRecord bpInfo, String phoneNumber);
	
	//获取血压控制设置
	public BloodpressureConfig getConfig(String userId);
	
	//更新血压控制设置
	public boolean modifyConfig(BloodpressureConfig bpConfig);
	
	//检查血压波动并通知
	public void noticeBloodpressureStatus(int sbp, int dbp,int heartrate, String phoneNumber);
}
