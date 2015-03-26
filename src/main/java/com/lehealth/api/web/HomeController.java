package com.lehealth.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.HomeService;
import com.lehealth.api.service.LoginService;
import com.lehealth.data.bean.HomeResult;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.JsonObjectResponse;

@Controller
@RequestMapping("/api/home")
public class HomeController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("homeService")
	private HomeService homeService;
	
	//患者首页数据接口
	@ResponseBody
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public JSONObject getHomeData(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token = StringUtils.trimToEmpty(request.getParameter("token"));
		String targetUserId = StringUtils.trimToEmpty(request.getParameter("user"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			int days = NumberUtils.toInt(request.getParameter("days"), 7);
			if(days <= 0){
				days = 7;
			}
			// 看自己的首页
			if(StringUtils.isBlank(targetUserId)){
				HomeResult result = this.homeService.getHomeData(user, days);
				return new JsonObjectResponse(ErrorCodeType.normal, result.toJsonObj()).toJson();
			// 看别人的首页
			}else{
				HomeResult result = this.homeService.getHomeData(targetUserId, days);
				return new JsonObjectResponse(ErrorCodeType.normal, result.toJsonObj()).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
}
