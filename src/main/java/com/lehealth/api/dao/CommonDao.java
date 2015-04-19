package com.lehealth.api.dao;

import java.util.List;
import java.util.Map;

import com.lehealth.api.entity.Activity;
import com.lehealth.api.entity.DiseaseCategroy;
import com.lehealth.api.entity.GoodsInfo;
import com.lehealth.api.entity.MedicineCategroy;

public interface CommonDao {
	
	//获取活动列表
	public List<Activity> selectAtivities();
	
	//获取药物列表
	public List<MedicineCategroy> selectMedicines();
	
	//获取疾病列表
	public List<DiseaseCategroy> selectDiseases();
	
	// 商品列表
	public Map<Integer, GoodsInfo> selectGoodsInfos();
	
}
