package com.lehealth.dao;

import com.lehealth.bean.BloodpressureResult;

public interface BloodpressureDao {
	
	//获取用户血压记录
	public BloodpressureResult selectBloodpressureRecords();
	
	//更新用户血压记录
	public void insertBloodpressureRecord();
	
}
