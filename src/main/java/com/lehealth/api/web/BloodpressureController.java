package com.lehealth.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.BloodpressureService;
import com.lehealth.api.service.LoginService;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.BloodpressureRecord;
import com.lehealth.bean.BloodpressureResult;
import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.UserInfomation;
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
	
	//患者获取自己血压数据
	@ResponseBody
	@RequestMapping(value = "/bp/record/list", method = RequestMethod.GET)
//	@RequestMapping(value = "/bprecords.do", method = RequestMethod.GET)
	public ResponseBean getBpRecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			int days=NumberUtils.toInt(request.getParameter("days"),7);
			if(days==0){
				days=7;
			}
			BloodpressureResult result=this.bloodpressureService.getRecords(user.getUserId(),days);
			responseBody.setResult(result.toJsonObj());
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//患者录入自己血压数据
	@ResponseBody
	@RequestMapping(value = "/bp/record/add", method = RequestMethod.POST)
//	@RequestMapping(value = "/bprecord.do", method = RequestMethod.POST)
	public ResponseBean addBpRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			int dbp=NumberUtils.toInt(request.getParameter("dbp"));
			int sbp=NumberUtils.toInt(request.getParameter("sbp"));
			int heartrate=NumberUtils.toInt(request.getParameter("heartrate"));
			int dosed=NumberUtils.toInt(request.getParameter("dosed"));//0未服1已服
			long date=NumberUtils.toLong(request.getParameter("date"));
			if(date==0){
				date=System.currentTimeMillis();
			}
			BloodpressureRecord bpInfo=new BloodpressureRecord();
			bpInfo.setUserId(user.getUserId());
			bpInfo.setDbp(dbp);
			bpInfo.setSbp(sbp);
			bpInfo.setHeartrate(heartrate);
			bpInfo.setDate(date);
			bpInfo.setDosed(dosed);
			if(this.bloodpressureService.addRecord(bpInfo)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//患者获取自己血压控制设置
	@ResponseBody
	@RequestMapping(value = "/bp/setting/info", method = RequestMethod.GET)
//	@RequestMapping(value = "/bpsetting.do", method = RequestMethod.GET)
	public ResponseBean getBpSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			BloodpressureConfig bpConfig=this.bloodpressureService.getConfig(user.getUserId());
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
	
	//患者更新自己血压控制设置
	@ResponseBody
	@RequestMapping(value = "/bp/setting/modify", method = RequestMethod.POST)
//	@RequestMapping(value = "/bpsetting.do", method = RequestMethod.POST)
	public ResponseBean modifyBpSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			int dbp1=NumberUtils.toInt(request.getParameter("dbp1"));
			int dbp2=NumberUtils.toInt(request.getParameter("dbp2"));
			int sbp1=NumberUtils.toInt(request.getParameter("sbp1"));
			int sbp2=NumberUtils.toInt(request.getParameter("sbp2"));
			int heartrate1=NumberUtils.toInt(request.getParameter("heartrate1"));
			int heartrate2=NumberUtils.toInt(request.getParameter("heartrate2"));
			BloodpressureConfig bpConfig=new BloodpressureConfig();
			bpConfig.setUserId(user.getUserId());
			bpConfig.setDbp1(dbp1);
			bpConfig.setDbp2(dbp2);
			bpConfig.setSbp1(sbp1);
			bpConfig.setSbp2(sbp2);
			bpConfig.setHeartrate1(heartrate1);
			bpConfig.setHeartrate2(heartrate2);
			if(this.bloodpressureService.modifyConfig(bpConfig)){
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
