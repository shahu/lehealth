package com.lehealth.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.PanientDao;
import com.lehealth.api.service.PanientService;
import com.lehealth.data.bean.PanientGuardianInfo;
import com.lehealth.data.bean.PanientInfo;

@Service("panientService")
public class PanientServiceImpl implements PanientService{

	@Autowired
	@Qualifier("panientDao")
	private PanientDao panientDao;
	
	//获取个人信息
	@Override
	public PanientInfo getPanient(String userId) {
		return this.panientDao.selectPanient(userId);
	}

	//更新个人信息
	@Override
	public boolean modifyPanient(PanientInfo info) {
		return this.panientDao.updatePanient(info);
	}

	//获取监护人列表
	@Override
	public List<PanientGuardianInfo> getGuardianList(String userId) {
		return this.panientDao.selectGuardianList(userId);
	}

	// 添加监护人信息
	@Override
	public boolean modifyGuardian(PanientGuardianInfo info) {
		//先删除
		this.panientDao.deleteGuardian(info.getUserId(),info.getGuardianNumber());
		//再插入
		return this.panientDao.insertGuardian(info);
	}

	// 删除监护人
	@Override
	public boolean deleteGuardian(String userId,String guardianNumber) {
		return this.panientDao.deleteGuardian(userId,guardianNumber);
	}

	// 获取被监护人列表
	@Override
	public List<PanientInfo> getPanientListByGuardian(String guardianPhoneNumber) {
		return this.panientDao.selectPanientListByGuardian(guardianPhoneNumber);
	}
	
	// 获取被关注病人列表
	@Override
	public List<PanientInfo> getPanientListByDoctor(String doctorId) {
		return this.panientDao.selectPanientListByDoctor(doctorId);
	}

}
