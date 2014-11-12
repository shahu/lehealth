package com.lehealth.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

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
import com.lehealth.bean.ResponseBean;
import com.lehealth.service.LoginService;
import com.lehealth.service.MedicineService;
import com.lehealth.type.ErrorCodeType;

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
	@RequestMapping(value = "/medicinehistory.do", method = RequestMethod.GET)
	public ResponseBean getmedicinehistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<MedicineInfo> list=this.medicineService.getMedicineHistory(userId);
			JSONArray arr=new JSONArray();
			for(MedicineInfo info:list){
				arr.add(info.toJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//更新用药记录
	@ResponseBody
	@RequestMapping(value = "/medicinehistory.do", method = RequestMethod.POST)
	public ResponseBean addmedicinehistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			String time=StringUtils.trimToEmpty(request.getParameter("time"));
			float dosage=NumberUtils.toFloat(request.getParameter("dosage"));
			MedicineInfo mInfo=new MedicineInfo();
			mInfo.setUserid(userId);
			mInfo.setMedicineid(medicineId);
			mInfo.addSituation(time, dosage);
			if(this.medicineService.updateMedicineHistory(mInfo)){
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
		ResponseBean responseBody=new ResponseBean();
		List<MedicineCategroy> list=this.medicineService.getMedicines();
		JSONArray arr=new JSONArray();
		for(MedicineCategroy mc:list){
			arr.add(mc.toJsonObj());
		}
		responseBody.setResult(arr);
		return responseBody;
	}
	
}
