package com.lehealth.service;

import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.bean.BloodpressureResult;

public interface BloodpressureService {
	
	//获取用户血压记录
	public BloodpressureResult getBloodpressureRecords(String userId,int days);
	
	//更新用户血压记录
	public boolean modifyBloodpressureRecord(BloodpressureInfo bpInfo);
	
}
