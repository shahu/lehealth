package com.lehealth.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.ResponseBean;

@Controller
@RequestMapping("/api")
public class SettingsController {

	private static Logger logger = Logger.getLogger(SettingsController.class);
	
	//获取血压控制设置
	@ResponseBody
	@RequestMapping(value = "/bpsetting.do", method = RequestMethod.GET)
	public ResponseBean<BloodpressureConfig> getBpSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		logger.info(loginId+","+token);
		
		ResponseBean<BloodpressureConfig> responseBody=new ResponseBean<BloodpressureConfig>();
		BloodpressureConfig bpConfig=new BloodpressureConfig();
		responseBody.setResult(bpConfig);
		
		return responseBody;
	}
	
	//更新血压控制设置
	@ResponseBody
	@RequestMapping(value = "/bpsetting.do", method = RequestMethod.POST)
	public ResponseBean<String> modifyBpSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		
		int dbp1=NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("dbp1")));
		int dbp2=NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("dbp2")));
		int sbp1=NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("sbp1")));
		int sbp2=NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("sbp2")));
		
		logger.info(loginId+","+token+","+dbp1+","+dbp2+","+sbp1+","+sbp2);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
	
	//获取用药设置
	@ResponseBody
	@RequestMapping(value = "/medicinesetting.do", method = RequestMethod.GET)
	public ResponseBean<List<MedicineConfig>> getMedicinesetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		logger.info(loginId+","+token);
		
		ResponseBean<List<MedicineConfig>> responseBody=new ResponseBean<List<MedicineConfig>>();
		List<MedicineConfig> list=new ArrayList<MedicineConfig>();
		responseBody.setResult(list);
		
		return responseBody;
	}
	
	//更新用药设置
	@ResponseBody
	@RequestMapping(value = "/medicinesetting.do", method = RequestMethod.POST)
	public ResponseBean<String> modifyMedicinesetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		int medicineId=NumberUtils.toInt(request.getParameter("Medicineid"));
		int amount=NumberUtils.toInt(request.getParameter("amount"));
		int frequency=NumberUtils.toInt(request.getParameter("frequency"));
		int timing=NumberUtils.toInt(request.getParameter("timing"));
		String startDateStr=StringUtils.trimToEmpty(request.getParameter("datefrom"));
		String endDateStr=StringUtils.trimToEmpty(request.getParameter("dateto"));
		logger.info(loginId+","+token+","+medicineId+","+amount+","+frequency+","+timing+","+startDateStr+","+endDateStr);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
	
	//TODO 获取个人信息
	
	//TODO 更新个人信息
	
}
