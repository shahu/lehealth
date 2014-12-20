package com.lehealth.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.PanientDao;
import com.lehealth.api.service.PanientService;
import com.lehealth.bean.PanientGuardianInfo;
import com.lehealth.bean.PanientInfo;

@Service("panientService")
public class PanientServiceImpl implements PanientService{

	@Autowired
	@Qualifier("panientDao")
	private PanientDao panientDao;
	
	//获取个人信息
	@Override
	public PanientInfo getInfo(String userId) {
		return this.panientDao.selectInfo(userId);
	}

	//更新个人信息
	@Override
	public boolean modifyInfo(PanientInfo info) {
		return this.panientDao.updateInfo(info);
	}

	//获取监护人信息
	@Override
	public List<PanientGuardianInfo> getGuardianInfos(String userId) {
		return this.panientDao.selectGuardianInfos(userId);
	}

	//添加监护人信息
	@Override
	public boolean modifyGuardianInfo(PanientGuardianInfo info) {
		//先删除
		this.panientDao.deleteGuardianInfo(info.getUserId(),info.getGuardianNumber());
		//再插入
		return this.panientDao.insertGuardianInfo(info);
	}

	//删除监护人
	@Override
	public boolean deleteGuardianInfo(String userId,String guardianNumber) {
		return this.panientDao.deleteGuardianInfo(userId,guardianNumber);
	}

	@Override
	public List<PanientInfo> getPanientList(String doctorId) {
		return this.panientDao.selectPanients(doctorId);
	}

}
