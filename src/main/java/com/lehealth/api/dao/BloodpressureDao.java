package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.BloodpressureInfo;

public interface BloodpressureDao {
	
	//获取用户血压记录
	public List<BloodpressureInfo> selectBloodpressureRecords(String userId,int days);
	
	//更新用户血压记录
	public boolean updateBloodpressureRecord(BloodpressureInfo info);
	
	//获取血压控制设置
	public BloodpressureConfig selectBloodpressureSetting(String userId);
	
	//更新血压控制设置
	public boolean updateBloodpressureSetting(BloodpressureConfig bpConfig);
}
