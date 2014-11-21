package com.lehealth.api.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.BloodpressureDao;
import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.util.TokenUtils;

@Repository("bloodpressureDao")
public class BloodpressureDaoImpl extends BaseJdbcDao implements BloodpressureDao {

	@Override
	public List<BloodpressureInfo> selectBloodpressureRecords(String userId,int days) {
		String sql="SELECT recordDate,dbp,heartrate,sbp FROM bp_record WHERE userid=:userid ORDER BY recordDate DESC limit "+days;
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
		String sql="UPDATE bp_record SET dbp=:dbp,sbp=:sbp,heartrate=:heartrate,updateTime=NOW() WHERE recordDate=:recordDate AND userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("uuid", TokenUtils.buildUUid());
		msps.addValue("userid", info.getUserid());
		msps.addValue("dbp", info.getDbp());
		msps.addValue("sbp", info.getSbp());
		msps.addValue("heartrate", info.getHeartrate());
		msps.addValue("recordDate", new Date(info.getDate()));
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO bp_record VALUE(:uuid,:userid,:dbp,:sbp,:heartrate,:recordDate,now())";
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