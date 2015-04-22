package com.lehealth.api.service;

import java.util.List;

import com.lehealth.api.entity.DiseaseHistory;

public interface DiseaseService {

	//获取病例
	public List<DiseaseHistory> getHistoryList(String userId);
	
	//获取病例内容
	public DiseaseHistory getHistory(String userId,int diseaseId);
	
	//更新病例
	public boolean modifyHistory(DiseaseHistory diseaseHistory);
	
}
