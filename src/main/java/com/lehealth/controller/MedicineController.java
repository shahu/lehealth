package com.lehealth.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.lehealth.bean.MedicineInfo;
import com.lehealth.bean.ResponseBean;
import com.lehealth.util.Constant;

@Controller
@RequestMapping("/api")
public class MedicineController {
	
	private static Logger logger = Logger.getLogger(MedicineController.class);
	
	//获取用药信息
	@ResponseBody
	@RequestMapping(value = "/medicinerecord.do", method = RequestMethod.GET)
	public ResponseBean<MedicineInfo> searchMedicineRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<MedicineInfo> responseBody=new ResponseBean<MedicineInfo>();
		MedicineInfo medicineInfo=new MedicineInfo();
		int lastDays=30;
		Random r=new Random();
		Date d=new Date();
		Map<Integer,ArrayList<Date>> map=new HashMap<Integer,ArrayList<Date>>();
		while(lastDays>0){
			int key=r.nextInt(5);
			if(!map.containsKey(key)){
				map.put(key, new ArrayList<Date>());
			}
			map.get(key).add(DateUtils.addDays(d, -lastDays));
			lastDays--;
		}
		responseBody.setResult(medicineInfo);
		return responseBody;
	}
	
	//更新用药记录
	@ResponseBody
	@RequestMapping(value = "/medicinerecord.do", method = RequestMethod.POST)
	public ResponseBean<String> modifyMedicineRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("l"));
		String password=StringUtils.trimToEmpty(request.getParameter("p"));
		String medicineDate=StringUtils.trimToEmpty(request.getParameter("md"));
		String medicineId=StringUtils.trimToEmpty(request.getParameter("mi"));
		String medicineName=StringUtils.trimToEmpty(request.getParameter("mn"));
		logger.info(loginId+","+password+","+medicineDate+","+medicineId+","+medicineName);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
}
