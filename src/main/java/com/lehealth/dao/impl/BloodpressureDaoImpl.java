package com.lehealth.dao.impl;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.BloodpressureResult;
import com.lehealth.dao.BloodpressureDao;

@Repository("bloodpressureDao")
public class BloodpressureDaoImpl extends BaseJdbcDao implements BloodpressureDao {

	public BloodpressureResult selectBloodpressureRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertBloodpressureRecord() {
		// TODO Auto-generated method stub
		
	}

	public void test() {
		String sql="select count(1) as c from DiseaseCategory";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			System.out.println("dao:"+rs.getString("c"));
		}
	}
}
