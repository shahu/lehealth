package com.lehealth.pay.service;

import java.util.Map;

import com.lehealth.pay.entity.WeixinOrder;

public interface WeixinPayService {

	// 创建我们的订单
	public WeixinOrder buildOrder(String userId, String openId,String ip, int goodsId);
	
	// 获取open id
	public String getOpenId(String code);
	
	// 调用微信接口下单
	public Map<String, String> prePayOrder(WeixinOrder order, long timestamp);
	
	// 查询订单信息
	public WeixinOrder getOrderInfo(String orderId);
	
	// 订单付费
	public String payOrder(Map<String, String> requestMap);

	// 关闭订单
	public String closeOrder(String orderId);
	
}
