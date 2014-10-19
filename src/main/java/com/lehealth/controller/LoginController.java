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

import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.UserInfo;

@Controller
@RequestMapping("/api")
public class LoginController {
	
	private static Logger logger = Logger.getLogger(LoginController.class);
	
	//用户信息
	@ResponseBody
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ResponseBean<UserInfo> login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<UserInfo> responseBody=new ResponseBean<UserInfo>();
		UserInfo user=new UserInfo();
		responseBody.setResult(user);
		
		return responseBody;
	}
	
	//用户注册
	@ResponseBody
	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public ResponseBean<String> register(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		String password2=StringUtils.trimToEmpty(request.getParameter("password2"));
		logger.info(loginId+","+password+","+password2);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
}
