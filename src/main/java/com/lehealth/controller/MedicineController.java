package com.lehealth.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.BpInfo;
import com.lehealth.bean.ResponseBean;
import com.lehealth.util.Constant;

@Controller
@RequestMapping("/api")
public class MedicineController {
	
	private static Logger logger = Logger.getLogger(MedicineController.class);
	
	//获取用药信息
	@ResponseBody
	@RequestMapping(value = "/medicinerecord.do", method = RequestMethod.GET)
	public ResponseBean<BpInfo> searchMedicineRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<BpInfo> responseBody=new ResponseBean<BpInfo>();
		BpInfo bpInfo=new BpInfo();
		int lastDays=30;
		Random r=new Random();
		Date d=DateUtils.addDays(new Date(), -1);
		bpInfo.setDates(d, lastDays);
		Map<String,Integer> map1=new HashMap<String,Integer>();
		Map<String,Integer> map2=new HashMap<String,Integer>();
		while(lastDays>0){
			map1.put(DateFormatUtils.format(d, Constant.dateFormat_yyyy_mm_dd), 6+r.nextInt(6));
			map2.put(DateFormatUtils.format(d, Constant.dateFormat_yyyy_mm_dd), 12+r.nextInt(6));
			lastDays--;
		}
		bpInfo.setDbp(map1);
		bpInfo.setSbp(map2);
		responseBody.setResult(bpInfo);
		return responseBody;
	}
	
	//更新用药记录
	@ResponseBody
	@RequestMapping(value = "/medicinerecord.do", method = RequestMethod.POST)
	public ResponseBean<String> modifyMedicineRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		String checkDate=StringUtils.trimToEmpty(request.getParameter("checkDate"));
		String medicineName=StringUtils.trimToEmpty(request.getParameter("medicineName"));
		logger.info(loginId+","+password+","+checkDate+","+medicineName);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
}
