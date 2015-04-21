package com.lehealth.pay.dao;

import com.lehealth.pay.entity.WeixinOrder;

public interface WeixinPayDao {
	
	// 生成新的订单
	public void insertNewOrder(WeixinOrder order);
	
	// 修改订单状态
	// 订单预付
	public int updateOrderStatus2PrePay(String orderId,String prePayId);
	
	// 修改订单状态
	// 订单支付
	public int updateOrderStatus2Pay(String orderId,String weixinOrderId);
	
	// 修改订单状态
	// 订单关闭
	public int updateOrderStatus2Close(String orderId);
	
	// 修改订单信息
	public int updateOrderInfo(String orderId, String weixinOrderId, int status,int toStatus);
}
