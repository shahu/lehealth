package com.lehealth.api.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import com.lehealth.api.dao.DoctorDao;
import com.lehealth.bean.Doctor;
import com.lehealth.util.TokenUtils;

@Repository("doctorDao")
public class DoctorDaoImpl extends BaseJdbcDao implements DoctorDao {

	@Override
	public List<Doctor> selectDoctors(String userId) {
		String sql="SELECT t1.description,t1.gender,t1.hospital,t1.id,t1.name,t1.title,t1.thumbnail,t2.doctorid as checkid FROM doctor t1 "
				+"LEFT JOIN attention_user_doctor t2 "
				+"ON (t1.id=t2.doctorid AND t2.userid=:userid) ";
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
			int checkId=rs.getInt("checkid");
			if(checkId!=0){
				checkId=1;
			}
			d.setAttention(checkId);
			list.add(d);
		}
		return list;
	}

	@Override
	public Doctor selectDoctor(String userId,int doctorId) {
		Doctor d=new Doctor();
		String sql="SELECT t1.description,t1.gender,t1.hospital,t1.id,t1.name,t1.title,t1.thumbnail,t1.image,t2.doctorid as checkid FROM doctor t1 "
				+"LEFT JOIN attention_user_doctor t2 "
				+"ON (t1.id=t2.doctorid AND t2.userid=:userid) "
				+"WHERE t1.id=:doctorid ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("doctorid", doctorId);
		msps.addValue("userid", userId);
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
			int checkId=rs.getInt("checkid");
			if(checkId!=0){
				checkId=1;
			}
			d.setAttention(checkId);
		}
		return d;
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
