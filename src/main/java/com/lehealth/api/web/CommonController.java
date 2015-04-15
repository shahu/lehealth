package com.lehealth.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.CommonService;
import com.lehealth.api.service.LoginService;
import com.lehealth.data.bean.Activity;
import com.lehealth.data.bean.DiseaseCategroy;
import com.lehealth.data.bean.MedicineCategroy;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.response.bean.JsonArrayResponse;

@Controller
@RequestMapping("/api")
public class CommonController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("commonService")
	private CommonService commonService;
	
	//获取线下活动列表
	@ResponseBody
	@RequestMapping(value = "/activities.do", method = RequestMethod.GET)
	public JSONObject activities(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<Activity> list=this.commonService.getAtivities();
		JSONArray arr=new JSONArray();
		for(Activity a:list){
			arr.add(a.toJsonObj());
		}
		return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
	}
	
	//药物列表
	@ResponseBody
	@RequestMapping(value = "/medicines.do", method = RequestMethod.GET)
	public JSONObject medicines(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<MedicineCategroy> list=this.commonService.getMedicines();
		JSONArray arr=new JSONArray();
		for(MedicineCategroy mc:list){
			arr.add(mc.toJsonObj());
		}
		return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
	}
		
	//疾病列表
	@ResponseBody
	@RequestMapping(value = "/diseases.do", method = RequestMethod.GET)
	public JSONObject diseases(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<DiseaseCategroy> list=this.commonService.getDiseases();
		JSONArray arr=new JSONArray();
		for(DiseaseCategroy mc:list){
			arr.add(mc.toJsonObj());
		}
		return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
	}
	
	//微信ticket
	@ResponseBody
	@RequestMapping(value = "/wxTicket", method = RequestMethod.GET)
	public JSONObject wxTicket(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<DiseaseCategroy> list=this.commonService.getDiseases();
		JSONArray arr=new JSONArray();
		for(DiseaseCategroy mc:list){
			arr.add(mc.toJsonObj());
		}
		return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
	}
}
