package com.lehealth.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lehealth.bean.Activitie;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.ResponseBean;
import com.lehealth.service.LoginService;
import com.lehealth.service.OnlineConsultationService;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class OnlineConsultationController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("onlineConsultationService")
	private OnlineConsultationService onlineConsultationService;
	
	private static Logger logger = Logger.getLogger(OnlineConsultationController.class);
	
	//获取医生列表
	@ResponseBody
	@RequestMapping(value = "/doctors.do", method = RequestMethod.GET)
	public ResponseBean doctors(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.getUserId(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<Doctor> list=this.onlineConsultationService.getDoctors();
			responseBody.setResult(list);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	
	//获取线下活动列表
	@ResponseBody
	@RequestMapping(value = "/activities.do", method = RequestMethod.GET)
	public ResponseBean activities(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.getUserId(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<Activitie> list=this.onlineConsultationService.getAtivities();
			responseBody.setResult(list);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
}
