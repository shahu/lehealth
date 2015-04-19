package com.lehealth.pay.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.entity.GoodsInfo;
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.common.util.Constant;
import com.lehealth.common.util.HttpUtils;
import com.lehealth.common.util.OrderUtils;
import com.lehealth.common.util.WeixinPayUtils;
import com.lehealth.data.type.SystemVariableKeyType;
import com.lehealth.pay.dao.WeixinPayDao;
import com.lehealth.pay.entity.WeixinOrder;
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
	public WeixinOrder buildOrder(String userId, String openId, String ip, int goodsId) {
		GoodsInfo goodsInfo = this.commonCacheService.getGoodsInfo(goodsId);
		if(goodsInfo != null){
			// 生成订单id
			String orderId = OrderUtils.buiildOrderId();
			// 生成订单obj
			WeixinOrder order = new WeixinOrder();
			order.setOrderId(orderId);
			order.setUserId(userId);
			order.setOpenId(openId);
			order.setIp(ip);
			order.setGoodsInfo(goodsInfo);
			// 记录数据库
			this.weixinPayDao.insertNewOrder(order);
			return order;
		}
		return null;
	}

	@Override
	public String prePayOrder(WeixinOrder order) {
		// 整理请求参数
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("appid", this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID));
		map.put("attach", "test");
		map.put("body", order.getGoodsInfo().getInfo());
		map.put("mch_id", this.systemVariableService.getValue(SystemVariableKeyType.weixinMchId));
		map.put("nonce_str", this.systemVariableService.getValue(SystemVariableKeyType.weixinPrePayNoncestr));
		map.put("notify_url", Constant.weixinNotifyUrl);
		map.put("openid", order.getOpenId());
		map.put("out_trade_no", order.getOrderId());
		map.put("spbill_create_ip", order.getIp());
		map.put("total_fee", String.valueOf(order.getGoodsInfo().getFee()));
		map.put("trade_type", Constant.weixinTradeType);
		map.put("sign", WeixinPayUtils.getSign(map, this.systemVariableService.getValue(SystemVariableKeyType.weixinAppSecret)));
		
    	String requestBody = WeixinPayUtils.transf2String(map);
    	System.out.println(requestBody);
        if(StringUtils.isNotBlank(requestBody)){
        	// 发送请求
            String responseBody = HttpUtils.getPostResponse(Constant.weixinPrePayApi, requestBody);
            System.out.println(responseBody);
            // 解析返回
            if(StringUtils.isNotBlank(responseBody)){
                return WeixinPayUtils.findKeyInXml(responseBody, "prepay_id");
            }
		}
		return null;
	}

	@Override
	public void getOrderInfo(String orderId) {
		
	}

	@Override
	public void payOrder(String orderId) {
		
	}
	
}
