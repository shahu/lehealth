package com.lehealth.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
		String sql="SELECT * FROM BpRecords WHERE userid=:userid ORDER BY recordDate DESC limit 7";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		List<BloodpressureInfo> list = new ArrayList<BloodpressureInfo>();
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			BloodpressureInfo info=new BloodpressureInfo();
			info.setDate(rs.getDate("recordDate").getTime());
			info.setDbp(rs.getInt("dbp"));
			info.setHeartrate(rs.getInt("heartrate"));
			info.setSbp(rs.getInt("sbp"));
			list.add(info);
		}
		return list;
	}

	@Override
	public boolean updateBloodpressureRecord(BloodpressureInfo info) {
		String sql="UPDATE BpRecords SET dbp=:dbp,sbp=:sbp,heartrate=:heartrate,updateTime=NOW() WHERE recordDate=:recordDate AND userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("uuid", TokenUtils.buildUUid());
		msps.addValue("userid", info.getUserid());
		msps.addValue("dbp", info.getDbp());
		msps.addValue("sbp", info.getSbp());
		msps.addValue("heartrate", info.getHeartrate());
		msps.addValue("recordDate", new Date(info.getDate()));
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO BpRecords VALUE(:uuid,:userid,:dbp,:sbp,:heartrate,:date,now())";
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
		
	}

}
