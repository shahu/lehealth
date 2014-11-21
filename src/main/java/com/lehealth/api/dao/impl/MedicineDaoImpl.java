package com.lehealth.api.dao.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.MedicineDao;
import com.lehealth.bean.MedicineInfo;
import com.lehealth.util.TokenUtils;

@Repository("medicineDao")
public class MedicineDaoImpl extends BaseJdbcDao implements MedicineDao {

	@Override
	public Map<Integer,MedicineInfo> selectMedicineHistory(String userId){
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
				info.setMedicineid(medicineId);
				info.setMedicinename(StringUtils.trimToEmpty(rs.getString("medicinename")));
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
	public boolean updateMedicineHistory(final MedicineInfo info){
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", info.getUserid());
		msps.addValue("medicineid", info.getMedicineid());
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
}