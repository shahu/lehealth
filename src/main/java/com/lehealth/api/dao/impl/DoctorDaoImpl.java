package com.lehealth.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.DoctorDao;
import com.lehealth.api.entity.DoctorInfo;
import com.lehealth.common.util.TokenUtils;

@Repository("doctorDao")
public class DoctorDaoImpl extends BaseJdbcDao implements DoctorDao {

	@Override
	public List<DoctorInfo> selectDoctors(String patinetId) {
		String sql="SELECT t1.userid,t1.description,t1.gender,t1.hospital,t1.id,t1.name,t1.title,t1.thumbnail,t2.doctorid as did "
				+"FROM user_doctor_info t1 "
				+"LEFT JOIN mapping_doctor_patient_attention t2 "
				+"ON (t1.userid=t2.doctorid AND t2.patinetid=:patinetid) ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("patinetid", patinetId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		List<DoctorInfo> list=new ArrayList<DoctorInfo>();
		while(rs.next()){
			DoctorInfo d=new DoctorInfo();
			d.setDesc(StringUtils.trim(rs.getString("description")));
			d.setGender(rs.getInt("gender"));
			d.setHospital(StringUtils.trimToEmpty(rs.getString("hospital")));
			d.setId(StringUtils.trimToEmpty(rs.getString("id")));
			d.setUserId(StringUtils.trim(rs.getString("userid")));
			d.setName(StringUtils.trimToEmpty(rs.getString("name")));
			d.setTitle(StringUtils.trimToEmpty(rs.getString("title")));
			d.setThumbnail(StringUtils.trimToEmpty(rs.getString("thumbnail")));
			String did=StringUtils.trimToEmpty(rs.getString("did"));
			if(StringUtils.isBlank(did)){
				d.setAttention(0);
			}else{
				d.setAttention(1);
			}
			list.add(d);
		}
		return list;
	}

	@Override
	public DoctorInfo selectDoctor(String patinetId,String doctorId) {
		DoctorInfo d=new DoctorInfo();
		String sql="SELECT t1.description,t1.gender,t1.hospital,t1.id,t1.name,t1.title,t1.thumbnail,t1.image,t2.doctorid as did "
				+"FROM user_doctor_info t1 "
				+"LEFT JOIN mapping_doctor_patient_attention t2 "
				+"ON (t1.userid=t2.doctorid AND t2.patinetid=:patinetid) "
				+"WHERE t1.userid=:doctorid ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("doctorid", doctorId);
		msps.addValue("patinetid", patinetId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql,msps);
		if(rs.next()){
			d.setDesc(StringUtils.trim(rs.getString("description")));
			d.setGender(rs.getInt("gender"));
			d.setHospital(StringUtils.trimToEmpty(rs.getString("hospital")));
			d.setId(StringUtils.trimToEmpty(rs.getString("id")));
			d.setName(StringUtils.trimToEmpty(rs.getString("name")));
			d.setTitle(StringUtils.trimToEmpty(rs.getString("title")));
			d.setThumbnail(StringUtils.trimToEmpty(rs.getString("thumbnail")));
			d.setImage(StringUtils.trimToEmpty(rs.getString("image")));
			String did=StringUtils.trimToEmpty(rs.getString("did"));
			if(StringUtils.isBlank(did)){
				d.setAttention(0);
			}else{
				d.setAttention(1);
			}
		}
		return d;
	}
	
	@Override
	public boolean cancelAttentionDoctor(String patinetId, String doctorId) {
		String sql="DELETE FROM mapping_doctor_patient_attention WHERE patinetid=:patinetid AND doctorid=:doctorid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("patinetid", patinetId);
		msps.addValue("doctorid", doctorId);
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean attentionDoctor(String patinetId, String doctorId) {
		String sql="INSERT INTO mapping_doctor_patient_attention VALUE(:uuid,:patinetid,:doctorid)";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("uuid", TokenUtils.buildUUid());
		msps.addValue("patinetid", patinetId);
		msps.addValue("doctorid", doctorId);
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}
	
}
