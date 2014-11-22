package com.lehealth.api.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.BloodpressureDao;
import com.lehealth.bean.BloodpressureConfig;
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
		msps.addValue("userid", info.getUserId());
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

	@Override
	public BloodpressureConfig selectBloodpressureConfig(String userId) {
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
	public boolean updateBloodpressureConfig(BloodpressureConfig bpConfig) {
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
