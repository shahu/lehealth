package com.lehealth.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.dao.BloodpressureDao;
import com.lehealth.util.TokenUtils;

@Repository("bloodpressureDao")
public class BloodpressureDaoImpl extends BaseJdbcDao implements BloodpressureDao {

	@Override
	public List<BloodpressureInfo> selectBloodpressureRecords(String userId) {
		String sql="SELECT * FROM BpRecords WHERE userid=:userid and recordtime>=:beginTime ORDER BY recordtime DESC";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		msps.addValue("beginTime", new Timestamp(DateUtils.addDays(new Date(), -7).getTime()));
		List<BloodpressureInfo> list = new ArrayList<BloodpressureInfo>();
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			BloodpressureInfo info=new BloodpressureInfo();
			info.setDate(rs.getDate("recordtime").getTime());
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
		msps.addValue("uuid", TokenUtils.buildUUid());
		msps.addValue("userid", info.getUserid());
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
