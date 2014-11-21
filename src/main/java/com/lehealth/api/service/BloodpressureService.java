package com.lehealth.api.service;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.bean.BloodpressureResult;

public interface BloodpressureService {
	
	//获取用户血压记录
	public BloodpressureResult getBloodpressureRecords(String userId,int days);
	
	//更新用户血压记录
	public boolean modifyBloodpressureRecord(BloodpressureInfo bpInfo);
	
	//获取血压控制设置
	public BloodpressureConfig getBloodpressureSetting(String userId);
	
	//更新血压控制设置
	public boolean modifyBloodpressureSetting(BloodpressureConfig bpConfig);
}
