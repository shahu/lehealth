package com.lehealth.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.UserRoleType;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.JsonObjectResponse;

@Controller
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	// 患者注册
	@ResponseBody
	@RequestMapping(value = "/patient/register")
	public JSONObject register(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password = StringUtils.trimToEmpty(request.getParameter("password"));
		String identifyingCode = StringUtils.trimToEmpty(request.getParameter("identifyingcode"));
		if(StringUtils.isBlank(loginId)
				|| StringUtils.isBlank(password)) {
			return new BaseResponse(ErrorCodeType.invalidUser).toJson();
		}else if(!this.loginService.checkIdentifyingCode(loginId, identifyingCode)) {
			return new BaseResponse(ErrorCodeType.invalidIdentifyingCode).toJson();
		}else{
			ErrorCodeType type=this.loginService.registerUser(loginId, password, UserRoleType.panient);
			return new BaseResponse(type).toJson();
		}
	}
	
	// 用户登录
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public JSONObject login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		if(StringUtils.isBlank(loginId)
			|| StringUtils.isBlank(password)) {
			return new BaseResponse(ErrorCodeType.invalidParam).toJson();
		}else {
			UserBaseInfo user=this.loginService.getUserByPassword(loginId, password);
			if(user != null){
				return new JsonObjectResponse(ErrorCodeType.normal, user.toJsonObj()).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.invalidUser).toJson();
			}
		}
	}
	
	// 获取验证码
	@ResponseBody
	@RequestMapping(value = "/identifyingcode")
	public JSONObject identifyingcode(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String phoneNumber=StringUtils.trimToEmpty(request.getParameter("phone"));
		if(phoneNumber.length() != 11
				|| NumberUtils.toLong(phoneNumber) > 0){
			return new BaseResponse(ErrorCodeType.invalidUser).toJson();
		}else{
			ErrorCodeType type=this.loginService.sendIdentifyingCode(phoneNumber);
			return new BaseResponse(type).toJson();
		}
	}
	
	//用户权限
	@ResponseBody
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public JSONObject doctorregister(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			return new JsonObjectResponse(ErrorCodeType.normal, user.toJsonObj()).toJson();
		}else{
			return new BaseResponse(ErrorCodeType.invalidUser).toJson();
		}
	}
	
}
