package com.lehealth.controller;

import java.util.List;

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

import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineInfo;
import com.lehealth.bean.MedicineResult;
import com.lehealth.bean.ResponseBean;
import com.lehealth.service.LoginService;
import com.lehealth.service.MedicineService;
import com.lehealth.type.ErrorCodeType;
import com.lehealth.util.JacksonGlobalMappers;

@Controller
@RequestMapping("/api")
public class MedicineController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("medicineService")
	private MedicineService medicineService;
	
	private static Logger logger = Logger.getLogger(MedicineController.class);
	
	//获取用药信息
	@ResponseBody
	@RequestMapping(value = "/medicinerecords.do", method = RequestMethod.GET)
	public ResponseBean medicinerecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.getUserId(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			MedicineResult result=this.medicineService.getMedicineRecords(userId);
			responseBody.setResult(result);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
		
	}
	
	//更新用药记录
	@ResponseBody
	@RequestMapping(value = "/medicinerecord.do", method = RequestMethod.POST)
	public ResponseBean medicinerecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.getUserId(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			float amount=NumberUtils.toFloat(request.getParameter("amount"));
			float frequency=NumberUtils.toFloat(request.getParameter("frequency"));
			int timing=NumberUtils.toInt(request.getParameter("timing"));
			MedicineInfo mInfo=new MedicineInfo();
			mInfo.setUserid(userId);
			mInfo.setMedicineid(medicineId);
			mInfo.setFrequency(frequency);
			mInfo.setAmount(amount);
			mInfo.setTiming(timing);
			if(this.medicineService.modifyMedicineRecord(mInfo)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//药物列表
	@ResponseBody
	@RequestMapping(value = "/medicines.do", method = RequestMethod.GET)
	public ResponseBean medicines(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.getUserId(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<MedicineCategroy> list=this.medicineService.getMedicines();
			responseBody.setResult(list);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
}
