package com.lehealth.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

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
import com.lehealth.bean.DiseaseHistory;
import com.lehealth.bean.ResponseBean;
import com.lehealth.type.ErrorCodeType;

@Controller
@RequestMapping("/api")
public class DiseaseController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("diseaseService")
	private DiseaseService diseaseService;
	
	//患者获取自己病例
	@ResponseBody
	@RequestMapping(value = "/disease/history/list", method = RequestMethod.GET)
//	@RequestMapping(value = "/diseasehistorys.do", method = RequestMethod.GET)
	public ResponseBean getDiseaseHistorys(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			List<DiseaseHistory> list=this.diseaseService.getDiseaseHistorys(userId);
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
	
	//患者获取自己疾病内容
	@ResponseBody
	@RequestMapping(value = "/disease/history/info", method = RequestMethod.GET)
//	@RequestMapping(value = "/diseasehistory.do", method = RequestMethod.GET)
	public ResponseBean getDiseaseHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int diseaseId=NumberUtils.toInt(request.getParameter("diseaseid"));
			DiseaseHistory history=this.diseaseService.getDiseaseHistory(userId,diseaseId);
			if(StringUtils.isNotBlank(history.getUserId())){
				responseBody.setResult(history.toJsonObj());
			}else{
				responseBody.setType(ErrorCodeType.abnormal);
			}
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	//患者添加自己的新病例
	@ResponseBody
	@RequestMapping(value = "/disease/history/add", method = RequestMethod.POST)
//	@RequestMapping(value = "/diseasehistory.do", method = RequestMethod.POST)
	public ResponseBean addDiseaseHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		String userId=this.loginService.checkUser4Token(loginId, token);
		if(StringUtils.isNotBlank(userId)){
			int diseaseId=NumberUtils.toInt(request.getParameter("diseaseid"));
			String diseaseDescription=StringUtils.trimToEmpty(request.getParameter("diseasedescription"));
			String medicinedescription=StringUtils.trimToEmpty(request.getParameter("medicinedescription"));
			DiseaseHistory info=new DiseaseHistory();
			info.setUserId(userId);
			info.setDiseaseId(diseaseId);
			info.setDiseaseDescription(diseaseDescription);
			info.setMedicineDescription(medicinedescription);
			if(this.diseaseService.modifyDiseaseHistory(info)){
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
