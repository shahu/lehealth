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
import com.lehealth.api.service.PanientService;
import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.PanientGuardianInfo;
import com.lehealth.bean.PanientInfo;
import com.lehealth.bean.UserInfomation;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class PanientController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("panientService")
	private PanientService panientService;
	
	// 患者获取自己个人信息
	@ResponseBody
	@RequestMapping(value = "/panient/info", method = RequestMethod.GET)
	public ResponseBean getPanientInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			PanientInfo info=this.panientService.getPanient(user.getUserId());
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
	
	// 患者更新自己个人信息
	@ResponseBody
	@RequestMapping(value = "/panient/modify", method = RequestMethod.POST)
	public ResponseBean modifyPanientInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			String userName=StringUtils.trimToEmpty(request.getParameter("username"));
			int gender=NumberUtils.toInt(request.getParameter("gender"));
			long birthday=NumberUtils.toInt(request.getParameter("birthday"));;
			float height=NumberUtils.toInt(request.getParameter("height"));;
			float weight=NumberUtils.toInt(request.getParameter("weight"));
			PanientInfo info=new PanientInfo();
			info.setBirthday(birthday);
			info.setGender(gender);
			info.setHeight(height);
			info.setUserId(user.getUserId());
			info.setUserName(userName);
			info.setWeight(weight);
			if(this.panientService.modifyPanient(info)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	// 患者获取监护人列表
	@ResponseBody
	@RequestMapping(value = "/guardian/list", method = RequestMethod.GET)
	public ResponseBean getGuardianList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			List<PanientGuardianInfo> list=this.panientService.getGuardianList(loginId);
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
	
	// 患者新增自己监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardian/add", method = RequestMethod.POST)
	public ResponseBean modifyGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			String guardianName=StringUtils.trimToEmpty(request.getParameter("guardianname"));
			String guardianNumber=StringUtils.trimToEmpty(request.getParameter("guardiannumber"));
			PanientGuardianInfo info=new PanientGuardianInfo();
			info.setUserId(user.getUserId());
			info.setGuardianName(guardianName);
			info.setGuardianNumber(guardianNumber);
			if(this.panientService.modifyGuardian(info)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	// 患者删除自己监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardian/delete", method = RequestMethod.POST)
	public ResponseBean delGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String guardianNumber=StringUtils.trimToEmpty(request.getParameter("guardiannumber"));
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			if(this.panientService.deleteGuardian(user.getUserId(),guardianNumber)){
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
