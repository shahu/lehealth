package com.lehealth.api.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.common.util.Ipv4Util;
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
			return new BaseResponse(ErrorCodeType.invalidPassword).toJson();
		}else{
			ErrorCodeType type = this.loginService.checkIdentifyingCode(loginId, identifyingCode);
			if(type == ErrorCodeType.success){
				type=this.loginService.registerUser(loginId, password, UserRoleType.panient);
			}
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
				return new JsonObjectResponse(ErrorCodeType.success, user.toJsonObj()).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.invalidPassword).toJson();
			}
		}
	}
	
	private static final Pattern phonePattern = Pattern.compile("^(13[0-9]|15[0-9]|14[7|5]|17[0-9]|18[0-9])\\d{8}$");
	
	// 获取验证码
	@ResponseBody
	@RequestMapping(value = "/identifyingcode")
	public JSONObject identifyingcode(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String phoneNumber=StringUtils.trimToEmpty(request.getParameter("phone"));
		Matcher matcher = phonePattern.matcher(phoneNumber); 
		if(matcher.matches()){
			String ip = Ipv4Util.getIp(request);
			ErrorCodeType type=this.loginService.sendIdentifyingCode(phoneNumber,ip);
			return new BaseResponse(type).toJson();
		}else{
			return new BaseResponse(ErrorCodeType.invalidPhoneNumber).toJson();
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
			return new JsonObjectResponse(ErrorCodeType.success, user.toJsonObj()).toJson();
		}else{
			return new BaseResponse(ErrorCodeType.invalidPassword).toJson();
		}
	}
	
}
