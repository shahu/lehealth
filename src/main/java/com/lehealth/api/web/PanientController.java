package com.lehealth.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.entity.PanientGuardianInfo;
import com.lehealth.api.entity.PanientInfo;
import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.api.service.LoginService;
import com.lehealth.api.service.PanientService;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.JsonArrayResponse;
import com.lehealth.response.bean.JsonObjectResponse;

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
	public JSONObject getPanientInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			PanientInfo info=this.panientService.getPanient(user.getUserId());
			if(StringUtils.isNotBlank(info.getUserId())){
				return new JsonObjectResponse(ErrorCodeType.success, info.toBackendJsonObj()).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.failed).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
	// 患者更新自己个人信息
	@ResponseBody
	@RequestMapping(value = "/panient/modify")
	public JSONObject modifyPanientInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			String userName=StringUtils.trimToEmpty(request.getParameter("username"));
			int gender=NumberUtils.toInt(request.getParameter("gender"));
			long birthday=NumberUtils.toLong(request.getParameter("birthday"));;
			float height=NumberUtils.toFloat(request.getParameter("height"));;
			float weight=NumberUtils.toFloat(request.getParameter("weight"));
			String idNum = StringUtils.trimToEmpty(request.getParameter("idnum"));
			PanientInfo info=new PanientInfo();
			info.setBirthday(birthday);
			info.setGender(gender);
			info.setHeight(height);
			info.setIDNumber(idNum);
			info.setUserId(user.getUserId());
			info.setUserName(userName);
			info.setWeight(weight);
			if(this.panientService.modifyPanient(info)){
				return new BaseResponse(ErrorCodeType.success).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.failed).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
	// 患者获取监护人列表
	@ResponseBody
	@RequestMapping(value = "/guardian/list", method = RequestMethod.GET)
	public JSONObject getGuardianList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			List<PanientGuardianInfo> list=this.panientService.getGuardianList(user.getUserId());
			JSONArray arr=new JSONArray();
			for(PanientGuardianInfo info:list){
				arr.add(info.toJsonObj());
			}
			return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
	// 患者新增自己监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardian/add", method = RequestMethod.POST)
	public JSONObject modifyGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			String guardianName=StringUtils.trimToEmpty(request.getParameter("guardianname"));
			String guardianNumber=StringUtils.trimToEmpty(request.getParameter("guardiannumber"));
			PanientGuardianInfo info=new PanientGuardianInfo();
			info.setUserId(user.getUserId());
			info.setGuardianName(guardianName);
			info.setGuardianNumber(guardianNumber);
			if(this.panientService.modifyGuardian(info)){
				return new BaseResponse(ErrorCodeType.success).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.failed).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
	// 患者删除自己监护人信息
	@ResponseBody
	@RequestMapping(value = "/guardian/delete", method = RequestMethod.POST)
	public JSONObject delGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		String guardianNumber=StringUtils.trimToEmpty(request.getParameter("guardiannumber"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			if(this.panientService.deleteGuardian(user.getUserId(),guardianNumber)){
				return new BaseResponse(ErrorCodeType.success).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.failed).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
}
