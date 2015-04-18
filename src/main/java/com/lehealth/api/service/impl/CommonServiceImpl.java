package com.lehealth.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.CommonDao;
import com.lehealth.api.service.CommonService;
import com.lehealth.data.bean.Activity;
import com.lehealth.data.bean.DiseaseCategroy;
import com.lehealth.data.bean.GoodsInfo;
import com.lehealth.data.bean.MedicineCategroy;

@Service("commonService")
public class CommonServiceImpl implements CommonService{

	@Autowired
	@Qualifier("commonDao")
	private CommonDao commonDao;
	
	@Override
	public List<Activity> getAtivities() {
		return this.commonDao.selectAtivities();
	}
	
	@Override
	public List<MedicineCategroy> getMedicines() {
		return this.commonDao.selectMedicines();
	}
	
	@Override
	public List<DiseaseCategroy> getDiseases() {
		return this.commonDao.selectDiseases();
	}

	@Override
	public List<GoodsInfo> getGoodsInfos() {
		return this.commonDao.selectGoodsInfos();
	}

	@Override
	public GoodsInfo getGoodsInfo(int goodsId) {
		return this.commonDao.selectGoodsInfo(goodsId);
	}
}
