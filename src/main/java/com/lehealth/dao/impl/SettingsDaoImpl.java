package com.lehealth.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.UserInfo;
import com.lehealth.dao.SettingsDao;
import com.lehealth.util.TokenUtils;

@Repository("settingsDao")
public class SettingsDaoImpl extends BaseJdbcDao implements SettingsDao {

	@Override
	public BloodpressureConfig selectBloodpressureSetting(String userId) {
		String sql="SELECT * FROM Bpsetting WHERE userid=:userid";
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
			bpConfig.setUserid(userid);
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
	public boolean updateBloodpressureSetting(BloodpressureConfig bpConfig) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", bpConfig.getUserid());
		msps.addValue("dbp1", bpConfig.getDbp1());
		msps.addValue("dbp2", bpConfig.getDbp2());
		msps.addValue("sbp1", bpConfig.getSbp1());
		msps.addValue("sbp2", bpConfig.getSbp2());
		msps.addValue("heartrate1", bpConfig.getHeartrate1());
		msps.addValue("heartrate2", bpConfig.getHeartrate2());
		String sql="UPDATE Bpsetting SET dbp1=:dbp1,dbp2 =:dbp2,sbp1 =:sbp1,sbp2 =:sbp2,heartrate1 =:heartrate1,heartrate2 =:heartrate2 WHERE userid=:userid;";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO Bpsetting VALUE(:uuid,:userid,:dbp1,:dbp2,:sbp1,:sbp2:,:heartrate1,:heartrate2)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<MedicineConfig> selectMedicineSettings(String userId) {
		String sql="SELECT t1.*,t2.name AS medicinename FROM MedicineSetting t1 "
				+"INNER JOIN Medicines t2 ON t1.medicineid=t2.id "
				+"WHERE t1.userid=:userid ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		List<MedicineConfig> list=new ArrayList<MedicineConfig>();
		while(rs.next()){
			MedicineConfig mConfig=new MedicineConfig();
			mConfig.setMedicineid(rs.getInt("medicineid"));
			mConfig.setMedicinename(StringUtils.trimToEmpty(rs.getString("medicinename")));;
			mConfig.setAmount(rs.getFloat("amount"));
			mConfig.setFrequency(rs.getFloat("frequency"));
			mConfig.setTiming(rs.getInt("timing"));
			mConfig.setDatefrom(rs.getDate("datefrom").getTime());
			mConfig.setDateto(rs.getDate("dateto").getTime());
			list.add(mConfig);
		}
		return list;
	}

	@Override
	public boolean updateMedicineSetting(MedicineConfig mConfig) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", mConfig.getUserid());
		msps.addValue("amount", mConfig.getAmount());
		msps.addValue("frequency", mConfig.getFrequency());
		msps.addValue("medicineid", mConfig.getMedicineid());
		msps.addValue("timing", mConfig.getTiming());
		msps.addValue("datefrom", new Timestamp(mConfig.getDatefrom()));
		msps.addValue("dateto", new Timestamp(mConfig.getDateto()));
		String sql="UPDATE MedicineSetting SET amount=:amount,frequency=:frequency,timing=:timing,datefrom=:datefrom,dateto=:dateto WHERE userid=:userid AND medicineid=:medicineid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO MedicineSetting VALUE(:uuid,:userid,:medicineid,:amount,:frequency,:timing,:datefrom,:dateto)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean deleteMedicineSetting(String userId, int medicineId) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		msps.addValue("medicineid", medicineId);
		String sql="DELETE FROM MedicineSetting WHERE userid=:userid AND medicineid=:medicineid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public UserInfo selectUserInfo(String userid) {
		String sql="SELECT * FROM UserInfo WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userid);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		UserInfo info=new UserInfo();
		if(rs.next()){
			String id=StringUtils.trimToEmpty(rs.getString("userid"));
			String userName=StringUtils.trimToEmpty(rs.getString("username"));
			int gender=rs.getInt("gender");
			long birthday=rs.getDate("birthday").getTime();
			float height=rs.getFloat("height");
			float weight=rs.getFloat("weight");
			float userInfocol=rs.getFloat("UserInfocol");
			info.setUserId(id);
			info.setUserName(userName);
			info.setGender(gender);
			info.setBirthday(birthday);
			info.setHeight(height);
			info.setWeight(weight);
			info.setUserInfocol(userInfocol);
		}
		return info;
	}

	@Override
	public boolean updateUserInfo(UserInfo info) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", info.getUserId());
		msps.addValue("gender", info.getGender());
		msps.addValue("birthday", new Timestamp(info.getBirthday()));
		msps.addValue("height", info.getHeight());
		msps.addValue("UserInfocol", info.getUserInfocol());
		msps.addValue("weight", info.getWeight());
		msps.addValue("username", info.getUserName());
		String sql="UPDATE UserInfo SET gender=:gender,birthday=:birthday,height=:height,UserInfocol=:UserInfocol,weight=:weight,username=:username WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO UserInfo VALUE(:uuid,:userid,:gender,:birthday,:height,:UserInfocol,:weight,:username)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}
	
}
