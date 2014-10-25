package com.lehealth.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.dao.SettingsDao;

@Repository("settingsDao")
public class SettingsDaoImpl extends BaseJdbcDao implements SettingsDao {

	@Override
	public BloodpressureConfig selectBloodpressureSetting(String userId) {
		String sql="SELECT * FROM Bpsetting WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userId", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		BloodpressureConfig bpConfig=new BloodpressureConfig();
		if(rs.next()){
			int dbp1=rs.getInt("dbp1");
			int dbp2=rs.getInt("dbp2");
			int sbp1=rs.getInt("sbp1");
			int sbp2=rs.getInt("sbp2");
			bpConfig.setDbp1(dbp1);
			bpConfig.setDbp2(dbp2);
			bpConfig.setSbp1(sbp1);
			bpConfig.setSbp2(sbp2);
		}
		return bpConfig;
	}

	@Override
	public boolean updateBloodpressureSetting(BloodpressureConfig bpConfig) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userId", bpConfig.getUserid());
		msps.addValue("dbp1", bpConfig.getDbp1());
		msps.addValue("dbp2", bpConfig.getDbp2());
		msps.addValue("sbp1", bpConfig.getSbp1());
		msps.addValue("sbp2", bpConfig.getSbp2());
		String sql="UPDATE Bpsetting SET dbp1=:dbp1,dbp2 =:dbp2,sbp1 =:sbp1,sbp2 =:sbp2 WHERE userid=:userid;";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO Bpsetting VALUE(:uuid,:userid,:dbp1,:dbp2,:sbp1,:sbp2)";
			msps.addValue("uuid", UUID.randomUUID().toString());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	//TODO 一个用户多个用药,唯一索引需要修改
	@Override
	public List<MedicineConfig> selectMedicineSettings(String userId) {
		String sql="SELECT * FROM MedicineSetting WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userId", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		List<MedicineConfig> list=new ArrayList<MedicineConfig>();
		while(rs.next()){
			int medicineid=rs.getInt("medicineid");
			float amount=rs.getFloat("amount");
			float frequency=rs.getFloat("frequency");
			int timing=rs.getInt("timing");
			long datefrom=rs.getDate("datefrom").getTime();
			long dateto=rs.getDate("dateto").getTime();
			MedicineConfig mConfig=new MedicineConfig();
			mConfig.setMedicineid(medicineid);
			mConfig.setAmount(amount);
			mConfig.setFrequency(frequency);
			mConfig.setTiming(timing);
			mConfig.setDatefrom(datefrom);
			mConfig.setDateto(dateto);
			list.add(mConfig);
		}
		return list;
	}

	@Override
	public boolean updateMedicineSetting(MedicineConfig mConfig) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userId", mConfig.getUserid());
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
			msps.addValue("uuid", UUID.randomUUID().toString());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}


}
