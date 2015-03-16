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
import com.lehealth.data.bean.MedicineConfig;
import com.lehealth.data.bean.MedicineRecord;
import com.lehealth.data.bean.ResponseBean;
import com.lehealth.data.bean.UserInfomation;
import com.lehealth.data.type.ErrorCodeType;

@Controller
@RequestMapping("/api/medicine")
public class MedicineController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("medicineService")
	private MedicineService medicineService;
	
	//患者获取自己今日用药记录
	@ResponseBody
	@RequestMapping(value = "/today/list", method = RequestMethod.GET)
	public ResponseBean getMedicineHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			List<MedicineRecord> list=this.medicineService.getTodayRecords(user.getUserId());
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
	@RequestMapping(value = "/record/list", method = RequestMethod.GET)
	public ResponseBean getMedicineRecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			int days=NumberUtils.toInt(request.getParameter("days"),7);
			if(days==0){
				days=7;
			}
			List<MedicineRecord> list=this.medicineService.getHistoryRecords(user.getUserId(),days);
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
	@RequestMapping(value = "/history/add", method = RequestMethod.POST)
	public ResponseBean addMedicineHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			String time=StringUtils.trimToEmpty(request.getParameter("time"));
			float dosage=NumberUtils.toFloat(request.getParameter("dosage"));
			MedicineRecord mInfo=new MedicineRecord();
			mInfo.setUserId(user.getUserId());
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
	@RequestMapping(value = "/setting/list", method = RequestMethod.GET)
	public ResponseBean getMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			List<MedicineConfig> list=this.medicineService.getConfigs(user.getUserId());
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
	@RequestMapping(value = "/setting/modify", method = RequestMethod.POST)
	public ResponseBean modifyMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			long fromTimeStamp=NumberUtils.toLong(request.getParameter("datefrom"));
			String configStr=StringUtils.trimToEmpty(request.getParameter("configs"));
			JSONArray jsonArr=JSONArray.fromObject(configStr);
			MedicineConfig mConfig=new MedicineConfig();
			mConfig.setDateFrom(fromTimeStamp);
			mConfig.setMedicineId(medicineId);
			mConfig.setUserId(user.getUserId());
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
	@RequestMapping(value = "/setting/delete", method = RequestMethod.POST)
	public ResponseBean deleteMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserInfomation user=this.loginService.getUserBaseInfo(loginId, token);
		if(user != null){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			if(this.medicineService.deleteConfig(user.getUserId(),medicineId)){
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
