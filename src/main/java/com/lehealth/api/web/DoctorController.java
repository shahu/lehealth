package com.lehealth.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.DoctorService;
import com.lehealth.api.service.LoginService;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.ResponseBean;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class DoctorController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("doctorService")
	private DoctorService doctorService;
	
	private static Logger logger = Logger.getLogger(DoctorController.class);
	
	//获取医生列表
	@ResponseBody
	@RequestMapping(value = "/doctors.do", method = RequestMethod.GET)
	public ResponseBean doctors(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		String userId=this.loginService.checkUser4Token(loginId, token);
		List<Doctor> list=this.doctorService.getDoctors(userId);
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
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		int doctorId=NumberUtils.toInt(request.getParameter("doctorid"));
		String userId=this.loginService.checkUser4Token(loginId, token);
		Doctor doctor=this.doctorService.getDoctor(userId,doctorId);
		if(doctor.getId()!=0){
			responseBody.setResult(doctor.toJsonObj());
		}else{
			responseBody.setType(ErrorCodeType.abnormal);
		}
		return responseBody;
	}
	
	//添加医生关注或取消关注
	@ResponseBody
	@RequestMapping(value = "/attentiondoctor.do", method = RequestMethod.POST)
	public ResponseBean attentiondoctor(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int doctorId=NumberUtils.toInt(request.getParameter("doctorid"));
			int status=NumberUtils.toInt(request.getParameter("status"));
			if(this.doctorService.modifyAttentionStatus(userId,doctorId,status)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
}
