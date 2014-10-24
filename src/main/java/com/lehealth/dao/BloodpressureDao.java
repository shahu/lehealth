package com.lehealth.dao;

import java.util.List;
import com.lehealth.bean.BloodpressureInfo;

public interface BloodpressureDao {
	
	//获取用户血压记录
	public List<BloodpressureInfo> selectBloodpressureRecords(String userId);
	
	//更新用户血压记录
	public boolean insertBloodpressureRecord(BloodpressureInfo info);
	
}
