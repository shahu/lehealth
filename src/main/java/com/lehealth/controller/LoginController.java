package com.lehealth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.User;
import com.lehealth.service.LoginService;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	private static Logger logger = Logger.getLogger(LoginController.class);
	
	//用户信息
	@ResponseBody
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public ResponseBean login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		ResponseBean responseBody=new ResponseBean();
		if(StringUtils.isNotBlank(loginId)&&StringUtils.isNotBlank(password)){
			ErrorCodeType type=this.loginService.checkUser4Login(loginId, password);
			responseBody.setType(type);
			if(type==ErrorCodeType.normal){
				User user=new User();
				user.setLoginid(loginId);
				user.setPassword(password);
				responseBody.setResult(user);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidUser);
		}
		
		return responseBody;
	}
	
	//用户注册
	@ResponseBody
	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public ResponseBean register(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		ResponseBean responseBody=new ResponseBean();
		if(StringUtils.isNotBlank(loginId)&&StringUtils.isNotBlank(password)){
			ErrorCodeType type=this.loginService.registerNewUser(loginId,password);
			responseBody.setType(type);
		}else{
			responseBody.setType(ErrorCodeType.invalidUser);
		}
		return responseBody;
	}
}
