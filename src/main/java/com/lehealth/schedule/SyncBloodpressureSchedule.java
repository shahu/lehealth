package com.lehealth.schedule;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lehealth.sync.service.SyncBloodpressureService;

@Controller
@RequestMapping("/test")
public class SyncBloodpressureSchedule {

	@Autowired
	@Qualifier("syncBloodpressureService")
	private SyncBloodpressureService syncBloodpressureService;
	
	private static Logger logger = Logger.getLogger(SyncBloodpressureSchedule.class);
	
	@Scheduled(cron = "5 0/10 * * * ?")
	public void syncBloodpressureFromYundf() {
		logger.info("begin sync from yundf");
//		this.syncBloodpressureService.syncFromYundf();
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/syncyundf.do", method = RequestMethod.GET)
//	public String syncYundf(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		this.syncBloodpressureService.syncFromYundf();
//		return "ok";
//	}
}
