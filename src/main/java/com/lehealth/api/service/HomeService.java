package com.lehealth.api.service;

import com.lehealth.api.entity.HomeResult;
import com.lehealth.api.entity.UserBaseInfo;

public interface HomeService {
	
	//获取用户血压记录
	public HomeResult getHomeData(String userId, int days);
	
	//获取用户血压记录
	public HomeResult getHomeData(UserBaseInfo user, int days);
	
}
