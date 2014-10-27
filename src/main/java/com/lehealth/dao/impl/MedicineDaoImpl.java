package com.lehealth.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import com.lehealth.bean.Medicine;
import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineInfo;
import com.lehealth.dao.MedicineDao;
import com.lehealth.util.TokenUtils;

@Repository("medicineDao")
public class MedicineDaoImpl extends BaseJdbcDao implements MedicineDao {

	@Override
	public List<MedicineCategroy> selectMedicines() {
		List<MedicineCategroy> list=new ArrayList<MedicineCategroy>();
		Map<Integer,MedicineCategroy> map=new HashMap<Integer,MedicineCategroy>();
		String sql="SELECT t1.id AS mid,t1.name AS mName,t2.id AS cid,t2.name AS cName FROM Medicines t1 INNER JOIN MedicineCategory t2 ON t1.cateid=t2.id";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			int mid=rs.getInt("mid");
			String mname=StringUtils.trimToEmpty(rs.getString("mName"));
			int cid=rs.getInt("cid");
			String cname=StringUtils.trimToEmpty(rs.getString("cName"));
			if(!map.containsKey(cid)){
				MedicineCategroy medicineCategroy=new MedicineCategroy();
				medicineCategroy.setCateid(cid);
				medicineCategroy.setCatename(cname);
				map.put(cid, medicineCategroy);
			}
			Medicine medicine=new Medicine();
			medicine.setId(mid);
			medicine.setName(mname);
			map.get(cid).addMedicine(medicine);
		}
		list.addAll(map.values());
		return list;
	}

	@Override
	public List<MedicineInfo> selectMedicineRecords(String userId) {
		String sql="SELECT t1.*,t2.name AS medicinename FROM MedicineRecords t1 "
				+"INNER JOIN Medicines t2 ON t1.medicineid=t2.id "
				+"WHERE userid=:userid "
				+"ORDER BY recordDate DESC LIMIT 7";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		List<MedicineInfo> list = new ArrayList<MedicineInfo>();
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			MedicineInfo info=new MedicineInfo();
			info.setAmount(rs.getFloat("amount"));
			info.setDate(rs.getDate("recordDate").getTime());
			info.setFrequency(rs.getFloat("frequency"));
			info.setMedicineid(rs.getInt("medicineid"));
			info.setMedicinename(StringUtils.trimToEmpty(rs.getString("medicinename")));
			info.setTiming(rs.getInt("timing"));
			list.add(info);
		}
		return list;
	}

	@Override
	public boolean updateMedicineRecord(MedicineInfo info) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("uuid", TokenUtils.buildUUid());
		msps.addValue("userid", info.getUserid());
		msps.addValue("medicineid", info.getMedicineid());
		msps.addValue("amount", info.getAmount());
		msps.addValue("frequency", info.getFrequency());
		msps.addValue("timing", info.getTiming());
		msps.addValue("recordDate", info.getTiming());
		String sql="UPDATE MedicineRecords SET amount=:amount,frequency=:frequency,timing=:timing,updateTime=NOW() WHERE recordDate=:recordDate AND medicineid=:medicineid AND userid=:userid";;
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO MedicineRecords VALUE(:uuid,:userid,:medicineid,:amount,:frequency,:timing,:recordDate,now())";
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
