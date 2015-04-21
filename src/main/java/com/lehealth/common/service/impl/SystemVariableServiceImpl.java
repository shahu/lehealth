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
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.data.type.SystemVariableKeyType;

@Service("systemVariableService")
public class SystemVariableServiceImpl implements SystemVariableService,InitializingBean{

	@Autowired
	@Qualifier("systemVariableDao")
	private SystemVariableDao systemVariableDao;
	
	private static Logger logger = Logger.getLogger(SystemVariableServiceImpl.class);
	
	private Map<SystemVariableKeyType, String> map = new ConcurrentHashMap<SystemVariableKeyType, String>();
	
	@Override
	public String getValue(SystemVariableKeyType key){
		return StringUtils.trimToEmpty(map.get(key));
	}
	
	@Override
	public void updateSystemVariable(){
		logger.info("update system variable begin");
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
		logger.info("update system variable end");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		updateSystemVariable();
	}

	@Override
	public JSONArray getCache() {
		JSONArray arr = new JSONArray();
		if(!this.map.isEmpty()){
			arr.addAll(this.map.entrySet());
		}
		return arr;
	}
	
}
