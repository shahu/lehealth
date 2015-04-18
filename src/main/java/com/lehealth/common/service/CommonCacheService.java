package com.lehealth.common.service;

import java.util.List;

import com.lehealth.data.bean.GoodsInfo;

public interface CommonCacheService {
	
	public void updateCommonCache30();
	
	public String getWeixinTicket();
	public String getWeixinToken();
	
	public GoodsInfo getGoodsInfo(int goodsId);
	public List<GoodsInfo> getGoodsInfos();
}
