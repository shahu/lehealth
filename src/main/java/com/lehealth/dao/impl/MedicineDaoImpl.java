package com.lehealth.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.Medicine;
import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineInfo;
import com.lehealth.dao.MedicineDao;

@Repository("medicineDao")
public class MedicineDaoImpl extends BaseJdbcDao implements MedicineDao {

	public List<MedicineCategroy> selectMedicines() {
		List<MedicineCategroy> list=new ArrayList<MedicineCategroy>();
		Map<Integer,MedicineCategroy> map=new HashMap<Integer,MedicineCategroy>();
		String sql="SELECT t1.id AS mId,t1.name AS mName,t2.id AS cid,t2.name AS cName FROM Medicines t1 INNER JOIN MedicineCategory t2 ON t1.cateid=t2.id";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			int mid=rs.getInt("mId");
			String mname=StringUtils.trimToEmpty(rs.getString("mName"));
			int cid=rs.getInt("cId");
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
		String sql="SELECT * FROM MedicineRecords WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		List<MedicineInfo> list = new ArrayList<MedicineInfo>();
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			MedicineInfo info=new MedicineInfo();
			info.setAmount(rs.getFloat("amount"));
			info.setDate(rs.getDate("userdate").getTime());
			info.setFrequency(rs.getFloat("frequency"));
			info.setMedicineid(rs.getInt("medicineid"));
			info.setTiming(rs.getInt("timing"));
			list.add(info);
		}
		return list;
	}

	@Override
	public boolean insertMedicineRecord(MedicineInfo info) {
		String sql="INSERT INTO MedicineRecords VALUE(:uuid,:userid,:medicineid,:amount,:frequency,:timing,now())";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("uuid", UUID.randomUUID().toString());
		msps.addValue("userid", info.getUserid());
		msps.addValue("medicineid", info.getMedicineid());
		msps.addValue("amount", info.getAmount());
		msps.addValue("frequency", info.getFrequency());
		msps.addValue("timing", info.getTiming());
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

}
