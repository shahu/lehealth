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

import com.lehealth.api.service.DoctorService;
import com.lehealth.api.service.LoginService;
import com.lehealth.data.bean.DoctorInfo;
import com.lehealth.data.bean.ResponseBean;
import com.lehealth.data.bean.UserInfomation;
import com.lehealth.data.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class DoctorController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("doctorService")
	private DoctorService doctorService;
	
	//患者获取医生列表
	@ResponseBody
	@RequestMapping(value = "/doctor/list", method = RequestMethod.GET)
//	@RequestMapping(value = "/doctors.do", method = RequestMethod.GET)
	public ResponseBean getDoctors(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		JSONArray arr=new JSONArray();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			List<DoctorInfo> list=this.doctorService.getInfoList(user.getUserId());
			for(DoctorInfo d:list){
				arr.add(d.toJsonObj());
			}
		}
		responseBody.setResult(arr);
		return responseBody;
	}
	
	//患者获取医生信息
	@ResponseBody
	@RequestMapping(value = "/doctor/info", method = RequestMethod.GET)
//	@RequestMapping(value = "/doctor.do", method = RequestMethod.GET)
	public ResponseBean getDoctor(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ResponseBean responseBody=new ResponseBean();
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		String doctorId=StringUtils.trimToEmpty(request.getParameter("doctorid"));
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			DoctorInfo doctor=this.doctorService.getInfo(user.getUserId(),doctorId);
			if(StringUtils.isNotBlank(doctor.getId())){
				responseBody.setResult(doctor.toJsonObj());
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.abnormal);
		}
		return responseBody;
	}
	
	//患者添加医生关注或取消关注
	@ResponseBody
	@RequestMapping(value = "/doctor/attention", method = RequestMethod.POST)
//	@RequestMapping(value = "/attentiondoctor.do", method = RequestMethod.POST)
	public ResponseBean addDoctorAttention(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			String doctorId=StringUtils.trimToEmpty(request.getParameter("doctorid"));
			int attention=NumberUtils.toInt(request.getParameter("attention"));
			if(this.doctorService.modifyAttentionStatus(user.getUserId(),doctorId,attention)){
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
