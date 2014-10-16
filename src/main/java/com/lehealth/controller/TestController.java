/**
 * 
 */
package com.lehealth.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.BpInfo;
import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.UserInfo;
import com.lehealth.service.TestService;

@Controller
@RequestMapping("/api")
public class TestController {
	private static Logger logger = Logger.getLogger(TestController.class);
	
	@Autowired
    @Qualifier("testService")
    private TestService testService;
	
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseBean<UserInfo> login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<UserInfo> responseBody=new ResponseBean<UserInfo>();
		UserInfo user=new UserInfo();
		responseBody.setResult(user);
		
		return responseBody;
	}
	
	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseBean<String> register(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		String password2=StringUtils.trimToEmpty(request.getParameter("password2"));
		logger.info(loginId+","+password+","+password2);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
	
	@RequestMapping(value = "/bprecord.do", method = RequestMethod.GET)
	@ResponseBody
	public ResponseBean<BpInfo> searchBpRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<BpInfo> responseBody=new ResponseBean<BpInfo>();
		BpInfo bpInfo=new BpInfo();
		responseBody.setResult(bpInfo);
		
		return responseBody;
	}
	
	@RequestMapping(value = "/bprecord.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseBean<String> modifyBpRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		
		Date date=new Date();
		int dbp=NumberUtils.toInt(request.getParameter("dbp"));
		int sbp=NumberUtils.toInt(request.getParameter("sbp"));
		double heartrate=NumberUtils.toDouble(request.getParameter("heartrate"));
		logger.info(loginId+","+password);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
	
	@RequestMapping(value = "/medicinerecord.do", method = RequestMethod.GET)
	@ResponseBody
	public ResponseBean<BpInfo> searchMedicineRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<BpInfo> responseBody=new ResponseBean<BpInfo>();
		BpInfo bpInfo=new BpInfo();
		responseBody.setResult(bpInfo);
		
		return responseBody;
	}
	
	@RequestMapping(value = "/medicinerecord.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseBean<String> modifyMedicineRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		
		Date date=new Date();
		int dbp=NumberUtils.toInt(request.getParameter("dbp"));
		int sbp=NumberUtils.toInt(request.getParameter("sbp"));
		double heartrate=NumberUtils.toDouble(request.getParameter("heartrate"));
		logger.info(loginId+","+password);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
}
