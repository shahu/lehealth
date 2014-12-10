package com.lehealth.sync.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lehealth.sync.service.SyncBloodpressureService;

@Controller
@RequestMapping("/test")
public class SyncBloodpressureController {

	@Autowired
	@Qualifier("syncBloodpressureService")
	private SyncBloodpressureService syncBloodpressureService;
	
	private static Logger logger = Logger.getLogger(SyncBloodpressureController.class);
	
	@Scheduled(cron = "5 0/10 * * * ?")
	public void syncBloodpressureFromYundf() {
		this.syncBloodpressureService.syncFromYundf();
	}
	
	@ResponseBody
	@RequestMapping(value = "/syncyundf.do", method = RequestMethod.GET)
	public String syncYundf(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		this.syncBloodpressureService.syncFromYundf();
		return "ok";
	}
}
