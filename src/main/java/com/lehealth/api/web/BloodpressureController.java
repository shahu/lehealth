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
import com.lehealth.api.service.LoginService;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.BloodpressureInfo;
import com.lehealth.bean.BloodpressureResult;
import com.lehealth.bean.ResponseBean;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class BloodpressureController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("bloodpressureService")
	private BloodpressureService bloodpressureService;
	
	private static Logger logger = Logger.getLogger(BloodpressureController.class);
	
	//血压数据获取
	@ResponseBody
	@RequestMapping(value = "/bprecords.do", method = RequestMethod.GET)
	public ResponseBean bprecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int days=NumberUtils.toInt(request.getParameter("days"));
			BloodpressureResult result=this.bloodpressureService.getBloodpressureRecords(userId,days);
			responseBody.setResult(result.toJsonObj());
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//用户血压数据录入
	@ResponseBody
	@RequestMapping(value = "/bprecord.do", method = RequestMethod.POST)
	public ResponseBean bprecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int dbp=NumberUtils.toInt(request.getParameter("dbp"));
			int sbp=NumberUtils.toInt(request.getParameter("sbp"));
			int heartrate=NumberUtils.toInt(request.getParameter("heartrate"));
			long date=NumberUtils.toLong(request.getParameter("date"));
			if(date==0){
				date=System.currentTimeMillis();
			}
			BloodpressureInfo bpInfo=new BloodpressureInfo();
			bpInfo.setUserId(userId);
			bpInfo.setDbp(dbp);
			bpInfo.setSbp(sbp);
			bpInfo.setHeartrate(heartrate);
			bpInfo.setDate(date);
			if(this.bloodpressureService.modifyBloodpressureRecord(bpInfo)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//获取血压控制设置
		@ResponseBody
		@RequestMapping(value = "/bpsetting.do", method = RequestMethod.GET)
		public ResponseBean getBpSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
			String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
			String token=StringUtils.trimToEmpty(request.getParameter("token"));
			ResponseBean responseBody=new ResponseBean();
			String userId=this.loginService.checkUser4Token(loginId, token);
			if(StringUtils.isNotBlank(userId)){
				BloodpressureConfig bpConfig=this.bloodpressureService.getBloodpressureSetting(userId);
				if(StringUtils.isBlank(bpConfig.getUserId())){
					responseBody.setType(ErrorCodeType.abnormal);
				}
				else{
					responseBody.setResult(bpConfig.toJsonObj());
				}
			}else{
				responseBody.setType(ErrorCodeType.invalidToken);
			}
			return responseBody;
		}
		
		//更新血压控制设置
		@ResponseBody
		@RequestMapping(value = "/bpsetting.do", method = RequestMethod.POST)
		public ResponseBean modifyBpSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
			String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
			String token=StringUtils.trimToEmpty(request.getParameter("token"));
			ResponseBean responseBody=new ResponseBean();
			String userId=this.loginService.checkUser4Token(loginId, token);
			if(StringUtils.isNotBlank(userId)){
				int dbp1=NumberUtils.toInt(request.getParameter("dbp1"));
				int dbp2=NumberUtils.toInt(request.getParameter("dbp2"));
				int sbp1=NumberUtils.toInt(request.getParameter("sbp1"));
				int sbp2=NumberUtils.toInt(request.getParameter("sbp2"));
				int heartrate1=NumberUtils.toInt(request.getParameter("heartrate1"));
				int heartrate2=NumberUtils.toInt(request.getParameter("heartrate2"));
				BloodpressureConfig bpConfig=new BloodpressureConfig();
				bpConfig.setUserId(userId);
				bpConfig.setDbp1(dbp1);
				bpConfig.setDbp2(dbp2);
				bpConfig.setSbp1(sbp1);
				bpConfig.setSbp2(sbp2);
				bpConfig.setHeartrate1(heartrate1);
				bpConfig.setHeartrate2(heartrate2);
				if(this.bloodpressureService.modifyBloodpressureSetting(bpConfig)){
					responseBody.setType(ErrorCodeType.normal);
				}else{
					responseBody.setType(ErrorCodeType.abnormal);
				}
			}else{
				responseBody.setType(ErrorCodeType.invalidToken);
			}
			return responseBody;
		}
		
}
