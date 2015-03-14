package com.lehealth.api.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.CommonDao;
import com.lehealth.bean.Activity;
import com.lehealth.bean.Disease;
import com.lehealth.bean.DiseaseCategroy;
import com.lehealth.bean.Medicine;
import com.lehealth.bean.MedicineCategroy;

@Repository("commonDao")
public class CommonDaoImpl extends BaseJdbcDao implements CommonDao {

	@Override
	public List<Activity> selectAtivities() {
		List<Activity> list=new ArrayList<Activity>();
		String sql="SELECT externalurl,description,endtime,id,location,name,starttime FROM activitie";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			Activity a=new Activity();
			a.setExternalUrl(StringUtils.trimToEmpty(rs.getString("externalurl")));
			a.setDesc(StringUtils.trimToEmpty(rs.getString("description")));
			a.setEndTime(rs.getDate("endtime").getTime());
			a.setId(rs.getInt("id"));
			a.setLocation(StringUtils.trimToEmpty(rs.getString("location")));
			a.setName(StringUtils.trimToEmpty(rs.getString("name")));
			a.setStartTime(rs.getDate("starttime").getTime());
			list.add(a);
		}
		
		return list;
	}

	

	@Override
	public List<MedicineCategroy> selectMedicines() {
		List<MedicineCategroy> list=new ArrayList<MedicineCategroy>();
		Map<Integer,MedicineCategroy> map=new HashMap<Integer,MedicineCategroy>();
		String sql="SELECT t1.id AS mid,t1.name AS mname,t2.id AS cid,t2.name AS cname "
				+"FROM medicine t1 "
				+"INNER JOIN medicine_category t2 ON t1.cateid=t2.id";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			int mid=rs.getInt("mid");
			String mname=StringUtils.trimToEmpty(rs.getString("mname"));
			int cid=rs.getInt("cid");
			String cname=StringUtils.trimToEmpty(rs.getString("cname"));
			if(!map.containsKey(cid)){
				MedicineCategroy medicineCategroy=new MedicineCategroy();
				medicineCategroy.setCateId(cid);
				medicineCategroy.setCateName(cname);
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
	public List<DiseaseCategroy> selectDiseases() {
		List<DiseaseCategroy> list=new ArrayList<DiseaseCategroy>();
		Map<Integer,DiseaseCategroy> map=new HashMap<Integer,DiseaseCategroy>();
		String sql="SELECT t1.id AS did,t1.name AS dname,t2.id AS cid,t2.name AS cname "
				+"FROM disease t1 "
				+"INNER JOIN disease_category t2 ON t1.cateid=t2.id";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			int did=rs.getInt("did");
			String dname=StringUtils.trimToEmpty(rs.getString("dname"));
			int cid=rs.getInt("cid");
			String cname=StringUtils.trimToEmpty(rs.getString("cname"));
			if(!map.containsKey(did)){
				DiseaseCategroy diseaseCategroy=new DiseaseCategroy();
				diseaseCategroy.setCateId(cid);
				diseaseCategroy.setCateName(cname);
				map.put(cid, diseaseCategroy);
			}
			Disease disease=new Disease();
			disease.setId(did);
			disease.setName(dname);
			map.get(cid).addDisease(disease);
		}
		list.addAll(map.values());
		return list;
	}
}
