package com.lehealth.api.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.SettingsDao;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.DiseaseHistory;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;
import com.lehealth.util.TokenUtils;

@Repository("settingsDao")
public class SettingsDaoImpl extends BaseJdbcDao implements SettingsDao {

	@Override
	public BloodpressureConfig selectBloodpressureSetting(String userId) {
		String sql="SELECT dbp1,dbp2,sbp1,sbp2,heartrate1,heartrate2 FROM bp_setting WHERE userid=:userid";
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
		String sql="UPDATE bp_setting SET dbp1=:dbp1,dbp2 =:dbp2,sbp1 =:sbp1,sbp2 =:sbp2,heartrate1 =:heartrate1,heartrate2 =:heartrate2 WHERE userid=:userid;";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO bp_setting VALUE(:uuid,:userid,:dbp1,:dbp2,:sbp1,:sbp2:,:heartrate1,:heartrate2)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	@Override
	public Map<Integer,MedicineConfig> selectMedicineSettings(String userId) {
		String sql="SELECT t1.medicineid,t1.datefrom,t1.dateto,t1.time,t1.dosage,t2.name AS medicinename FROM medicine_setting t1 "
				+"INNER JOIN medicine t2 ON t1.medicineid=t2.id "
				+"WHERE t1.userid=:userid ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		Map<Integer,MedicineConfig> map=new HashMap<Integer, MedicineConfig>();
		while(rs.next()){
			int medicineId=rs.getInt("medicineid");
			if(!map.containsKey(medicineId)){
				MedicineConfig mConfig=new MedicineConfig();
				mConfig.setMedicineid(medicineId);
				mConfig.setMedicinename(StringUtils.trimToEmpty(rs.getString("medicinename")));
				mConfig.setDatefrom(rs.getDate("datefrom").getTime());
				mConfig.setDateto(rs.getDate("dateto").getTime());
				map.put(medicineId, mConfig);
			}
			MedicineConfig mConfig=map.get(medicineId);
			mConfig.addConfig(rs.getString("time"), rs.getFloat("dosage"));
		}
		return map;
	}

	@Override
	public boolean insertMedicineSetting(MedicineConfig mConfig) {
		int index=0;
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", mConfig.getUserid());
		msps.addValue("medicineid", mConfig.getMedicineid());
		msps.addValue("datefrom", new Timestamp(mConfig.getDatefrom()));
		msps.addValue("dateto", new Timestamp(mConfig.getDateto()));
		if(mConfig.getConfigs()!=null&&!mConfig.getConfigs().isEmpty()){
			for(Entry<String,Float> e : mConfig.getConfigs().entrySet()){
				msps.addValue("time", e.getKey());
				msps.addValue("dosage", e.getValue());
				msps.addValue("uuid", TokenUtils.buildUUid());
				String sql="INSERT INTO medicine_setting VALUE(:uuid,:userid,:medicineid,:dosage,:time,:datefrom,:dateto)";
				index+=this.namedJdbcTemplate.update(sql, msps);
			}
		}
		if(index==0){
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteMedicineSetting(String userId, int medicineId) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		msps.addValue("medicineid", medicineId);
		String sql="DELETE FROM medicine_setting WHERE userid=:userid AND medicineid=:medicineid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public UserInfo selectUserInfo(String userid) {
		String sql="SELECT userid,username,gender,birthday,height,weight FROM user_info WHERE userid=:userid";
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
			info.setUserId(id);
			info.setUserName(userName);
			info.setGender(gender);
			info.setBirthday(birthday);
			info.setHeight(height);
			info.setWeight(weight);
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
		msps.addValue("weight", info.getWeight());
		msps.addValue("username", info.getUserName());
		String sql="UPDATE user_info SET gender=:gender,birthday=:birthday,height=:height,weight=:weight,username=:username WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO user_info VALUE(:uuid,:userid,:gender,:birthday,:height,:weight,:username)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	@Override
	public UserGuardianInfo selectUserGuardianInfo(String userId) {
		String sql="SELECT userid,guardianname,guardiannumber FROM user_guardian WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		UserGuardianInfo info=new UserGuardianInfo();
		if(rs.next()){
			String id=StringUtils.trimToEmpty(rs.getString("userid"));
			String guardianName=StringUtils.trimToEmpty(rs.getString("guardianname"));
			String guardianNumber=StringUtils.trimToEmpty(rs.getString("guardiannumber"));
			info.setUserId(id);
			info.setGuardianName(guardianName);
			info.setGuardianNumber(guardianNumber);
		}
		return info;
	}

	@Override
	public boolean updateUserGuardianInfo(UserGuardianInfo info) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", info.getUserId());
		msps.addValue("guardianname", info.getGuardianName());
		msps.addValue("guardiannumber", info.getGuardianNumber());
		String sql="UPDATE user_guardian SET guardianname=:guardianname,guardiannumber=:guardiannumber WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO user_guardian VALUE(:uuid,:userid,:guardianname,:guardianname,:guardiannumber)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<DiseaseHistory> selectDiseaseHistorys(String userId) {
		String sql="SELECT t1.diseasedescription,t1.diseaseid,t1.medicinedescription,t2.name AS diseasename FROM disease_history t1 "
				+"INNER JOIN disease t2 ON t1.diseaseid=t2.id "
				+"WHERE t1.userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		List<DiseaseHistory> list = new ArrayList<DiseaseHistory>();
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			DiseaseHistory h=new DiseaseHistory();
			h.setDiseaseDescription(StringUtils.trimToEmpty(rs.getString("diseasedescription")));
			h.setDiseaseId(rs.getInt("diseaseid"));
			h.setDiseaseName(StringUtils.trimToEmpty(rs.getString("diseasename")));
			h.setMedicinedescription(StringUtils.trimToEmpty(rs.getString("medicinedescription")));
			list.add(h);
		}
		return list;
	}

	@Override
	public boolean updateDiseaseHistory(DiseaseHistory history) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", history.getUserId());
		msps.addValue("diseasedescription", history.getDiseaseDescription());
		msps.addValue("medicinedescription", history.getMedicinedescription());
		msps.addValue("diseaseid", history.getDiseaseId());
		String sql="UPDATE disease_history SET diseasedescription=:diseasedescription,medicinedescription=:medicinedescription,diseaseid=:diseaseid WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO disease_history VALUE(:uuid,:userid,:diseaseid,:diseasedescription,:medicinedescription)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Doctor> selectAttentionDoctor(String userId) {
		String sql="SELECT t1.description,t1.gender,t1.hospital,t1.id,t1.name,t1.title,t1.thumbnail FROM doctor t1 "
				+"INNER JOIN attention_user_doctor t2 "
				+"ON t1.id=t2.doctorid "
				+"WHERE t2.userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		List<Doctor> list=new ArrayList<Doctor>();
		while(rs.next()){
			Doctor d=new Doctor();
			d.setDesc(StringUtils.trim(rs.getString("description")));
			d.setGender(rs.getInt("gender"));
			d.setHospital(StringUtils.trimToEmpty(rs.getString("hospital")));
			d.setId(rs.getInt("id"));
			d.setName(StringUtils.trimToEmpty(rs.getString("name")));
			d.setTitle(StringUtils.trimToEmpty(rs.getString("title")));
			d.setThumbnail(StringUtils.trimToEmpty(rs.getString("thumbnail")));
			list.add(d);
		}
		return list;
	}

	@Override
	public boolean cancelAttentionDoctor(String userId, int doctorId) {
		String sql="DELETE FROM attention_user_doctor WHERE userid=:userid AND doctorid=:doctorid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		msps.addValue("doctorid", doctorId);
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean attentionDoctor(String userId, int doctorId) {
		String sql="INSERT INTO attention_user_doctor VALUE(:uuid,:userid,:doctorid)";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("uuid", TokenUtils.buildUUid());
		msps.addValue("userid", userId);
		msps.addValue("doctorid", doctorId);
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}
	
}
