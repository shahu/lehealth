package com.lehealth.sync.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lehealth.sync.entity.YundfRecord;
import com.lehealth.sync.entity.YundfUser;

public interface SyncBloodpressureDao {
	
	public Map<String,YundfUser> getLastRids(Set<String> phoneNumbers);
	
	public void insertRecord(String userId,List<YundfRecord> records);
	
	public void deleteSyncRid(Set<String> userIds);
	
	public void insertSyncRid(List<YundfUser> users);
}
