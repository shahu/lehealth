package com.lehealth.pay.web;

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
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.SystemVariableKeyType;
import com.lehealth.pay.service.WeixinPayService;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.MapResponse;

@Controller
@RequestMapping("/weixin")
public class WeixinPayController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("commonCacheService")
	private CommonCacheService commonCacheService;
	
	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;
	
	@Autowired
	@Qualifier("weixinPayService")
	private WeixinPayService weixinPayService;
	
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
			String jsapi_ticket = this.commonCacheService.getWeixinTicket();
			String noncestr = this.systemVariableService.getValue(SystemVariableKeyType.weixinConfigNoncestr);
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
			map.put("appid", this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID));
			map.put("timestamp", String.valueOf(timestamp));
			map.put("nonceStr", noncestr);
			map.put("signature", signature);
			return new MapResponse(ErrorCodeType.success, map).toJson();
		}
		return new BaseResponse(ErrorCodeType.invalidToken).toJson();
	}
	
	// 调用支付第一步，页面请求生成预付订单
	// request 商品id
	// response appId、timeStamp、nonceStr、package、signType、paySign、订单id
	@ResponseBody
	@RequestMapping(value = "/pre/pay", method = RequestMethod.GET)
	public JSONObject prePay(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			int goodsId = NumberUtils.toInt(request.getParameter("goodsid"));
			long timestamp = NumberUtils.toLong(request.getParameter("timestamp"), System.currentTimeMillis()/1000);
			// 创建我们的订单
			String orderId = this.weixinPayService.buildOrder(user.getUserId(), goodsId);
			// 调用微信接口下单
			String prepayId = this.weixinPayService.prePayOrder();
			
			// 构造返回前端的信息
			if(StringUtils.isNotBlank(prepayId)){
				String appId = this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID);
				String noncestr = this.systemVariableService.getValue(SystemVariableKeyType.weixinPrePayNoncestr);
				String packageStr = "prepay_id=" + prepayId;
				String signtype = "MD5";
				StringBuilder sb = new StringBuilder();
				sb.append("appId=")
					.append(appId)
					.append("&nonceStr=")
					.append(noncestr)
					.append("&package=")
					.append(packageStr)
					.append("&signType=")
					.append(signtype)
					.append("&timeStamp=")
					.append(timestamp);
				String paysign = DigestUtils.md5Hex(sb.toString());
				Map<String, String> map = new HashMap<String, String>();
				map.put("appid", appId);
				map.put("timestamp", String.valueOf(timestamp));
				map.put("noncestr", noncestr);
				map.put("package", packageStr);
				map.put("signtype", signtype);
				map.put("paysign", paysign);
				map.put("orderid", orderId);
				return new MapResponse(ErrorCodeType.success, map).toJson();
			}
			return new BaseResponse(ErrorCodeType.failed).toJson();
		}
		return new BaseResponse(ErrorCodeType.invalidToken).toJson();
	}
	
	// 查询支付结果
	// request 订单id
	// response trade_state_desc
	@ResponseBody
	@RequestMapping(value = "/check/pay", method = RequestMethod.GET)
	public JSONObject checkPay(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return new BaseResponse(ErrorCodeType.success).toJson();
	}
	
	// 微信回调接口
	@ResponseBody
	@RequestMapping(value = "/callback/pay", method = RequestMethod.GET)
	public JSONObject callbackPay(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		return new BaseResponse(ErrorCodeType.success).toJson();
	}
}
