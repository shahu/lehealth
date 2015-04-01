package com.lehealth.schedule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.common.service.SendTemplateSMSService;

@Controller
@RequestMapping("/test")
public class ClearCacheSchedule {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("sendTemplateSMSService")
	private SendTemplateSMSService sendTemplateSMSService;
	
	@Scheduled(cron = "5 1 * * * ?")
	public void syncBloodpressureFromYundf() {
		this.loginService.clearIdentifyingCodeCache();
	}
	
	//@ResponseBody
	//@RequestMapping(value = "/send1.do", method = RequestMethod.GET)
	public String send1(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		this.sendTemplateSMSService.sendIdentifyingCodeSMS("18621545318", "123456");
		return "send1";
	}
	
	//@ResponseBody
	//@RequestMapping(value = "/send2.do", method = RequestMethod.GET)
	public String send2(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		this.sendTemplateSMSService.sendNoticeSMS("18621545318", "aaa", "99", "88");
		return "send2";
	}
	
	@ResponseBody
	@RequestMapping(value = "/clear.do", method = RequestMethod.GET)
	public String clear(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		this.loginService.clearIdentifyingCodeCache();
		return "clear";
	}
	
	@ResponseBody
	@RequestMapping(value = "/map.do", method = RequestMethod.GET)
	public String map(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return this.loginService.getMapCache().toString();
	}
}
