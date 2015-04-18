package com.lehealth.api.service;

import java.util.List;

import com.lehealth.data.bean.Activity;
import com.lehealth.data.bean.DiseaseCategroy;
import com.lehealth.data.bean.GoodsInfo;
import com.lehealth.data.bean.MedicineCategroy;

public interface CommonService {
	
	//获取活动列表
	public List<Activity> getAtivities();
	
	//获取药物列表
	public List<MedicineCategroy> getMedicines();
	
	//获取疾病列表
	public List<DiseaseCategroy> getDiseases();
	
	// 商品列表
	public List<GoodsInfo> getGoodsInfos();
	
	// 商品详情
	public GoodsInfo getGoodsInfo(int goodsId);
}
