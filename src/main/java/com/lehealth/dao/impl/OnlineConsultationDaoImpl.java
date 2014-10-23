package com.lehealth.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;
import com.lehealth.dao.OnlineConsultationDao;

@Repository("onlineConsultationDao")
public class OnlineConsultationDaoImpl extends BaseJdbcDao implements OnlineConsultationDao {

	public List<Doctor> selectDoctors() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Activitie> selectAtivities() {
		// TODO Auto-generated method stub
		return null;
	}

}
