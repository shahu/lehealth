package com.lehealth.api.web;

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
	
	private static Logger logger = Logger.getLogger(DiseaseController.class);
	
	//获取病例
	@ResponseBody
	@RequestMapping(value = "/diseasehistorys.do", method = RequestMethod.GET)
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
