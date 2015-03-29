package com.lehealth.api.service;

import com.lehealth.data.bean.HomeResult;
import com.lehealth.data.bean.UserBaseInfo;

public interface HomeService {
	
	//获取用户血压记录
	public HomeResult getHomeData(String userId, int days);
	
	//获取用户血压记录
	public HomeResult getHomeData(UserBaseInfo user, int days);
	
}
