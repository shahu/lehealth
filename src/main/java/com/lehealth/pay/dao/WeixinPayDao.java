package com.lehealth.pay.dao;

import java.util.Date;
import java.util.List;

import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.pay.entity.WeixinOrder;

public interface WeixinPayDao {
	
	// 生成新的订单
	public void insert(WeixinOrder order);
	
	// 获取订单信息
	public WeixinOrder selectInfo(String orderId);
	// 获取订单列表
	public List<WeixinOrder> selectInfos(UserBaseInfo user);
	
	// 修改订单状态
	// 订单预付
	public int updateStatus2PrePay(String orderId,String prePayId);
	// 订单完成
	public int updateStatus2Success(String orderId, String weixinOrderId, Date payTime);
	// 订单异常关闭
	public int updateStatus2Error(String orderId, String message);
	// 订单关闭
	public int updateStatus2Close(String orderId);
	
}
