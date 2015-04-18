package com.lehealth.api.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.service.LoginService;
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.response.bean.BaseResponse;

@Controller
@RequestMapping("/api/weixin")
public class WeixinPayController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("commonCacheService")
	private CommonCacheService commonCacheService;
	
	// 刚进页面，请求这个接口获取
	// request timestamp、url
	// response appId、timestamp、nonceStr、signature
	@ResponseBody
	@RequestMapping(value = "/signature", method = RequestMethod.GET)
	public JSONObject signature(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			long timestamp = NumberUtils.toLong(request.getParameter("timestamp"), System.currentTimeMillis()/1000);
			String url = StringUtils.trimToEmpty(request.getParameter("url"));
			String jsapi_ticket=this.commonCacheService.getWeixinTicket();
			String noncestr="aaa";
			StringBuilder sb = new StringBuilder();
			sb.append("jsapi_ticket=")
				.append(jsapi_ticket)
				.append("&noncestr=")
				.append(noncestr)
				.append("&timestamp=")
				.append(timestamp)
				.append("&url=")
				.append(url);
			String signature = DigestUtils.sha1Hex(sb.toString());
			Map<String, String> map = new HashMap<String, String>();
			re
		}
		return wxTicket;
	}
	
	// 调用支付第一步，页面请求生成预付订单
	// request 商品id
	// response appId、timeStamp、nonceStr、package、signType、paySign、订单id
	@ResponseBody
	@RequestMapping(value = "/pre/pay", method = RequestMethod.GET)
	public JSONObject ticket(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		String wxTicket=this.commonCacheService.getWeixinTicket();
		return wxTicket;
	}
	
	// 查询支付结果
	// request 订单id
	// response trade_state_desc
	@ResponseBody
	@RequestMapping(value = "/check/pay", method = RequestMethod.GET)
	public JSONObject newOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return new BaseResponse(ErrorCodeType.success).toJson();
	}
	
	
}
