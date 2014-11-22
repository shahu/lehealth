package com.lehealth.api.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.MedicineDao;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineInfo;
import com.lehealth.util.TokenUtils;

@Repository("medicineDao")
public class MedicineDaoImpl extends BaseJdbcDao implements MedicineDao {

	@Override
	public Map<Integer,MedicineInfo> selectMedicineTodayRecords(String userId){
		String sql="SELECT t1.medicineid,t2.name as medicinename,t1.time as configtime,t1.dosage as configdosage,"
				+"t3.time as historytime,t3.dosage as historydosage,t3.updatetime,t3.medicineid as checkid "
				+"FROM medicine_setting t1 "
				+"INNER JOIN medicine t2 ON t1.medicineid=t2.id "
				+"LEFT JOIN medicine_record t3 "
				+"ON (t1.userid=t3.userid AND t1.medicineid=t3.medicineid AND t1.time=t3.time AND Date(t3.updatetime)=:date) "
				+"WHERE t1.userid=:userid "
				+"AND t1.datefrom<=:date "
				+"AND t1.dateto>=:date ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		msps.addValue("date", new Date(System.currentTimeMillis()));
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql,msps);
		Map<Integer,MedicineInfo> map=new HashMap<Integer, MedicineInfo>();
		while(rs.next()){
			int medicineId=rs.getInt("medicineid");
			String checkId=rs.getString("checkid");
			if(!map.containsKey(medicineId)){
				MedicineInfo info=new MedicineInfo();
				info.setMedicineId(medicineId);
				info.setMedicineName(StringUtils.trimToEmpty(rs.getString("medicinename")));
				if(StringUtils.isNotBlank(checkId)){
					info.setDate(rs.getDate("updatetime").getTime());
				}
				map.put(medicineId, info);
			}
			MedicineInfo info=map.get(medicineId);
			info.addConfig(StringUtils.trimToEmpty(rs.getString("configtime")), rs.getFloat("configdosage"));
			if(StringUtils.isNotBlank(checkId)){
				info.addSituation(StringUtils.trimToEmpty(rs.getString("historytime")), rs.getFloat("historydosage"));
			}
		}
		return map;
	}
	
	@Override
	public List<MedicineInfo> selectMedicineRecords(String userId, int days) {
		String sql="SELECT t1.userid,t1.medicineid,t2.name AS medicinename,t1.updatetime FROM medicine_record t1 "
				+"INNER JOIN medicine t2 ON t1.medicineid=t2.id "
				+"WHERE t1.userid=:userid AND Date(t1.updatetime)>=:date";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		msps.addValue("date", new Date(DateUtils.addDays(new Date(System.currentTimeMillis()), -days).getTime()));
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql,msps);
		List<MedicineInfo> list=new ArrayList<MedicineInfo>();
		while(rs.next()){
			MedicineInfo record=new MedicineInfo();
			record.setMedicineId(rs.getInt("medicineid"));
			record.setMedicineName(StringUtils.trimToEmpty(rs.getString("medicinename")));
			record.setDate(rs.getDate("updatetime").getTime());
			list.add(record);
		}
		return list;
	}
	
	@Override
	public boolean updateMedicineRecord(final MedicineInfo info){
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", info.getUserId());
		msps.addValue("medicineid", info.getMedicineId());
		msps.addValue("time", info.getTime());
		msps.addValue("dosage", info.getDosage());
		String sql="UPDATE medicine_record SET dosage=:dosage,updatetime=NOW() WHERE userid=:userid AND medicineid=:medicineid AND time=:time";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			msps.addValue("uuid", TokenUtils.buildUUid());
			sql="INSERT INTO medicine_record VALUE(:uuid,:userid,:medicineid,:time,:dosage,now())";
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
	public Map<Integer,MedicineConfig> selectMedicineConfigs(String userId) {
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
				mConfig.setMedicineId(medicineId);
				mConfig.setMedicineName(StringUtils.trimToEmpty(rs.getString("medicinename")));
				mConfig.setDateFrom(rs.getDate("datefrom").getTime());
				mConfig.setDateTo(rs.getDate("dateto").getTime());
				map.put(medicineId, mConfig);
			}
			MedicineConfig mConfig=map.get(medicineId);
			mConfig.addConfig(rs.getString("time"), rs.getFloat("dosage"));
		}
		return map;
	}

	@Override
	public boolean insertMedicineConfig(MedicineConfig mConfig) {
		int index=0;
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", mConfig.getUserId());
		msps.addValue("medicineid", mConfig.getMedicineId());
		msps.addValue("datefrom", new Timestamp(mConfig.getDateFrom()));
		msps.addValue("dateto", new Timestamp(mConfig.getDateTo()));
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
	public boolean deleteMedicineConfig(String userId, int medicineId) {
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

}
