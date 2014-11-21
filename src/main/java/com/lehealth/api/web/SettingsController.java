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
import com.lehealth.api.service.SettingsService;
import com.lehealth.bean.BloodpressureConfig;
import com.lehealth.bean.DiseaseHistory;
import com.lehealth.bean.Doctor;
import com.lehealth.bean.MedicineConfig;
import com.lehealth.bean.ResponseBean;
import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;
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
	public ResponseBean deleteMedicineSetting(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
			
	//获取个人信息
	@ResponseBody
	@RequestMapping(value = "/userinfo.do", method = RequestMethod.GET)
	public ResponseBean getUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			UserInfo info=this.settingsService.getUserInfo(userId);
			if(StringUtils.isNotBlank(info.getUserId())){
				responseBody.setResult(info.toJsonObj());
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//更新个人信息
	@ResponseBody
	@RequestMapping(value = "/userinfo.do", method = RequestMethod.POST)
	public ResponseBean modifyUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			String userName=StringUtils.trimToEmpty(request.getParameter("username"));
			int gender=NumberUtils.toInt(request.getParameter("gender"));
			long birthday=NumberUtils.toInt(request.getParameter("birthday"));;
			float height=NumberUtils.toInt(request.getParameter("height"));;
			float weight=NumberUtils.toInt(request.getParameter("weight"));
			UserInfo info=new UserInfo();
			info.setBirthday(birthday);
			info.setGender(gender);
			info.setHeight(height);
			info.setUserId(userId);
			info.setUserName(userName);
			info.setWeight(weight);
			if(this.settingsService.modifyUserInfo(info)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//获取监护人设置和短信通知
	@ResponseBody
	@RequestMapping(value = "/diseases.do", method = RequestMethod.GET)
	public ResponseBean getGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			UserGuardianInfo info=this.settingsService.getUserGuardianInfo(userId);
			if(StringUtils.isNotBlank(info.getUserId())){
				responseBody.setResult(info.toJsonObj());
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//更新监护人设置和短信通知
	@ResponseBody
	@RequestMapping(value = "/guardianinfo.do", method = RequestMethod.POST)
	public ResponseBean modifyGuardianInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			String guardianName=StringUtils.trimToEmpty(request.getParameter("guardianname"));
			String guardianNumber=StringUtils.trimToEmpty(request.getParameter("guardiannumber"));
			UserGuardianInfo info=new UserGuardianInfo();
			info.setUserId(userId);
			info.setGuardianName(guardianName);
			info.setGuardianNumber(guardianNumber);
			if(this.settingsService.modifyUserGuardianInfo(info)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//获取关注医生列表
	@ResponseBody
	@RequestMapping(value = "/attentiondoctors.do", method = RequestMethod.GET)
	public ResponseBean attentionDoctors(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<Doctor> list=this.settingsService.getAttentionDoctor(userId);
			JSONArray arr=new JSONArray();
			for(Doctor d:list){
				arr.add(d.toJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//添加医生关注或取消关注
	@ResponseBody
	@RequestMapping(value = "/attentiondoctor.do", method = RequestMethod.POST)
	public ResponseBean attentiondoctor(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			String guardianName=StringUtils.trimToEmpty(request.getParameter("guardianname"));
			String guardianNumber=StringUtils.trimToEmpty(request.getParameter("guardiannumber"));
			UserGuardianInfo info=new UserGuardianInfo();
			info.setUserId(userId);
			info.setGuardianName(guardianName);
			info.setGuardianNumber(guardianNumber);
			if(this.settingsService.modifyUserGuardianInfo(info)){
				responseBody.setType(ErrorCodeType.normal);
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//获取病例
	@ResponseBody
	@RequestMapping(value = "/diseasehistorys.do", method = RequestMethod.GET)
	public ResponseBean getDiseaseHistorys(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<DiseaseHistory> list=this.settingsService.getDiseaseHistorys(userId);
			JSONArray arr=new JSONArray();
			for(DiseaseHistory d:list){
				arr.add(d.toJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//添加新病例
	@ResponseBody
	@RequestMapping(value = "/diseasehistory.do", method = RequestMethod.POST)
	public ResponseBean modifyDiseaseHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int diseaseId=NumberUtils.toInt(request.getParameter("diseaseid"));
			String diseaseDescription=StringUtils.trimToEmpty(request.getParameter("ddesc"));
			String medicinedescription=StringUtils.trimToEmpty(request.getParameter("mdesc"));
			DiseaseHistory info=new DiseaseHistory();
			info.setUserId(userId);
			info.setDiseaseId(diseaseId);
			info.setDiseaseDescription(diseaseDescription);
			info.setMedicinedescription(medicinedescription);
			if(this.settingsService.modifyDiseaseHistory(info)){
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
