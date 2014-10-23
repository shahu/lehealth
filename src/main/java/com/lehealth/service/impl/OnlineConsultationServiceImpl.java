package com.lehealth.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;
import com.lehealth.service.OnlineConsultationService;

@Service("onlineConsultationService")
public class OnlineConsultationServiceImpl implements OnlineConsultationService{

	private static Logger logger = Logger.getLogger(OnlineConsultationServiceImpl.class);

	public List<Doctor> getDoctors() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Activitie> getAtivities() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
