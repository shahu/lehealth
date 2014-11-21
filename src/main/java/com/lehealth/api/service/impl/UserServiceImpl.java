package com.lehealth.api.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.UserDao;
import com.lehealth.api.service.UserService;
import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);

	//获取个人信息
	@Override
	public UserInfo getUserInfo(String userId) {
		return this.userDao.selectUserInfo(userId);
	}

	//更新个人信息
	@Override
	public boolean modifyUserInfo(UserInfo info) {
		return this.userDao.updateUserInfo(info);
	}

	//获取监护人信息
	@Override
	public List<UserGuardianInfo> getUserGuardianInfos(String userId) {
		return this.userDao.selectUserGuardianInfos(userId);
	}

	//添加监护人信息
	@Override
	public boolean modifyUserGuardianInfo(UserGuardianInfo info) {
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
