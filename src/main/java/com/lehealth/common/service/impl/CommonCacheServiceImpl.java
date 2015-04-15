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
import com.lehealth.data.type.SystemVariableKeyType;

@Service("commonCacheService")
public class CommonCacheServiceImpl implements CommonCacheService,InitializingBean{

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
		Map<String, String> temp = this.systemVariableDao.selectSystemVariables();
		if(temp != null && !temp.isEmpty()){
			for(Entry<String, String> e : temp.entrySet()){
				logger.info("update system variable : key=" + e.getKey() + ";value=" + e.getValue());
				SystemVariableKeyType key = SystemVariableKeyType.getType(e.getKey());
				if(key != SystemVariableKeyType.unknown){
					map.put(key, e.getValue());
				}
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
