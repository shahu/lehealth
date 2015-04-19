package com.lehealth.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.common.service.CommonCacheService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.pay.dao.WeixinPayDao;
import com.lehealth.pay.service.WeixinPayService;

@Service("weixinPayService")
public class WeixinPayServiceImpl implements WeixinPayService{
	
	@Autowired
	@Qualifier("weixinPayDao")
	private WeixinPayDao weixinPayDao;
	
	@Autowired
	@Qualifier("commonCacheService")
	private CommonCacheService commonCacheService;
	
	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;

	@Override
	public String buildOrder(String userId, int goodsId) {
		
		return null;
	}

	@Override
	public String prePayOrder() {
		
		return null;
	}

	@Override
	public void getOrderInfo(String orderId) {
		
	}

	@Override
	public void updateOrderInfo(String orderId) {
		
	}
	
}
