package com.lehealth.api.service;

import java.util.List;
import com.lehealth.bean.DiseaseHistory;

public interface DiseaseService {

	//获取病例
	public List<DiseaseHistory> getDiseaseHistorys(String userId);
	
	//更新病例
	public boolean modifyDiseaseHistory(DiseaseHistory diseaseHistory);
	
}
