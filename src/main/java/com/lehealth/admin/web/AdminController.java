package com.lehealth.admin.web;

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
import com.lehealth.api.service.HomeService;
import com.lehealth.api.service.LoginService;
import com.lehealth.api.service.PanientService;
import com.lehealth.data.bean.DiseaseHistory;
import com.lehealth.data.bean.HomeResult;
import com.lehealth.data.bean.PanientInfo;
import com.lehealth.data.bean.ResponseBean;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("panientService")
	private PanientService panientService;
	
	@Autowired
	@Qualifier("homeService")
	private HomeService homeService;
	
	@Autowired
	@Qualifier("diseaseService")
	private DiseaseService diseaseService;
	
	// 医生获取关注的病人列表
	@ResponseBody
	@RequestMapping(value = "/patient/list", method = RequestMethod.GET)
	public ResponseBean getPatients(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			List<PanientInfo> list=this.panientService.getPanientListByDoctor(user.getUserId());
			JSONArray arr=new JSONArray();
			for(PanientInfo p:list){
				arr.add(p.toBaseJsonObj());
			}
			responseBody.setResult(arr);
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	// 医生获取病人信息
	@ResponseBody
	@RequestMapping(value = "/patient/info", method = RequestMethod.GET)
	public ResponseBean getPatientInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			String pid=StringUtils.trimToEmpty(request.getParameter("pid"));
			PanientInfo p=this.panientService.getPanient(pid);
			responseBody.setResult(p.toJsonObj());
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	// 医生获取病人血压和用药记录
	@ResponseBody
	@RequestMapping(value = "/patient/record/list", method = RequestMethod.GET)
	public ResponseBean getPatientBpRecords(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token = StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			String pid = StringUtils.trimToEmpty(request.getParameter("pid"));
			int days = NumberUtils.toInt(request.getParameter("days"),7);
			if(days <= 0){
				days = 7;
			}
			HomeResult result = this.homeService.getHomeData(pid, days);
			responseBody.setResult(result.toJsonObj());
		}else{
			responseBody.setType(ErrorCodeType.invalidToken);
		}
		return responseBody;
	}
	
	// 医生获取病史
	@ResponseBody
	@RequestMapping(value = "/patient/disease/list", method = RequestMethod.GET)
	public ResponseBean getPatientDiseases(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		ResponseBean responseBody=new ResponseBean();
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			String pid=StringUtils.trimToEmpty(request.getParameter("pid"));
			List<DiseaseHistory> list=this.diseaseService.getHistoryList(pid);
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
	
}
