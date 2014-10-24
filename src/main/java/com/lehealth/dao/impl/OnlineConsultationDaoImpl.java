package com.lehealth.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;
import com.lehealth.dao.OnlineConsultationDao;

@Repository("onlineConsultationDao")
public class OnlineConsultationDaoImpl extends BaseJdbcDao implements OnlineConsultationDao {

	public List<Doctor> selectDoctors() {
		List<Doctor> list=new ArrayList<Doctor>();
		String sql="SELECT * FROM Doctors";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			Doctor d=new Doctor();
			d.setDesc(StringUtils.trim(rs.getString("description")));
			d.setGender(rs.getInt("gender"));
			d.setHospital(StringUtils.trimToEmpty(rs.getString("hospital")));
			d.setId(rs.getInt("id"));
			d.setName(StringUtils.trimToEmpty(rs.getString("name")));
			d.setTitle(StringUtils.trimToEmpty(rs.getString("title")));
			list.add(d);
		}
		return list;
	}

	public List<Activitie> selectAtivities() {
		List<Activitie> list=new ArrayList<Activitie>();
		String sql="SELECT * FROM Activities";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			Activitie a=new Activitie();
			a.setExternalurl(StringUtils.trimToEmpty(rs.getString("externalurl")));
			a.setDesc(StringUtils.trimToEmpty(rs.getString("description")));
			a.setEndtimeStamp(rs.getDate("endtime").getTime());
			a.setId(rs.getInt("id"));
			a.setLocation(StringUtils.trimToEmpty(rs.getString("location")));
			a.setName(StringUtils.trimToEmpty(rs.getString("name")));
			a.setStartTimeStamp(rs.getDate("starttime").getTime());
			list.add(a);
		}
		
		return list;
	}

}
