package com.lehealth.pay.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.entity.GoodsInfo;
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.common.util.Constant;
import com.lehealth.common.util.HttpUtils;
import com.lehealth.common.util.OrderUtils;
import com.lehealth.common.util.WeixinPayUtils;
import com.lehealth.data.type.SystemVariableKeyType;
import com.lehealth.pay.dao.WeixinPayDao;
import com.lehealth.pay.entity.WeixinOrder;
import com.lehealth.pay.service.WeixinPayService;

@Service("weixinPayService")
public class WeixinPayServiceImpl implements WeixinPayService{
	
	@Autowired
	@Qualifier("weixinPayDao")
	private WeixinPayDao weixinPayDao;
	
	@Autowired
	@Qualifier("commonCacheService")
	private CommonCacheService commonCacheService;
	
	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;

	private static Logger logger = Logger.getLogger(WeixinPayServiceImpl.class);
	
	@Override
	public String getOpenId(String code){
		StringBuilder sb = new StringBuilder();
		sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
			.append(this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID))
			.append("&secret=")
			.append(this.systemVariableService.getValue(SystemVariableKeyType.weixinAppSecret))
			.append("&code=")
			.append(code)
			.append("&grant_type=authorization_code");
		String response = HttpUtils.getGetResponse(sb.toString());
		if(StringUtils.isNotBlank(response)){
			JSONObject result = null;
			try{
				result = JSONObject.fromObject(response);
			}catch(JSONException e){
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			if(result != null){
				if(result.containsKey("openid")){
					return StringUtils.trimToEmpty(result.getString("openid"));
				}else if(result.containsKey("errcode") && result.containsKey("errmsg")){
					logger.info("weixin get openid errcode=" + result.getString("errcode") + ",errmsg=" + result.getString("errmsg"));
				}
			}else{
				logger.info("weixin get openid parse json error");
			}
		}
		return "";
	}
	
	@Override
	public WeixinOrder buildOrder(String userId, String openId, String ip, int goodsId) {
		GoodsInfo goodsInfo = this.commonCacheService.getGoodsInfo(goodsId);
		if(goodsInfo != null){
			// 生成订单id
			String orderId = OrderUtils.buiildOrderId();
			// 生成订单obj
			WeixinOrder order = new WeixinOrder();
			order.setOrderId(orderId);
			order.setUserId(userId);
			order.setOpenId(openId);
			order.setIp(ip);
			order.setGoodsInfo(goodsInfo);
			// 记录数据库
			this.weixinPayDao.insert(order);
			return order;
		}
		return null;
	}

	
	private static final String successFlag = "SUCCESS";
	
	@Override
	public Map<String, String> prePayOrder(WeixinOrder order, long timestamp) {
		// 整理请求参数
		Map<String, String> requestMap = new LinkedHashMap<String, String>();
		requestMap.put("appid", this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID));
		requestMap.put("attach", "test");
		requestMap.put("body", order.getGoodsInfo().getInfo());
		requestMap.put("mch_id", this.systemVariableService.getValue(SystemVariableKeyType.weixinMchId));
		requestMap.put("nonce_str", this.systemVariableService.getValue(SystemVariableKeyType.weixinPrePayNoncestr));
		requestMap.put("notify_url", Constant.weixinNotifyUrl);
		requestMap.put("openid", order.getOpenId());
		requestMap.put("out_trade_no", order.getOrderId());
		requestMap.put("spbill_create_ip", order.getIp());
		requestMap.put("total_fee", String.valueOf(order.getGoodsInfo().getFee()));
		requestMap.put("trade_type", Constant.weixinTradeType);
		requestMap.put("sign", WeixinPayUtils.getSign(requestMap, this.systemVariableService.getValue(SystemVariableKeyType.weixinAppSecret), false));
		
    	String requestBody = WeixinPayUtils.transf2String(requestMap);
    	System.out.println(requestBody);
        if(StringUtils.isNotBlank(requestBody)){
        	// 发送请求
            String responseBody = HttpUtils.getPostResponse(Constant.weixinPrePayApi, requestBody);
            System.out.println(responseBody);
            // 解析返回
            if(StringUtils.isNotBlank(responseBody)){
            	Map<String, String> resultMap = WeixinPayUtils.transf2Xml(responseBody);
            	if(resultMap != null && !resultMap.isEmpty()){
            		// 通信标识
                	String returnCode = resultMap.get("return_code");
                	if(successFlag.equals(returnCode)){
                		// 业务标识
                		String resultCode = resultMap.get("result_code");
                		if(successFlag.equals(resultCode)){
                			String prePayId = resultMap.get("prepay_id");
                			if(StringUtils.isNotBlank(prePayId)){
                				String appId = this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID);
            					String noncestr = this.systemVariableService.getValue(SystemVariableKeyType.weixinPrePayNoncestr);
            					String packageStr = "prepay_id=" + prePayId;
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
            					Map<String, String> responseMap = new LinkedHashMap<String, String>();
            					responseMap.put("appid", appId);
            					responseMap.put("timestamp", String.valueOf(timestamp));
            					responseMap.put("noncestr", noncestr);
            					responseMap.put("package", packageStr);
            					responseMap.put("signtype", signtype);
            					responseMap.put("paysign", paysign);
            					responseMap.put("orderid", order.getOrderId());
            					//更新订单状态，预付成功
            					int result = this.weixinPayDao.updateStatus2PrePay(order.getOrderId(), prePayId);
            					if(result == 1){
            						return responseMap;
            					}else{
            						logger.info("update prepay order failed");
            						// 关闭订单
            						closeOrder(order.getOrderId());
            					}
                			}else{
                				logger.info("weixin prepay api response prepay_id is empty");
                			}
                		}else{
                			logger.info("weixin prepay api response result_code=" + resultCode + ",code=" + resultMap.get("err_code") + ",message=" + resultMap.get("err_code_des"));
                			Map<String, String> responseMap = new HashMap<String, String>();
                			responseMap.put("message", resultMap.get("err_code_des"));
                			responseMap.put("error", resultMap.get("err_code"));
                			return responseMap;
                		}
                	}else{
                		logger.info("weixin prepay api response return_code=" + returnCode + ",message=" + resultMap.get("return_msg"));
                	}
            	}else{
            		logger.info("weixin prepay api response tranf2map failed");
            	}
            }else{
            	logger.info("weixin prepay api response is empty");
            }
		}else{
			logger.info("weixin prepay api request is empty");
		}
		return null;
	}

	@Override
	public String payOrder(Map<String, String> requestMap) {
		//检查参数
		String sign = WeixinPayUtils.getSign(requestMap, this.systemVariableService.getValue(SystemVariableKeyType.weixinAppSecret), true);
		if(!sign.equals(requestMap.get("sign"))){
			return "签名不正确";
		}
		String orderId = requestMap.get("out_trade_no");
		if(StringUtils.isBlank(orderId)){
			return "订单号为空";
		}
		WeixinOrder order = this.weixinPayDao.selectInfo(orderId);
		if(checkPayInfo(order, requestMap)){
			return "订单信息异常";
		}
		//更新数据库
		int result = this.weixinPayDao.updateStatus2Pay(orderId, requestMap.get("transaction_id"));
		if(result == 1){
			return "";
		}else{
			return "该订单不是预付订单";
		}
	}

	private boolean checkPayInfo(WeixinOrder order, Map<String, String> requestMap){
		
		return true;
	}
	
	@Override
	public String closeOrder(String orderId) {
		int result = this.weixinPayDao.updateStatus2Close(orderId);
		return null;
	}
	
	@Override
	public WeixinOrder getOrderInfo(String orderId) {
		// 请求获取微信订单信息
		
		// 获取订单信息
		WeixinOrder order = this.weixinPayDao.selectInfo(orderId);
		
		// 检查差异
		
		// 状态变化修改数据库
		
		return null;
	}
}
