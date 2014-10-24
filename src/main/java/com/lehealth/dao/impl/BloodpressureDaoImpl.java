package com.lehealth.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.dao.BloodpressureDao;

@Repository("bloodpressureDao")
public class BloodpressureDaoImpl extends BaseJdbcDao implements BloodpressureDao {

	@Override
	public List<BloodpressureInfo> selectBloodpressureRecords(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertBloodpressureRecord(BloodpressureInfo info) {
		// TODO Auto-generated method stub
		return false;
	}


}
