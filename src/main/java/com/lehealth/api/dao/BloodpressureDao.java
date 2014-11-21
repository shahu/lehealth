package com.lehealth.api.dao;

import java.util.List;
import com.lehealth.bean.BloodpressureInfo;

public interface BloodpressureDao {
	
	//获取用户血压记录
	public List<BloodpressureInfo> selectBloodpressureRecords(String userId,int days);
	
	//更新用户血压记录
	public boolean updateBloodpressureRecord(BloodpressureInfo info);
	
}
