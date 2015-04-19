package com.lehealth.pay.service;

import com.lehealth.pay.entity.WeixinOrder;

public interface WeixinPayService {

	// 创建我们的订单
	public WeixinOrder buildOrder(String userId, String openId,String ip, int goodsId);
	
	// 调用微信接口下单
	public String prePayOrder(WeixinOrder order);
	
	// 查询订单信息
	public void getOrderInfo(String orderId);
	
	// 订单付费
	public void payOrder(String orderId);
	
}
