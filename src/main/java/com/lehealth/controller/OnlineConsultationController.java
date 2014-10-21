package com.lehealth.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.ResponseBean;

@Controller
@RequestMapping("/api")
public class OnlineConsultationController {

	private static Logger logger = Logger.getLogger(OnlineConsultationController.class);
	
	//获取医生列表
	@ResponseBody
	@RequestMapping(value = "/doctors.do", method = RequestMethod.GET)
	public ResponseBean<List<Doctor>> doctors(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.info("doctors");
		
		ResponseBean<List<Doctor>> responseBody=new ResponseBean<List<Doctor>>();
		List<Doctor> list=new ArrayList<Doctor>();
		responseBody.setResult(list);
		return responseBody;
	}
	
	
	//获取线下活动列表
	@ResponseBody
	@RequestMapping(value = "/activities.do", method = RequestMethod.GET)
	public ResponseBean<List<Activitie>> activities(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		//用户报名信息
		logger.info("activities");
		
		ResponseBean<List<Activitie>> responseBody=new ResponseBean<List<Activitie>>();
		List<Activitie> list=new ArrayList<Activitie>();
		responseBody.setResult(list);
		
		return responseBody;
	}
	
}
