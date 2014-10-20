package com.lehealth.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.bean.ResponseBean;
import com.lehealth.util.Constant;

@Controller
@RequestMapping("/api")
public class BloodpressureController {
	
	private static Logger logger = Logger.getLogger(BloodpressureController.class);
	
	//血压数据获取
	@ResponseBody
	@RequestMapping(value = "/bprecord.do", method = RequestMethod.GET)
	public ResponseBean<BloodpressureInfo> searchBpRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		logger.info(loginId+","+password);
		
		ResponseBean<BloodpressureInfo> responseBody=new ResponseBean<BloodpressureInfo>();
		BloodpressureInfo bpInfo=new BloodpressureInfo();
		int lastDays=60;
		Random r=new Random();
		Date d=new Date();
		Map<Date,Integer> map1=new HashMap<Date,Integer>();
		Map<Date,Integer> map2=new HashMap<Date,Integer>();
		while(lastDays>0){
			d=DateUtils.addHours(d, -12);
			map1.put(d, 70+r.nextInt(20));
			map2.put(d, 90+r.nextInt(20));
			lastDays--;
		}
		bpInfo.setDbp(map1);
		bpInfo.setSbp(map2);
		responseBody.setResult(bpInfo);
		return responseBody;
	}
	
	//用户血压数据录入
	@ResponseBody
	@RequestMapping(value = "/bprecord.do", method = RequestMethod.POST)
	public ResponseBean<String> modifyBpRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		String checkDate=StringUtils.trimToEmpty(request.getParameter("checkDate"));
		
		int dbp=NumberUtils.toInt(request.getParameter("dbp"));
		int sbp=NumberUtils.toInt(request.getParameter("sbp"));
		double heartrate=NumberUtils.toDouble(request.getParameter("heartrate"));
		logger.info(loginId+","+password+","+checkDate+","+dbp+","+sbp+","+heartrate);
		
		ResponseBean<String> responseBody=new ResponseBean<String>();
		responseBody.setResult("");
		
		return responseBody;
	}
}
