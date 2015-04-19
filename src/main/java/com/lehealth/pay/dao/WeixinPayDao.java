package com.lehealth.pay.dao;

import com.lehealth.pay.entity.WeixinOrder;

public interface WeixinPayDao {
	
	// 生成新的订单
	public void insertNewOrder(WeixinOrder order);
	
}
