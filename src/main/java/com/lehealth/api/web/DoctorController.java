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

import com.lehealth.api.service.DoctorService;
import com.lehealth.api.service.LoginService;
import com.lehealth.data.bean.DoctorInfo;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.JsonArrayResponse;
import com.lehealth.response.bean.JsonObjectResponse;

@Controller
@RequestMapping("/api/doctor")
public class DoctorController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("doctorService")
	private DoctorService doctorService;
	
	//患者获取医生列表
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSONObject getDoctors(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		JSONArray arr=new JSONArray();
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			List<DoctorInfo> list=this.doctorService.getInfoList(user.getUserId());
			for(DoctorInfo d:list){
				arr.add(d.toJsonObj());
			}
		}
		return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
	}
	
	//患者获取医生信息
	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public JSONObject getDoctor(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		String doctorId=StringUtils.trimToEmpty(request.getParameter("doctorid"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			DoctorInfo doctor=this.doctorService.getInfo(user.getUserId(),doctorId);
			if(StringUtils.isNotBlank(doctor.getId())){
				return new JsonObjectResponse(ErrorCodeType.success, doctor.toJsonObj()).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.abnormal).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
	//患者添加医生关注或取消关注
	@ResponseBody
	@RequestMapping(value = "/attention", method = RequestMethod.POST)
	public JSONObject addDoctorAttention(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			String doctorId=StringUtils.trimToEmpty(request.getParameter("doctorid"));
			int attention=NumberUtils.toInt(request.getParameter("attention"));
			if(this.doctorService.modifyAttentionStatus(user.getUserId(),doctorId,attention)){
				return new BaseResponse(ErrorCodeType.success).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.abnormal).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
}
