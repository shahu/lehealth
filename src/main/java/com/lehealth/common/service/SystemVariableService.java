package com.lehealth.common.service;

import com.lehealth.data.type.SystemVariableKeyType;

public interface SystemVariableService {
	
	public String getValue(SystemVariableKeyType key);
	
	public void updateSystemVariable();
}
