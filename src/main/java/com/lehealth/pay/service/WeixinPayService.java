package com.lehealth.pay.service;

import java.util.List;
import java.util.Map;

import com.lehealth.pay.entity.WeixinOrder;

public interface WeixinPayService {

	// 创建我们的订单
	public WeixinOrder buildOrder(String userId, String openId,String ip, int goodsId);
	
	// 获取open id
	public String getOpenId(String code);
	
	// 调用微信接口下单
	public Map<String, String> prePayOrder(WeixinOrder order, long timestamp);
	
	// 订单付费
	public String payOrder(Map<String, String> requestMap);
	
	// 获取订单列表
	public List<WeixinOrder> getOrderList(String userId);

	// 关闭订单
	public void cleanOrders();
	
}
