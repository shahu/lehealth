package com.lehealth.api.service;

import com.lehealth.bean.HomeResult;

public interface HomeService {
	
	//获取用户血压记录
	public HomeResult getHomeData(String userId);
	
}