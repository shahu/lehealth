package com.lehealth.common.service;

import net.sf.json.JSONArray;

import com.lehealth.data.type.SystemVariableKeyType;

public interface SystemVariableService {
	
	public String getValue(SystemVariableKeyType key);
	
	public void updateSystemVariable();
	
	public JSONArray getCache();
}
