package com.lehealth.pay.service;

public interface WeixinPayService {

	// 创建我们的订单
	public String buildOrder(String userId, int goodsId);
	
	// 调用微信接口下单
	public String prePayOrder();
	
	// 查询订单信息
	public void getOrderInfo(String orderId);
	
	// 更新订单状态
	public void updateOrderInfo(String orderId);
	
}
