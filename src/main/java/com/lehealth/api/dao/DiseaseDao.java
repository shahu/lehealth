package com.lehealth.api.dao;

import java.util.List;

import com.lehealth.api.entity.DiseaseHistory;

public interface DiseaseDao {
	
	//获取病例
	public List<DiseaseHistory> selectHistorys(String userId);
	
	//获取病例
	public DiseaseHistory selectHistory(String userId,int disease);
	
	//更新病例
	public boolean updateHistory(DiseaseHistory diseaseHistory);
}
