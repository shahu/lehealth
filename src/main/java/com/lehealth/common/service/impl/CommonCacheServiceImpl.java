package com.lehealth.common.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.common.dao.SystemVariableDao;
import com.lehealth.common.service.CommonCacheService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.common.util.HttpUtils;
import com.lehealth.data.type.SystemVariableKeyType;

@Service("commonCacheService")
public class CommonCacheServiceImpl implements CommonCacheService,InitializingBean{

	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;
	
	private static Logger logger = Logger.getLogger(CommonCacheServiceImpl.class);
	
	private String weixinTicket = "";
	private String weixinToken = "";
	
	@Override
	public String getWeixinTicket() {
		return weixinTicket;
	}
	@Override
	public String getWeixinToken() {
		return weixinToken;
	}
	
	private void updateWeixinCache(){
		logger.info("update weixin token and ticket begin");
		StringBuilder accessTokenApi = new StringBuilder();
		accessTokenApi.append("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid")
			.append(this.systemVariableService.getValue(SystemVariableKeyType.weixinAppID))
			.append("&secret=")
			.append(this.systemVariableService.getValue(SystemVariableKeyType.weixinAppSecret));
		String tempToken = HttpUtils.getGetResponse(accessTokenApi.toString());
		if(StringUtils.isNotBlank(tempToken)){
			this.weixinToken = tempToken;
			StringBuilder jsTicketApi = new StringBuilder();
			jsTicketApi.append("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=")
				.append(tempToken)
				.append("&type=jsapi");
			String tempTicket = HttpUtils.getGetResponse(jsTicketApi.toString());
			if(StringUtils.isNotBlank(tempTicket)){
				this.weixinTicket = tempTicket;
			}
		}
		logger.info("update weixin token and ticket end");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		updateCommonCache30();
	}

	@Override
	public void updateCommonCache30(){
		logger.info("update common cache begin");
		
		updateWeixinCache();
		
		logger.info("update common cache end");
	}

}
