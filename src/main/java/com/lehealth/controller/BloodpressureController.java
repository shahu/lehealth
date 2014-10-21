package com.lehealth.controller;


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

import com.lehealth.bean.BloodpressureResult;
import com.lehealth.bean.ResponseBean;

@Controller
@RequestMapping("/api")
public class BloodpressureController {
	
	private static Logger logger = Logger.getLogger(BloodpressureController.class);
	
	//血压数据获取
	@ResponseBody
	@RequestMapping(value = "/bprecords.do", method = RequestMethod.GET)
	public ResponseBean<BloodpressureResult> bprecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		logger.info(loginId+","+token);
		
		ResponseBean<BloodpressureResult> responseBody=new ResponseBean<BloodpressureResult>();
		BloodpressureResult bpResult=new BloodpressureResult();
		responseBody.setResult(bpResult);
		return responseBody;
	}
	
	//用户血压数据录入
	@ResponseBody
	@RequestMapping(value = "/bprecord.do", method = RequestMethod.POST)
	public ResponseBean<String> bprecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		int dbp=NumberUtils.toInt(request.getParameter("dbp"));
		int sbp=NumberUtils.toInt(request.getParameter("sbp"));
		double heartrate=NumberUtils.toDouble(request.getParameter("heartrate"));
		logger.info(loginId+","+token+","+dbp+","+sbp+","+heartrate);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
}
