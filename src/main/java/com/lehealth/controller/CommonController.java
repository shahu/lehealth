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
import com.lehealth.bean.DiseaseCategroy;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.ResponseBean;
import com.lehealth.service.CommonService;
import com.lehealth.service.LoginService;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class CommonController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("commonService")
	private CommonService commonService;
	
	private static Logger logger = Logger.getLogger(CommonController.class);
	
	//获取医生列表
	@ResponseBody
	@RequestMapping(value = "/doctors.do", method = RequestMethod.GET)
	public ResponseBean doctors(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		List<Doctor> list=this.commonService.getDoctors();
		JSONArray arr=new JSONArray();
		for(Doctor d:list){
			arr.add(d.toJsonObj());
		}
		responseBody.setResult(arr);
		return responseBody;
	}
	
	//获取医生信息
	@ResponseBody
	@RequestMapping(value = "/doctor.do", method = RequestMethod.GET)
	public ResponseBean doctor(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		int doctorId=NumberUtils.toInt(request.getParameter("doctorid"));
		Doctor doctor=this.commonService.getDoctor(doctorId);
		if(doctor.getId()!=0){
			responseBody.setResult(doctor.toJsonObj());
		}else{
			responseBody.setType(ErrorCodeType.abnormal);
		}
		return responseBody;
	}
		
	//获取线下活动列表
	@ResponseBody
	@RequestMapping(value = "/activities.do", method = RequestMethod.GET)
	public ResponseBean activities(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		List<Activitie> list=this.commonService.getAtivities();
		JSONArray arr=new JSONArray();
		for(Activitie a:list){
			arr.add(a.toJsonObj());
		}
		responseBody.setResult(arr);
		return responseBody;
	}
	
	//药物列表
	@ResponseBody
	@RequestMapping(value = "/medicines.do", method = RequestMethod.GET)
	public ResponseBean medicines(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		List<MedicineCategroy> list=this.commonService.getMedicines();
		JSONArray arr=new JSONArray();
		for(MedicineCategroy mc:list){
			arr.add(mc.toJsonObj());
		}
		responseBody.setResult(arr);
		return responseBody;
	}
		
	//疾病列表
	@ResponseBody
	@RequestMapping(value = "/diseases.do", method = RequestMethod.GET)
	public ResponseBean diseases(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		List<DiseaseCategroy> list=this.commonService.getDiseases();
		JSONArray arr=new JSONArray();
		for(DiseaseCategroy mc:list){
			arr.add(mc.toJsonObj());
		}
		responseBody.setResult(arr);
		return responseBody;
	}
}
