package com.lehealth.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.api.service.MedicineService;
import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.MedicineInfo;
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
	
	private static Logger logger = Logger.getLogger(MedicineController.class);
	
	//获取用药信息
	@ResponseBody
	@RequestMapping(value = "/medicinehistory.do", method = RequestMethod.GET)
	public ResponseBean getmedicinehistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<MedicineInfo> list=this.medicineService.getMedicineHistory(userId);
			JSONArray arr=new JSONArray();
			for(MedicineInfo info:list){
				arr.add(info.toJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//更新用药记录
	@ResponseBody
	@RequestMapping(value = "/medicinehistory.do", method = RequestMethod.POST)
	public ResponseBean addmedicinehistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			String time=StringUtils.trimToEmpty(request.getParameter("time"));
			float dosage=NumberUtils.toFloat(request.getParameter("dosage"));
			MedicineInfo mInfo=new MedicineInfo();
			mInfo.setUserid(userId);
			mInfo.setMedicineid(medicineId);
			mInfo.addSituation(time, dosage);
			if(this.medicineService.updateMedicineHistory(mInfo)){
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
			List<MedicineConfig> list=this.medicineService.getMedicineSettings(userId);
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
			long fromTimeStamp=NumberUtils.toLong(request.getParameter("datefrom"));
			long toTimeStamp=NumberUtils.toLong(request.getParameter("dateto"));
			String configStr=StringUtils.trimToEmpty(request.getParameter("configs"));
			JSONArray jsonArr=JSONArray.fromObject(configStr);
			MedicineConfig mConfig=new MedicineConfig();
			mConfig.setDatefrom(fromTimeStamp);
			mConfig.setDateto(toTimeStamp);
			mConfig.setMedicineid(medicineId);
			mConfig.setUserid(userId);
			for(int i=0;i<jsonArr.size();i++){
				JSONObject jsonObj=jsonArr.getJSONObject(i);
				String time=StringUtils.trimToEmpty(jsonObj.getString("time"));
				float dosage=NumberUtils.toFloat(jsonObj.getString("dosage"));
				mConfig.addConfig(time, dosage);
			}
			if(this.medicineService.modifyMedicineSetting(mConfig)){
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
	public ResponseBean deleteMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int medicineId=NumberUtils.toInt(request.getParameter("medicineid"));
			if(this.medicineService.delMedicineSetting(userId,medicineId)){
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
