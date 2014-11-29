package com.lehealth.sync.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.impl.BaseJdbcDao;
import com.lehealth.sync.dao.SyncBloodpressureDao;
import com.lehealth.sync.entity.YundfRecord;

@Repository("syncBloodpressureDao")
public class SyncBloodpressureDaoImpl extends BaseJdbcDao implements SyncBloodpressureDao {

	public boolean saveRecord(List<YundfRecord> recordList){
		return true;
	}
	
}
