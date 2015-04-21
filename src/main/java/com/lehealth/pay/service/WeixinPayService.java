package com.lehealth.pay.service;

import java.util.Map;

import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.pay.entity.WeixinOrder;

public interface WeixinPayService {

	// 创建我们的订单
	public WeixinOrder buildOrder(String userId, String openId,String ip, int goodsId);
	
	// 调用微信接口下单
	public Map<String, String> prePayOrder(WeixinOrder order, long timestamp);
	
	// 查询订单信息
	public WeixinOrder getOrderInfo(String orderId);
	
	// 订单付费
	public ErrorCodeType payOrder(String orderId, String weixinOrderId);

	// 关闭订单
	public ErrorCodeType closeOrder(String orderId, String weixinOrderId);
	
}
