package com.lehealth.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.UserDao;
import com.lehealth.api.service.UserService;
import com.lehealth.bean.PanientGuardianInfo;
import com.lehealth.bean.PanientInfo;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	//获取个人信息
	@Override
	public PanientInfo getUserInfo(String userId) {
		return this.userDao.selectUserInfo(userId);
	}

	//更新个人信息
	@Override
	public boolean modifyUserInfo(PanientInfo info) {
		return this.userDao.updateUserInfo(info);
	}

	//获取监护人信息
	@Override
	public List<PanientGuardianInfo> getUserGuardianInfos(String userId) {
		return this.userDao.selectUserGuardianInfos(userId);
	}

	//添加监护人信息
	@Override
	public boolean modifyUserGuardianInfo(PanientGuardianInfo info) {
		//先删除
		this.userDao.deleteUserGuardianInfo(info.getUserId(),info.getGuardianNumber());
		//再插入
		return this.userDao.insertUserGuardianInfo(info);
	}

	//删除监护人
	@Override
	public boolean delUserGuardianInfo(String userId,String guardianNumber) {
		return this.userDao.deleteUserGuardianInfo(userId,guardianNumber);
	}

}
