package com.lehealth.pay.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.api.service.LoginService;
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.common.util.Ipv4Utils;
import com.lehealth.common.util.WeixinPayUtils;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.SystemVariableKeyType;
import com.lehealth.pay.entity.WeixinOrder;
import com.lehealth.pay.service.WeixinPayService;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.JsonArrayResponse;
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
			map.put("jsapi_ticket", jsapi_ticket);
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
			String code = StringUtils.trimToEmpty(request.getParameter("code"));
			if(goodsId != 0 && StringUtils.isNotBlank(code)){
				String ip = Ipv4Utils.getIp(request);
				long timestamp = NumberUtils.toLong(request.getParameter("timestamp"), System.currentTimeMillis()/1000);
				// 获取openid
				String openId = this.weixinPayService.getOpenId(code);
				if(StringUtils.isNotBlank(openId)){
					// 创建我们的订单
					WeixinOrder order = this.weixinPayService.buildOrder(user.getUserId(), openId, ip, goodsId);
					if(order != null){
						// 调用微信接口下单
						Map<String, String> map = this.weixinPayService.prePayOrder(order, timestamp);
						if(map != null){
							ErrorCodeType type = map.containsKey("error")? ErrorCodeType.weixinError : ErrorCodeType.success;
							// 构造返回前端的信息
							return new MapResponse(type, map).toJson();
						}
					}
				}
				return new BaseResponse(ErrorCodeType.weixinError).toJson();
			}
			return new BaseResponse(ErrorCodeType.failed).toJson();
		}
		return new BaseResponse(ErrorCodeType.invalidToken).toJson();
	}
	
	// 微信回调接口
	@ResponseBody
	@RequestMapping(value = "/callback/pay")
	public String callbackPay(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String requestBody = null;
		try {
			requestBody = IOUtils.toString(request.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, String> result = new HashMap<String, String>();
		if(StringUtils.isNotBlank(requestBody)){
			Map<String, String> map = WeixinPayUtils.transf2Xml(requestBody);
			if(map != null && !map.isEmpty()){
				String message = this.weixinPayService.payOrder(map);
				if(StringUtils.isBlank(message)){
					result.put("return_code", "SUCCESS");
					result.put("return_msg", "OK");
				}else{
					result.put("return_code", "FAIL");
					result.put("return_msg", "message");
				}
			}else{
				result.put("return_code", "FAIL");
				result.put("return_msg", "xml parse failed");
			}
		}else{
			result.put("return_code", "FAIL");
			result.put("return_msg", "request is null");
		}
		return WeixinPayUtils.transf2String(result); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/order/list")
	public JSONObject checkPay(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginId=StringUtils.trimToEmpty(request.getParameter("loginid"));
		String token=StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user=this.loginService.getUserByToken(loginId, token);
		if(user != null){
			List<WeixinOrder> list=this.weixinPayService.getOrderList(user.getUserId());
			JSONArray arr=new JSONArray();
			for(WeixinOrder order:list){
				arr.add(order.toJsonObj());
			}
			return new JsonArrayResponse(ErrorCodeType.success, arr).toJson();
		}
		return new BaseResponse(ErrorCodeType.invalidToken).toJson();
	}
	
	// 关闭建单超过1天的数据
	@Scheduled(cron = "5 1 * * * ?")
	public void cleanOrder(){
		this.weixinPayService.cleanOrders();
	}
	
}
