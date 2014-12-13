package com.lehealth.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.UserInfomation;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	//用户登录
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
				UserInfomation user=new UserInfomation();
				user.setLoginId(loginId);
				user.setPassword(password);
				responseBody.setResult(user.toJsonObj());
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidUser);
		}
		
		return responseBody;
	}
	
	//患者注册
	@ResponseBody
	@RequestMapping(value = "/patient/register.do", method = RequestMethod.POST)
	public ResponseBean register(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		ResponseBean responseBody=new ResponseBean();
		if(StringUtils.isNotBlank(loginId)&&StringUtils.isNotBlank(password)){
			ErrorCodeType type=this.loginService.registerNewUser(loginId,password,4);
			responseBody.setType(type);
		}else{
			responseBody.setType(ErrorCodeType.invalidUser);
		}
		return responseBody;
	}
	
	//医生注册
	@ResponseBody
	@RequestMapping(value = "/doctor/register.do", method = RequestMethod.POST)
	public ResponseBean doctorregister(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		ResponseBean responseBody=new ResponseBean();
		if(StringUtils.isNotBlank(loginId)&&StringUtils.isNotBlank(password)){
			ErrorCodeType type=this.loginService.registerNewUser(loginId,password,2);
			if(type==ErrorCodeType.normal){
				//创建用户成功则获取医生信息进行录入
				//TODO
			}else{
				responseBody.setType(type);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidUser);
		}
		return responseBody;
	}
	
}
