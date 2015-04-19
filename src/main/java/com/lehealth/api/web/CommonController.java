package com.lehealth.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.entity.Activity;
import com.lehealth.api.entity.DiseaseCategroy;
import com.lehealth.api.entity.GoodsInfo;
import com.lehealth.api.entity.MedicineCategroy;
import com.lehealth.api.service.CommonService;
import com.lehealth.api.service.LoginService;
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.JsonArrayResponse;
import com.lehealth.response.bean.JsonObjectResponse;

@Controller
@RequestMapping("/api")
public class CommonController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("commonService")
	private CommonService commonService;
	
	@Autowired
	@Qualifier("commonCacheService")
	private CommonCacheService commonCacheService;
	
	// 获取线下活动列表
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
	
	// 药物列表
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
		
	// 疾病列表
	@ResponseBody
	@RequestMapping(value = "/diseases.do", method = RequestMethod.GET)
	public JSONObject diseases(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<DiseaseCategroy> list=this.commonService.getDiseases();
		JSONArray arr=new JSONArray();
		for(DiseaseCategroy dc:list){
			arr.add(dc.toJsonObj());
		}
		return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
	}
	
	// 商品列表
	@ResponseBody
	@RequestMapping(value = "/goods/list", method = RequestMethod.GET)
	public JSONObject goodsList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<GoodsInfo> list=this.commonCacheService.getGoodsInfos();
		JSONArray arr=new JSONArray();
		for(GoodsInfo gi:list){
			arr.add(gi.toJsonObj());
		}
		return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
	}
	
	// 商品接口
	@ResponseBody
	@RequestMapping(value = "/goods/detail", method = RequestMethod.GET)
	public JSONObject goodsDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		int goodsId = NumberUtils.toInt(request.getParameter("goodsid"));
		GoodsInfo info = this.commonCacheService.getGoodsInfo(goodsId);
		if(info != null){
			return new JsonObjectResponse(ErrorCodeType.success, info.toJsonObj()).toJson();
		}else{
			return new BaseResponse(ErrorCodeType.noneData).toJson();
		}
	}
}
