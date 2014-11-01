package com.lehealth.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.ResponseBean;
import com.lehealth.service.LoginService;
import com.lehealth.service.SettingsService;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class SettingsController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("settingsService")
	private SettingsService settingsService;
	
	private static Logger logger = Logger.getLogger(SettingsController.class);
	
	//获取血压控制设置
	@ResponseBody
	@RequestMapping(value = "/bpsetting.do", method = RequestMethod.GET)
	public ResponseBean getBpSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			BloodpressureConfig bpConfig=this.settingsService.getBloodpressureSetting(userId);
			if(StringUtils.isBlank(bpConfig.getUserid())){
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
			bpConfig.setUserid(userId);
			bpConfig.setDbp1(dbp1);
			bpConfig.setDbp2(dbp2);
			bpConfig.setSbp1(sbp1);
			bpConfig.setSbp2(sbp2);
			bpConfig.setHeartrate1(heartrate1);
			bpConfig.setHeartrate2(heartrate2);
			if(this.settingsService.modifyBloodpressureSetting(bpConfig)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//获取用药设置
	@ResponseBody
	@RequestMapping(value = "/medicinesetting.do", method = RequestMethod.GET)
	public ResponseBean getMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<MedicineConfig> list=this.settingsService.getMedicineSettings(userId);
			JSONArray arr=new JSONArray();
			for(MedicineConfig mc:list){
				arr.add(mc.toJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//更新用药设置
	@ResponseBody
	@RequestMapping(value = "/medicinesetting.do", method = RequestMethod.POST)
	public ResponseBean modifyMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			int amount=NumberUtils.toInt(request.getParameter("amount"));
			int frequency=NumberUtils.toInt(request.getParameter("frequency"));
			int timing=NumberUtils.toInt(request.getParameter("timing"));
			long fromTimeStamp=NumberUtils.toLong(request.getParameter("datefrom"));
			long toTimeStamp=NumberUtils.toLong(request.getParameter("dateto"));
			MedicineConfig mConfig=new MedicineConfig();
			mConfig.setAmount(amount);
			mConfig.setDatefrom(fromTimeStamp);
			mConfig.setDateto(toTimeStamp);
			mConfig.setFrequency(frequency);
			mConfig.setMedicineid(medicineId);
			mConfig.setTiming(timing);
			mConfig.setUserid(userId);
			if(this.settingsService.modifyMedicineSetting(mConfig)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		
		return responseBody;
	}
	
	//删除用药设置
	@ResponseBody
	@RequestMapping(value = "/medicinesettingdel.do", method = RequestMethod.POST)
	public ResponseBean medicinesettingdel(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			if(this.settingsService.delMedicineSetting(userId,medicineId)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
			
	//TODO 获取个人信息
	
	//TODO 更新个人信息
	
}
