package com.lehealth.common.dao;

import java.util.Map;

public interface SystemVariableDao {
	
	//获取用户基本信息
	public Map<String, String> selectSystemVariables();
	
}
