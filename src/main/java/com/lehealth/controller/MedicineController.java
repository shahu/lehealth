package com.lehealth.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineResult;
import com.lehealth.bean.ResponseBean;

@Controller
@RequestMapping("/api")
public class MedicineController {
	
	private static Logger logger = Logger.getLogger(MedicineController.class);
	
	//获取用药信息
	@ResponseBody
	@RequestMapping(value = "/medicinerecords.do", method = RequestMethod.GET)
	public ResponseBean<MedicineResult> medicinerecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		logger.info(loginId+","+token);
		
		ResponseBean<MedicineResult> responseBody=new ResponseBean<MedicineResult>();
		MedicineResult medicineResult=new MedicineResult();
		responseBody.setResult(medicineResult);
		return responseBody;
	}
	
	//更新用药记录
	@ResponseBody
	@RequestMapping(value = "/medicinerecord.do", method = RequestMethod.POST)
	public ResponseBean<String> medicinerecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		String medicineid=StringUtils.trimToEmpty(request.getParameter("medicineid"));
		String frequency=StringUtils.trimToEmpty(request.getParameter("frequency"));
		String timing=StringUtils.trimToEmpty(request.getParameter("timing "));
		logger.info(loginId+","+token+","+medicineid+","+frequency+","+timing);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
	
	//药物列表
	@ResponseBody
	@RequestMapping(value = "/medicines.do", method = RequestMethod.GET)
	public ResponseBean<List<MedicineCategroy>> medicines(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.info("medicines");
		
		ResponseBean<List<MedicineCategroy>> responseBody=new ResponseBean<List<MedicineCategroy>>();
		List<MedicineCategroy> list=new ArrayList<MedicineCategroy>(); 
		responseBody.setResult(list);
		
		return responseBody;
	}
}
