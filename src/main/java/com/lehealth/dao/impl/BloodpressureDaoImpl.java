package com.lehealth.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.dao.BloodpressureDao;

@Repository("bloodpressureDao")
public class BloodpressureDaoImpl extends BaseJdbcDao implements BloodpressureDao {

	@Override
	public List<BloodpressureInfo> selectBloodpressureRecords(String userId) {
		String sql="SELECT * FROM BpRecords WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		List<BloodpressureInfo> list = new ArrayList<BloodpressureInfo>();
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			BloodpressureInfo info=new BloodpressureInfo();
			info.setDateStamp(rs.getDate("recordtime").getTime());
			info.setDbp(rs.getInt("dbp"));
			info.setHeartrate(rs.getInt("heartrate"));
			info.setSbp(rs.getInt("sbp"));
			list.add(info);
		}
		return list;
	}

	@Override
	public boolean insertBloodpressureRecord(BloodpressureInfo info) {
		String sql="INSERT INTO BpRecords VALUE(:uuid,:userid,:dbp,:sbp,:heartrate,now())";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("uuid", UUID.randomUUID());
		msps.addValue("userid", info.getUserId());
		msps.addValue("dbp", info.getDbp());
		msps.addValue("sbp", info.getSbp());
		msps.addValue("heartrate", info.getHeartrate());
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}


}
