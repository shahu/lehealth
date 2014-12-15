package com.lehealth.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.api.service.MedicineService;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineRecord;
import com.lehealth.bean.ResponseBean;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class MedicineController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("medicineService")
	private MedicineService medicineService;
	
	//患者获取自己今日用药记录
	@ResponseBody
	@RequestMapping(value = "/medicine/today/list", method = RequestMethod.GET)
//	@RequestMapping(value = "/medicinehistory.do", method = RequestMethod.GET)
	public ResponseBean getMedicineHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<MedicineRecord> list=this.medicineService.getTodayRecords(userId);
			JSONArray arr=new JSONArray();
			for(MedicineRecord info:list){
				arr.add(info.toJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//患者获取自己过去一段时间用药记录
	@ResponseBody
	@RequestMapping(value = "/medicine/record/list", method = RequestMethod.GET)
//	@RequestMapping(value = "/medicinerecords.do", method = RequestMethod.GET)
	public ResponseBean getMedicineRecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int days=NumberUtils.toInt(request.getParameter("days"),7);
			if(days==0){
				days=7;
			}
			List<MedicineRecord> list=this.medicineService.getRecords(userId,days);
			JSONArray arr=new JSONArray();
			for(MedicineRecord info:list){
				arr.add(info.toJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//患者更新自己今日用药记录
	@ResponseBody
	@RequestMapping(value = "/medicine/history/add", method = RequestMethod.POST)
//	@RequestMapping(value = "/medicinehistory.do", method = RequestMethod.POST)
	public ResponseBean addMedicineHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			String time=StringUtils.trimToEmpty(request.getParameter("time"));
			float dosage=NumberUtils.toFloat(request.getParameter("dosage"));
			MedicineRecord mInfo=new MedicineRecord();
			mInfo.setUserId(userId);
			mInfo.setMedicineId(medicineId);
			mInfo.addSituation(time, dosage);
			if(this.medicineService.addRecord(mInfo)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
		
	//患者获取自己用药设置
	@ResponseBody
	@RequestMapping(value = "/medicine/setting/info", method = RequestMethod.GET)
//	@RequestMapping(value = "/medicinesetting.do", method = RequestMethod.GET)
	public ResponseBean getMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<MedicineConfig> list=this.medicineService.getConfigs(userId);
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
	
	//患者更新自己用药设置
	@ResponseBody
	@RequestMapping(value = "/medicine/setting/modify", method = RequestMethod.POST)
//	@RequestMapping(value = "/medicinesetting.do", method = RequestMethod.POST)
	public ResponseBean modifyMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			long fromTimeStamp=NumberUtils.toLong(request.getParameter("datefrom"));
			String configStr=StringUtils.trimToEmpty(request.getParameter("configs"));
			JSONArray jsonArr=JSONArray.fromObject(configStr);
			MedicineConfig mConfig=new MedicineConfig();
			mConfig.setDateFrom(fromTimeStamp);
			mConfig.setMedicineId(medicineId);
			mConfig.setUserId(userId);
			for(int i=0;i<jsonArr.size();i++){
				JSONObject jsonObj=jsonArr.getJSONObject(i);
				String time=StringUtils.trimToEmpty(jsonObj.getString("time"));
				float dosage=NumberUtils.toFloat(jsonObj.getString("dosage"));
				mConfig.addConfig(time, dosage);
			}
			if(this.medicineService.modifyConfig(mConfig)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		
		return responseBody;
	}
	
	//患者除用自己药设置
	@ResponseBody
	@RequestMapping(value = "/medicine/setting/delete", method = RequestMethod.POST)
//	@RequestMapping(value = "/medicinesettingdel.do", method = RequestMethod.POST)
	public ResponseBean deleteMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			if(this.medicineService.deleteConfig(userId,medicineId)){
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
