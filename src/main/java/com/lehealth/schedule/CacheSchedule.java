package com.lehealth.schedule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.common.service.SystemVariableService;

@Controller
@RequestMapping("/test")
public class CacheSchedule {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("sendTemplateSMSService")
	private SendTemplateSMSService sendTemplateSMSService;
	
	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;
	
	@Scheduled(cron = "5 1 * * * ?")
	public void syncBloodpressureFromYundf() {
		this.loginService.clearIdentifyingCodeCache();
	}
	
	@Scheduled(cron = "5 1/5 * * * ?")
	public void refreshSystemVariable() {
		this.systemVariableService.updateSystemVariable();
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/send1.do", method = RequestMethod.GET)
//	public String send1(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		this.sendTemplateSMSService.sendIdentifyingCodeSMS("18621545318", "222222");
//		return "send1";
//	}
	
//	@ResponseBody
//	@RequestMapping(value = "/send2.do", method = RequestMethod.GET)
//	public String send2(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		this.sendTemplateSMSService.sendNoticeSMS("18621545318", "bbb", "111", "99");
//		return "send2";
//	}
	
	@ResponseBody
	@RequestMapping(value = "/refresh.do", method = RequestMethod.GET)
	public String refreshsystemvariable(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		this.systemVariableService.updateSystemVariable();
		return "ok";
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
		JSONObject obj = this.loginService.getMapCache();
		obj.accumulate("systemvariable", this.systemVariableService.getCache());
		return obj.toString();
	}
}
