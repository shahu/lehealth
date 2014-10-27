package com.lehealth.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.ResponseBean;
import com.lehealth.service.LoginService;
import com.lehealth.service.OnlineConsultationService;

@Controller
@RequestMapping("/api")
public class OnlineConsultationController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("onlineConsultationService")
	private OnlineConsultationService onlineConsultationService;
	
	private static Logger logger = Logger.getLogger(OnlineConsultationController.class);
	
	//获取医生列表
	@ResponseBody
	@RequestMapping(value = "/doctors.do", method = RequestMethod.GET)
	public ResponseBean doctors(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		List<Doctor> list=this.onlineConsultationService.getDoctors();
		JSONArray arr=new JSONArray();
		for(Doctor d:list){
			arr.add(d.toJsonObj());
		}
		responseBody.setResult(arr);
		return responseBody;
	}
	
	//获取医生列表
	@ResponseBody
	@RequestMapping(value = "/doctor.do", method = RequestMethod.GET)
	public ResponseBean doctor(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		int doctorId=NumberUtils.toInt(request.getParameter("doctorid"));
		Doctor doctor=this.onlineConsultationService.getDoctor(doctorId);
		responseBody.setResult(doctor.toJsonObj());
		return responseBody;
	}
		
	//获取线下活动列表
	@ResponseBody
	@RequestMapping(value = "/activities.do", method = RequestMethod.GET)
	public ResponseBean activities(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		List<Activitie> list=this.onlineConsultationService.getAtivities();
		JSONArray arr=new JSONArray();
		for(Activitie a:list){
			arr.add(a.toJsonObj());
		}
		responseBody.setResult(arr);
		return responseBody;
	}
	
}
