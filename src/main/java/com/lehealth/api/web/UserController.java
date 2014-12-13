package com.lehealth.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.api.service.UserService;
import com.lehealth.bean.DiseaseHistory;
import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.PanientGuardianInfo;
import com.lehealth.bean.PanientInfo;
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
	
	//患者获取自己个人信息
	@ResponseBody
	@RequestMapping(value = "/userinfo.do", method = RequestMethod.GET)
	public ResponseBean getUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			PanientInfo info=this.userService.getUserInfo(userId);
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
	
	//患者更新自己个人信息
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
			PanientInfo info=new PanientInfo();
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
	
	//患者获取自己监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardianinfos.do", method = RequestMethod.GET)
	public ResponseBean getGuardianInfos(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<PanientGuardianInfo> list=this.userService.getUserGuardianInfos(userId);
			JSONArray arr=new JSONArray();
			for(PanientGuardianInfo info:list){
				arr.add(info.toJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//患者新增自己监护人信息
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
			PanientGuardianInfo info=new PanientGuardianInfo();
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
	
	//患者删除自己监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardianinfodel.do", method = RequestMethod.POST)
	public ResponseBean delGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		String guardianNumber=StringUtils.trimToEmpty(request.getParameter("guardiannumber"));
		if(StringUtils.isNotBlank(userId)){
			if(this.userService.delUserGuardianInfo(userId,guardianNumber)){
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
