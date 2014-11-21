package com.lehealth.api.dao;

import java.util.List;
import com.lehealth.bean.DiseaseHistory;

public interface DiseaseDao {
	
	//获取病例
	public List<DiseaseHistory> selectDiseaseHistorys(String userId);
	
	//更新病例
	public boolean updateDiseaseHistory(DiseaseHistory diseaseHistory);
}
