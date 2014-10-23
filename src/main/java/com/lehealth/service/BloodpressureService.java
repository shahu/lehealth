package com.lehealth.service;

import com.lehealth.bean.BloodpressureResult;

public interface BloodpressureService {
	
	//获取用户血压记录
	public BloodpressureResult getBloodpressureRecords();
	
	//更新用户血压记录
	public String modifyBloodpressureRecord();
	
}
