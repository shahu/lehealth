package com.lehealth.sync.dao;

import java.util.List;

import com.lehealth.sync.entity.YundfRecord;

public interface SyncBloodpressureDao {
	
	public boolean saveRecord(List<YundfRecord> recordList);
	
}
