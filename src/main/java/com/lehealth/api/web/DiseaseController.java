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

import com.lehealth.api.service.DiseaseService;
import com.lehealth.api.service.LoginService;
import com.lehealth.data.bean.DiseaseHistory;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.JsonArrayResponse;
import com.lehealth.response.bean.JsonObjectResponse;

@Controller
@RequestMapping("/api/disease")
public class DiseaseController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("diseaseService")
	private DiseaseService diseaseService;
	
	//患者获取自己病例
	@ResponseBody
	@RequestMapping(value = "/history/list", method = RequestMethod.GET)
	public JSONObject getDiseaseHistorys(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			List<DiseaseHistory> list=this.diseaseService.getHistoryList(user.getUserId());
			JSONArray arr=new JSONArray();
			for(DiseaseHistory d:list){
				arr.add(d.toJsonObj());
			}
			return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
	//患者获取自己疾病内容
	@ResponseBody
	@RequestMapping(value = "/history/info", method = RequestMethod.GET)
	public JSONObject getDiseaseHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			int diseaseId=NumberUtils.toInt(request.getParameter("diseaseid"));
			DiseaseHistory history=this.diseaseService.getHistory(user.getUserId(), diseaseId);
			if(StringUtils.isNotBlank(history.getUserId())){
				return new JsonObjectResponse(ErrorCodeType.success, history.toJsonObj()).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.abnormal).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
	//患者添加自己的新病例
	@ResponseBody
	@RequestMapping(value = "/history/add", method = RequestMethod.POST)
	public JSONObject addDiseaseHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			int diseaseId=NumberUtils.toInt(request.getParameter("diseaseid"));
			String diseaseDescription=StringUtils.trimToEmpty(request.getParameter("diseasedescription"));
			String medicinedescription=StringUtils.trimToEmpty(request.getParameter("medicinedescription"));
			DiseaseHistory info=new DiseaseHistory();
			info.setUserId(user.getUserId());
			info.setDiseaseId(diseaseId);
			info.setDiseaseDescription(diseaseDescription);
			info.setMedicineDescription(medicinedescription);
			if(this.diseaseService.modifyHistory(info)){
				return new BaseResponse(ErrorCodeType.success).toJson();
			}else{
				return new BaseResponse(ErrorCodeType.abnormal).toJson();
			}
		}else{
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}
	
}
