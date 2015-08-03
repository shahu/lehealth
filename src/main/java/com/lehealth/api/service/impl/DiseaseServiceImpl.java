package com.lehealth.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.DiseaseDao;
import com.lehealth.api.entity.DiseaseHistory;
import com.lehealth.api.service.DiseaseService;

@Service("diseaseService")
public class DiseaseServiceImpl implements DiseaseService{

	@Autowired
	@Qualifier("diseaseDao")
	private DiseaseDao diseaseDao;
	
	@Override
	public List<DiseaseHistory> getHistoryList(String userId) {
		return this.diseaseDao.selectHistorys(userId);
	}

	@Override
	public boolean modifyHistory(DiseaseHistory diseaseHistory) {
		return this.diseaseDao.updateHistory(diseaseHistory);
	}

	@Override
	public DiseaseHistory getHistory(String userId, int diseaseId) {
		return this.diseaseDao.selectHistory(userId,diseaseId);
	}

	@Override
	public boolean uploadPanientFiles(String userId, List<String> files) {
		StringBuilder sb = new StringBuilder();
		for(String file : files){
			sb.append(",").append(file);
		}
		if(sb.length() > 0){
			return this.diseaseDao.updatePanientFiles(userId, sb.substring(1));
		}
		return false;
	}
}
