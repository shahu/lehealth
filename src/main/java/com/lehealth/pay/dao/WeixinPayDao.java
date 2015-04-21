package com.lehealth.pay.dao;

import com.lehealth.pay.entity.WeixinOrder;

public interface WeixinPayDao {
	
	// 生成新的订单
	public void insert(WeixinOrder order);
	
	// 获取订单信息
	public WeixinOrder selectInfo(String orderId);
	
	// 修改订单状态
	// 订单预付
	public int updateStatus2PrePay(String orderId,String prePayId);
	
	// 修改订单状态
	// 订单支付
	public int updateStatus2Pay(String orderId,String weixinOrderId);
	
	// 修改订单状态
	// 订单关闭
	public int updateStatus2Close(String orderId);
	
	// 修改订单信息
	public int updateInfo(WeixinOrder order);
}
