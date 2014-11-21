package com.lehealth.api.web;

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
import com.lehealth.api.service.LoginService;
import com.lehealth.api.service.UserService;
import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class UserController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	private static Logger logger = Logger.getLogger(UserController.class);
	
	//获取个人信息
	@ResponseBody
	@RequestMapping(value = "/userinfo.do", method = RequestMethod.GET)
	public ResponseBean getUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			UserInfo info=this.userService.getUserInfo(userId);
			if(StringUtils.isNotBlank(info.getUserId())){
				responseBody.setResult(info.toJsonObj());
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//更新个人信息
	@ResponseBody
	@RequestMapping(value = "/userinfo.do", method = RequestMethod.POST)
	public ResponseBean modifyUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			String userName=StringUtils.trimToEmpty(request.getParameter("username"));
			int gender=NumberUtils.toInt(request.getParameter("gender"));
			long birthday=NumberUtils.toInt(request.getParameter("birthday"));;
			float height=NumberUtils.toInt(request.getParameter("height"));;
			float weight=NumberUtils.toInt(request.getParameter("weight"));
			UserInfo info=new UserInfo();
			info.setBirthday(birthday);
			info.setGender(gender);
			info.setHeight(height);
			info.setUserId(userId);
			info.setUserName(userName);
			info.setWeight(weight);
			if(this.userService.modifyUserInfo(info)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//获取监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardianinfo.do", method = RequestMethod.GET)
	public ResponseBean getGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			UserGuardianInfo info=this.userService.getUserGuardianInfo(userId);
			if(StringUtils.isNotBlank(info.getUserId())){
				responseBody.setResult(info.toJsonObj());
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//新增监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardianinfo.do", method = RequestMethod.POST)
	public ResponseBean modifyGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			String guardianName=StringUtils.trimToEmpty(request.getParameter("guardianname"));
			String guardianNumber=StringUtils.trimToEmpty(request.getParameter("guardiannumber"));
			UserGuardianInfo info=new UserGuardianInfo();
			info.setUserId(userId);
			info.setGuardianName(guardianName);
			info.setGuardianNumber(guardianNumber);
			if(this.userService.modifyUserGuardianInfo(info)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//删除监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardianinfodel.do", method = RequestMethod.POST)
	public ResponseBean delGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			if(this.userService.delUserGuardianInfo(userId)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
}
