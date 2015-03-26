package com.lehealth.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.data.bean.ResponseBean;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.UserRoleType;

@Controller
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	// 患者注册
	@ResponseBody
	@RequestMapping(value = "/patient/register")
	public ResponseBean register(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password = StringUtils.trimToEmpty(request.getParameter("password"));
		String identifyingCode = StringUtils.trimToEmpty(request.getParameter("identifyingcode"));
		ResponseBean responseBody=new ResponseBean();
		
		if(StringUtils.isBlank(loginId)
				|| StringUtils.isBlank(password)) {
			responseBody.setType(ErrorCodeType.invalidUser);
		}else if(!this.loginService.checkIdentifyingCode(loginId, identifyingCode)) {
			responseBody.setType(ErrorCodeType.invalidIdentifyingCode);
		}else{
			ErrorCodeType type=this.loginService.registerUser(loginId, password, UserRoleType.panient);
			responseBody.setType(type);
		}
		return responseBody;
	}
	
	// 用户登录
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseBean login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		ResponseBean responseBody=new ResponseBean();
		if(StringUtils.isBlank(loginId)
			|| StringUtils.isBlank(password)) {
			responseBody.setType(ErrorCodeType.invalidParam);
		}else {
			UserBaseInfo user=this.loginService.getUserByPassword(loginId, password);
			if(user != null){
				responseBody.setType(ErrorCodeType.normal);
				responseBody.setResult(user.toJsonObj());
			}else{
				responseBody.setType(ErrorCodeType.invalidUser);
			}
		}
		return responseBody;
	}
	
	// 获取验证码
	@ResponseBody
	@RequestMapping(value = "/identifyingcode")
	public ResponseBean identifyingcode(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String phoneNumber=StringUtils.trimToEmpty(request.getParameter("phone"));
		ResponseBean responseBody=new ResponseBean();
		if(phoneNumber.length() != 11
				|| NumberUtils.toLong(phoneNumber) > 0){
			responseBody.setType(ErrorCodeType.invalidUser);
		}else{
			ErrorCodeType type=this.loginService.sendIdentifyingCode(phoneNumber);
			responseBody.setType(type);
		}
		return responseBody;
	}
	
	//用户权限
	@ResponseBody
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public ResponseBean doctorregister(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			responseBody.setResult(user.toJsonObj());
		}else{
			responseBody.setType(ErrorCodeType.invalidUser);
		}
		return responseBody;
	}
	
}
