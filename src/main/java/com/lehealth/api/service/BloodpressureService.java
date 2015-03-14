package com.lehealth.api.service;

import com.lehealth.data.bean.BloodpressureConfig;
import com.lehealth.data.bean.BloodpressureRecord;
import com.lehealth.data.bean.BloodpressureResult;

public interface BloodpressureService {
	
	//获取用户血压记录
	public BloodpressureResult getRecords(String userId,int days);
	
	//更新用户血压记录
	public boolean addRecord(BloodpressureRecord bpInfo);
	
	//获取血压控制设置
	public BloodpressureConfig getConfig(String userId);
	
	//更新血压控制设置
	public boolean modifyConfig(BloodpressureConfig bpConfig);
}
