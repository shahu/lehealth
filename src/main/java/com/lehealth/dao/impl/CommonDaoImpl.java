package com.lehealth.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.Disease;
import com.lehealth.bean.DiseaseCategroy;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.Medicine;
import com.lehealth.bean.MedicineCategroy;
import com.lehealth.dao.CommonDao;

@Repository("commonDao")
public class CommonDaoImpl extends BaseJdbcDao implements CommonDao {

	@Override
	public List<Doctor> selectDoctors() {
		List<Doctor> list=new ArrayList<Doctor>();
		String sql="SELECT * FROM doctors";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
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
	public List<Activitie> selectAtivities() {
		List<Activitie> list=new ArrayList<Activitie>();
		String sql="SELECT * FROM activitie";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			Activitie a=new Activitie();
			a.setExternalurl(StringUtils.trimToEmpty(rs.getString("externalurl")));
			a.setDesc(StringUtils.trimToEmpty(rs.getString("description")));
			a.setEndtime(rs.getDate("endtime").getTime());
			a.setId(rs.getInt("id"));
			a.setLocation(StringUtils.trimToEmpty(rs.getString("location")));
			a.setName(StringUtils.trimToEmpty(rs.getString("name")));
			a.setStarttime(rs.getDate("starttime").getTime());
			list.add(a);
		}
		
		return list;
	}

	@Override
	public Doctor selectDoctorById(int doctorId) {
		Doctor d=new Doctor();
		String sql="SELECT * FROM doctors WHERE id=:doctorId";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("doctorId", doctorId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql,msps);
		if(rs.next()){
			d.setDesc(StringUtils.trim(rs.getString("description")));
			d.setGender(rs.getInt("gender"));
			d.setHospital(StringUtils.trimToEmpty(rs.getString("hospital")));
			d.setId(rs.getInt("id"));
			d.setName(StringUtils.trimToEmpty(rs.getString("name")));
			d.setTitle(StringUtils.trimToEmpty(rs.getString("title")));
			d.setThumbnail(StringUtils.trimToEmpty(rs.getString("thumbnail")));
			d.setImage(StringUtils.trimToEmpty(rs.getString("image")));
		}
		return d;
	}

	@Override
	public List<MedicineCategroy> selectMedicines() {
		List<MedicineCategroy> list=new ArrayList<MedicineCategroy>();
		Map<Integer,MedicineCategroy> map=new HashMap<Integer,MedicineCategroy>();
		String sql="SELECT t1.id AS mid,t1.name AS mName,t2.id AS cid,t2.name AS cName FROM medicine t1 INNER JOIN medicine_category t2 ON t1.cateid=t2.id";
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
	public List<DiseaseCategroy> selectDiseases() {
		List<DiseaseCategroy> list=new ArrayList<DiseaseCategroy>();
		Map<Integer,DiseaseCategroy> map=new HashMap<Integer,DiseaseCategroy>();
		String sql="SELECT t1.id AS MID,t1.name AS mName,t2.id AS cid,t2.name AS cName FROM disease t1 INNER JOIN disease_category t2 ON t1.cateid=t2.id";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			int did=rs.getInt("did");
			String dname=StringUtils.trimToEmpty(rs.getString("dName"));
			int cid=rs.getInt("cid");
			String cname=StringUtils.trimToEmpty(rs.getString("cName"));
			if(!map.containsKey(did)){
				DiseaseCategroy diseaseCategroy=new DiseaseCategroy();
				diseaseCategroy.setCateid(cid);
				diseaseCategroy.setCatename(cname);
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
