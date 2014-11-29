package com.lehealth.api.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.BloodpressureDao;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.BloodpressureRecord;
import com.lehealth.util.TokenUtils;

@Repository("bloodpressureDao")
public class BloodpressureDaoImpl extends BaseJdbcDao implements BloodpressureDao {

	@Override
	public List<BloodpressureRecord> selectRecords(String userId,int days) {
		String sql="SELECT updateTime,dbp,heartrate,sbp FROM bp_record WHERE userid=:userid AND Date(updatetime)>=:date";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		msps.addValue("date", new Date(DateUtils.addDays(new Date(System.currentTimeMillis()), -days).getTime()));
		List<BloodpressureRecord> list = new ArrayList<BloodpressureRecord>();
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			BloodpressureRecord info=new BloodpressureRecord();
			info.setDate(rs.getDate("updateTime").getTime());
			info.setDbp(rs.getInt("dbp"));
			info.setHeartrate(rs.getInt("heartrate"));
			info.setSbp(rs.getInt("sbp"));
			list.add(info);
		}
		return list;
	}

	@Override
	public boolean insertRecord(BloodpressureRecord info) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("uuid", TokenUtils.buildUUid());
		msps.addValue("userid", info.getUserId());
		msps.addValue("dbp", info.getDbp());
		msps.addValue("sbp", info.getSbp());
		msps.addValue("heartrate", info.getHeartrate());
		String sql="INSERT INTO bp_record VALUE(:uuid,:userid,:dbp,:sbp,:heartrate,now())";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public BloodpressureConfig selectConfig(String userId) {
		String sql="SELECT userid,dbp1,dbp2,sbp1,sbp2,heartrate1,heartrate2 FROM bp_setting WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		BloodpressureConfig bpConfig=new BloodpressureConfig();
		if(rs.next()){
			String userid=StringUtils.trimToEmpty(rs.getString("userid"));
			int dbp1=rs.getInt("dbp1");
			int dbp2=rs.getInt("dbp2");
			int sbp1=rs.getInt("sbp1");
			int sbp2=rs.getInt("sbp2");
			int heartrate1=rs.getInt("heartrate1");
			int heartrate2=rs.getInt("heartrate2");
			bpConfig.setUserId(userid);
			bpConfig.setDbp1(dbp1);
			bpConfig.setDbp2(dbp2);
			bpConfig.setSbp1(sbp1);
			bpConfig.setSbp2(sbp2);
			bpConfig.setHeartrate1(heartrate1);
			bpConfig.setHeartrate2(heartrate2);
		}
		return bpConfig;
	}

	@Override
	public boolean updateConfig(BloodpressureConfig bpConfig) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", bpConfig.getUserId());
		msps.addValue("dbp1", bpConfig.getDbp1());
		msps.addValue("dbp2", bpConfig.getDbp2());
		msps.addValue("sbp1", bpConfig.getSbp1());
		msps.addValue("sbp2", bpConfig.getSbp2());
		msps.addValue("heartrate1", bpConfig.getHeartrate1());
		msps.addValue("heartrate2", bpConfig.getHeartrate2());
		String sql="UPDATE bp_setting SET dbp1=:dbp1,dbp2 =:dbp2,sbp1 =:sbp1,sbp2 =:sbp2,heartrate1 =:heartrate1,heartrate2 =:heartrate2 WHERE userid=:userid;";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO bp_setting VALUE(:uuid,:userid,:dbp1,:dbp2,:sbp1,:sbp2,:heartrate1,:heartrate2)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}
}
