package com.lehealth.pay.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.entity.GoodsInfo;
import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.common.util.Constant;
import com.lehealth.common.util.HttpUtils;
import com.lehealth.common.util.OrderUtils;
import com.lehealth.common.util.WeixinPayUtils;
import com.lehealth.data.type.SystemVariableKeyType;
import com.lehealth.data.type.WeixinOrderStatusType;
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
			String orderSecret = OrderUtils.buiildOrderSecret(orderId, openId);
			// 生成订单obj
			WeixinOrder order = new WeixinOrder();
			order.setOrderId(orderId);
			order.setOrderSecret(orderSecret);
			order.setUserId(userId);
			order.setOpenId(openId);
			order.setIp(ip);
			order.setGoodsInfo(goodsInfo);
			// 生成时间
			Date date = new Date();
			order.setStartTime(date.getTime());
			order.setExpireTime(DateUtils.addDays(date, 1).getTime());
			// 记录数据库
			this.weixinPayDao.insert(order);
			return order;
		}else{
			logger.info("goods id=" + goodsId +" no data");
		}
		return null;
	}

	
	private static final String successFlag = "SUCCESS";
	
	@Override
	public Map<String, String> prePayOrder(WeixinOrder order, long timestamp) {
		// 整理请求参数
		Map<String, String> requestMap = new LinkedHashMap<String, String>();
		requestMap.put("appid", this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID));
		requestMap.put("attach", order.getOrderSecret());
		requestMap.put("body", order.getGoodsInfo().getInfo());
		requestMap.put("mch_id", this.systemVariableService.getValue(SystemVariableKeyType.weixinMchId));
		requestMap.put("nonce_str", this.systemVariableService.getValue(SystemVariableKeyType.weixinPrePayNoncestr));
		requestMap.put("notify_url", Constant.weixinNotifyUrl);
		requestMap.put("openid", order.getOpenId());
		requestMap.put("out_trade_no", order.getOrderId());
		requestMap.put("spbill_create_ip", order.getIp());
		requestMap.put("time_start", DateFormatUtils.format(order.getStartTime(), Constant.dateFormat_yyyymmddhhmmss));
		requestMap.put("time_expire", DateFormatUtils.format(order.getExpireTime(), Constant.dateFormat_yyyymmddhhmmss));
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
                	if(StringUtils.equals(successFlag, returnCode)){
                		// 业务标识
                		String resultCode = resultMap.get("result_code");
                		if(StringUtils.equals(successFlag, resultCode)){
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
            					this.weixinPayDao.updateStatus2PrePay(order.getOrderId(), prePayId);
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
		// 检查参数
		// 不检查签名
//		String sign = WeixinPayUtils.getSign(requestMap, this.systemVariableService.getValue(SystemVariableKeyType.weixinAppSecret), true);
//		if(!sign.equals(requestMap.get("sign"))){
//			return "签名不正确";
//		}
		String orderId = requestMap.get("out_trade_no");
		if(StringUtils.isBlank(orderId)){
			return "订单号为空";
		}
		
		String resultCode = requestMap.get("result_code");
		if(!StringUtils.equalsIgnoreCase(successFlag, resultCode)){
			this.weixinPayDao.updateStatus2Error(orderId);
			String errCode = requestMap.get("err_code");
			String errCodeDes = requestMap.get("err_code_des");
			logger.info("weixin callback result_code=" + resultCode + ",errCode=" + errCode + ",errCodeDes=" + errCodeDes);
			return "result_code不是SUCCESS";
		}
		
		WeixinOrder order = this.weixinPayDao.selectInfo(orderId);
		String message = checkPayInfo(order, requestMap);
		if(StringUtils.isNotBlank(message)){
			return "订单信息异常:" + message;
		}
		String timeEnd = StringUtils.trimToEmpty(requestMap.get("transaction_id"));
		Date date = new Date();
		try {
			date=DateUtils.parseDate(timeEnd, Constant.dateFormat_yyyymmddhhmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//更新数据库
		int result = this.weixinPayDao.updateStatus2Success(orderId, requestMap.get("transaction_id"), date);
		if(result == 1){
			return "";
		}else{
			return "该订单状态已变更";
		}
	}

	private String checkPayInfo(WeixinOrder order, Map<String, String> requestMap){
		// 检查订单是否存在
		if(order == null || StringUtils.isBlank(order.getOpenId())){
			return "订单不存在";
		}
		// 检查公众号
		String appId = requestMap.get("appid");
		if(!StringUtils.equals(appId, this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID))){
			return "公众号不匹配";
		}
		// 检查商户号
		String mchId = requestMap.get("mch_id");
		if(!StringUtils.equals(mchId, this.systemVariableService.getValue(SystemVariableKeyType.weixinMchId))){
			return "公众号不匹配";
		}
		// 检查状态
		int status = order.getStatus();
		if(status == WeixinOrderStatusType.create.getCode()){
			return "订单为生成预付";
		}else if(status == WeixinOrderStatusType.success.getCode()){
			return "订单已经支付完成";
		}else if(status == WeixinOrderStatusType.error.getCode()){
			return "订单已经异常结束";
		}else if(status == WeixinOrderStatusType.close.getCode()){
			return "订单已经关闭";
		}
		// 检查金额
		double fee = NumberUtils.toDouble(requestMap.get("total_fee"));
		if(order.getFee() > fee || fee < 0){
			return "金额不正确";
		}
		// 检查openid
		String openId = requestMap.get("openid");
		if(!StringUtils.equals(openId, order.getOpenId())){
			return "用户不匹配";
		}
		// 检查密码
		String orderSecret = requestMap.get("attach");
		if(!StringUtils.equals(orderSecret, order.getOrderSecret())){
			return "密钥不匹配";
		}
		return "";
	}
	
	@Override
	public List<WeixinOrder> getOrderList(UserBaseInfo user){
		// 获取订单列表
		List<WeixinOrder> orderList = this.weixinPayDao.selectInfos(user);
		// 用户支付中状态需要查询微信接口
		if(orderList != null && !orderList.isEmpty()){
			for(WeixinOrder order : orderList){
				if(order.getStatus() == WeixinOrderStatusType.prepay.getCode()){
					Map<String, String> requestMap = new LinkedHashMap<String, String>();
					requestMap.put("appid", this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID));
					requestMap.put("mch_id", this.systemVariableService.getValue(SystemVariableKeyType.weixinMchId));
					requestMap.put("nonce_str", this.systemVariableService.getValue(SystemVariableKeyType.weixinCheckOrderNocestr));
					requestMap.put("out_trade_no", order.getOpenId());
					if(StringUtils.isNotBlank(order.getTransactionId())){
						requestMap.put("transaction_id", order.getTransactionId());
					}
					requestMap.put("sign", WeixinPayUtils.getSign(requestMap, this.systemVariableService.getValue(SystemVariableKeyType.weixinAppSecret), false));
					
					String requestBody = WeixinPayUtils.transf2String(requestMap);
			    	System.out.println(requestBody);
			        if(StringUtils.isNotBlank(requestBody)){
			        	// 发送请求
			            String responseBody = HttpUtils.getPostResponse(Constant.weixinSearchApi, requestBody);
			            System.out.println(responseBody);
			            // 解析返回
			            if(StringUtils.isNotBlank(responseBody)){
			            	Map<String, String> resultMap = WeixinPayUtils.transf2Xml(responseBody);
			            	if(resultMap != null && !resultMap.isEmpty()){
			            		// 通信标识
			                	String returnCode = resultMap.get("return_code");
			                	if(StringUtils.equals(successFlag, returnCode)){
			                		// 业务标识
			                		String resultCode = resultMap.get("result_code");
			                		if(StringUtils.equals(successFlag, resultCode)){
			                			// 更新订单状态
			                			String trade_state = resultMap.get("trade_state");
			                			if(StringUtils.equals("SUCCESS", trade_state)){
			                				String transactionId = resultMap.get("transaction_id");
			                				String timeEnd = resultMap.get("time_end");
			                				Date payTime = new Date();
			                				try {
			                					payTime=DateUtils.parseDate(timeEnd, Constant.dateFormat_yyyymmddhhmmss);
			                				} catch (ParseException e) {
			                					e.printStackTrace();
			                				}
			                				this.weixinPayDao.updateStatus2Success(order.getOrderId(), transactionId, payTime);
			                				order.setStatus(WeixinOrderStatusType.success.getCode());
			                			}
			                		}else{
			                			logger.info("weixin search api response result_code=" + resultCode + ",code=" + resultMap.get("err_code") + ",message=" + resultMap.get("err_code_des"));
			                		}
			                	}else{
			                		logger.info("weixin search api response return_code=" + returnCode + ",message=" + resultMap.get("return_msg"));
			                	}
			            	}else{
			            		logger.info("weixin search api response tranf2map failed");
			            	}
			            }else{
			            	logger.info("weixin search api response is empty");
			            }
					}else{
						logger.info("weixin search api request is empty");
					}
				}
			}
		}
		return orderList;
	}
	
	@Override
	public int getOrderStatus(String orderId) {
		WeixinOrder order = this.weixinPayDao.selectInfo(orderId);
		if(order != null){
			return order.getStatus();
		}
		return -1;
	}
}
