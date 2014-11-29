package com.lehealth.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.BloodpressureService;
import com.lehealth.api.service.HomeService;
import com.lehealth.api.service.LoginService;
import com.lehealth.bean.HomeResult;
import com.lehealth.bean.ResponseBean;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class HomeController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("homeService")
	private HomeService homeService;
	
	private static Logger logger = Logger.getLogger(HomeController.class);
	
	//首页数据接口
	@ResponseBody
	@RequestMapping(value = "/homedata.do", method = RequestMethod.GET)
	public ResponseBean homeData(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int days=NumberUtils.toInt(request.getParameter("days"),7);
			if(days==0){
				days=7;
			}
			HomeResult result=this.homeService.getHomeData(userId,days);
			responseBody.setResult(result.toJsonObj());
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
}
