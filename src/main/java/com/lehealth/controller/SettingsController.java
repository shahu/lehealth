package com.lehealth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.UserInfo;

@Controller
@RequestMapping("/api")
public class SettingsController {

	private static Logger logger = Logger.getLogger(SettingsController.class);
	
	//获取血压控制设置
	@ResponseBody
	@RequestMapping(value = "/getBloodpressureInfo.do", method = RequestMethod.GET)
	public ResponseBean<BloodpressureConfig> getBloodpressureInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<BloodpressureConfig> responseBody=new ResponseBean<BloodpressureConfig>();
		BloodpressureConfig bpConfig=new BloodpressureConfig();
		responseBody.setResult(bpConfig);
		
		return responseBody;
	}
	
	//更新血压控制设置
	@ResponseBody
	@RequestMapping(value = "/updateBloodpressureInfo.do", method = RequestMethod.POST)
	public ResponseBean<String> updateBloodpressureInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
	
	//获取用药设置
	@ResponseBody
	@RequestMapping(value = "/getMedicineInfo.do", method = RequestMethod.GET)
	public ResponseBean<MedicineConfig> getMedicineInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<MedicineConfig> responseBody=new ResponseBean<MedicineConfig>();
		MedicineConfig medicineConfig=new MedicineConfig();
		responseBody.setResult(medicineConfig);
		
		return responseBody;
	}
	
	//更新用药设置
	@ResponseBody
	@RequestMapping(value = "/updateMedicineInfo.do", method = RequestMethod.POST)
	public ResponseBean<String> updateMedicineInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
	
	//获取个人信息
	@ResponseBody
	@RequestMapping(value = "/getUserInfo.do", method = RequestMethod.GET)
	public ResponseBean<UserInfo> getUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<UserInfo> responseBody=new ResponseBean<UserInfo>();
		UserInfo user=new UserInfo();
		responseBody.setResult(user);
		
		return responseBody;
	}
	
	
	//更新个人信息
	@ResponseBody
	@RequestMapping(value = "/getUserInfo.do", method = RequestMethod.POST)
	public ResponseBean<String> updateUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
	
}
