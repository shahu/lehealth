package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.api.entity.BloodpressureConfig;
import com.lehealth.api.entity.BloodpressureRecord;

public interface BloodpressureDao {
	
	//获取用户血压记录
	public List<BloodpressureRecord> selectRecords(String userId,int days);
	
	//更新用户血压记录
	public boolean insertRecord(BloodpressureRecord info);
	
	//获取血压控制设置
	public BloodpressureConfig selectConfig(String userId);
	
	//更新血压控制设置
	public boolean updateConfig(BloodpressureConfig bpConfig);
}
