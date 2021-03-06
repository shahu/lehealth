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
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.common.service.SendSMSService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.pay.service.WeixinPayService;

@Controller
@RequestMapping("/test")
public class CacheSchedule {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("sendSMSService")
	private SendSMSService sendSMSService;
	
	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;
	
	@Autowired
	@Qualifier("commonCacheService")
	private CommonCacheService commonCacheService;
	
	@Autowired
	@Qualifier("weixinPayService")
	private WeixinPayService weixinPayService;
	
	// 定时清理验证码
	@Scheduled(cron = "5 1 * * * ?")
	public void clearIdentifyingCodeCache() {
		this.loginService.clearIdentifyingCodeCache();
	}
	
	// 定时更新系统变量
	@Scheduled(cron = "5 1/5 * * * ?")
	public void refreshSystemVariable() {
		this.systemVariableService.updateSystemVariable();
	}
	
	// 定时更新外部系统数据缓存，30分钟间隔
	@Scheduled(cron = "5 1/30 * * * ?")
	public void updateCommonCache30() {
		this.commonCacheService.updateCommonCache30();
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/refreshSystemVariable.do", method = RequestMethod.GET)
//	public String refreshSystemVariable(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		this.systemVariableService.updateSystemVariable();
//		return "refreshSystemVariable";
//	}
	
//	@ResponseBody
//	@RequestMapping(value = "/clearIdentifyingCode.do", method = RequestMethod.GET)
//	public String clearIdentifyingCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		this.loginService.clearIdentifyingCodeCache();
//		return "clearIdentifyingCode";
//	}
	
//	@ResponseBody
//	@RequestMapping(value = "/refreshCommonCache30.do", method = RequestMethod.GET)
//	public String refreshCommonCache30(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		this.commonCacheService.updateCommonCache30();
//		return "updateCommonCache30";
//	}
	
//	@ResponseBody
//	@RequestMapping(value = "/getCache.do", method = RequestMethod.GET)
//	public String getCache(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		JSONObject obj = this.loginService.getMapCache();
//		obj.accumulate("systemvariable", this.systemVariableService.getCache());
//		return obj.toString();
//	}
	
	@ResponseBody
	@RequestMapping(value = "/notice.do", method = RequestMethod.GET)
	public String getCache(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return "o";
	}
}
