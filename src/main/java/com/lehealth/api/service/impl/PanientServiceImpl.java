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
	public PanientInfo getUserInfo(String userId) {
		return this.panientDao.selectUserInfo(userId);
	}

	//更新个人信息
	@Override
	public boolean modifyUserInfo(PanientInfo info) {
		return this.panientDao.updateUserInfo(info);
	}

	//获取监护人信息
	@Override
	public List<PanientGuardianInfo> getUserGuardianInfos(String userId) {
		return this.panientDao.selectUserGuardianInfos(userId);
	}

	//添加监护人信息
	@Override
	public boolean modifyUserGuardianInfo(PanientGuardianInfo info) {
		//先删除
		this.panientDao.deleteUserGuardianInfo(info.getUserId(),info.getGuardianNumber());
		//再插入
		return this.panientDao.insertUserGuardianInfo(info);
	}

	//删除监护人
	@Override
	public boolean delUserGuardianInfo(String userId,String guardianNumber) {
		return this.panientDao.deleteUserGuardianInfo(userId,guardianNumber);
	}

}
