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
	// 订单支付
	public int updateStatus2Pay(String orderId);
	// 订单完成
	public int updateStatus2Success(String orderId, String weixinOrderId);
	// 订单异常关闭
	public int updateStatus2Error(String orderId);
	// 订单关闭
	public int updateStatus2Close(String orderId);
	
}
