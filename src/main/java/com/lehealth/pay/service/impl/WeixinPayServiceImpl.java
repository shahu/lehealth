package com.lehealth.pay.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.LoginDao;
import com.lehealth.api.service.LoginService;
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.common.util.Constant;
import com.lehealth.common.util.TokenUtils;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.UserRoleType;
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
