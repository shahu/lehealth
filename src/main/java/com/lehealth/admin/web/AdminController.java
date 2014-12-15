package com.lehealth.admin.web;

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
import com.lehealth.api.service.DoctorService;
import com.lehealth.api.service.LoginService;
import com.lehealth.bean.ResponseBean;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("doctorService")
	private DoctorService doctorService;
	
	private static Logger logger = Logger.getLogger(AdminController.class);
	
	//医生获取关注的病人列表
	@ResponseBody
	@RequestMapping(value = "/patient/list", method = RequestMethod.GET)
	public ResponseBean getPatients(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		System.out.println("test!!!");
		if(StringUtils.isNotBlank(userId)){
			//List<DoctorInfo> list=this.doctorService.getPanients(userId);
			System.out.println("test!!!");
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//医生获取病人信息
	@ResponseBody
	@RequestMapping(value = "/patient/info", method = RequestMethod.GET)
	public ResponseBean getPatientInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			//List<DoctorInfo> list=this.doctorService.getPanients(userId);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//医生获取病人血压记录
	@ResponseBody
	@RequestMapping(value = "/patient/bp/record/list", method = RequestMethod.GET)
	public ResponseBean getPatientBpRecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			//List<DoctorInfo> list=this.doctorService.getPanients(userId);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//医生获取病人用药记录
	@ResponseBody
	@RequestMapping(value = "/patient/medicine/record/list", method = RequestMethod.GET)
	public ResponseBean getPatientMedicineRecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			//List<DoctorInfo> list=this.doctorService.getPanients(userId);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
		
	//医生获取病史
	@ResponseBody
	@RequestMapping(value = "/patient/disease/list", method = RequestMethod.GET)
	public ResponseBean getPatientDiseases(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			//List<DoctorInfo> list=this.doctorService.getPanients(userId);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
}
