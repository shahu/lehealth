package com.lehealth.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.LoginDao;
import com.lehealth.api.service.LoginService;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.UserRoleType;
import com.lehealth.util.TokenUtils;

@Service("loginService")
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	@Qualifier("loginDao")
	private LoginDao loginDao;
	
	private Map<String, String> identifyingCodeCache = new HashMap<String, String>();
	
	@Override
	public ErrorCodeType registerUser(String loginId,String password,UserRoleType role) {
		//是否用户名存在
		UserBaseInfo user=this.loginDao.selectUserBaseInfo(loginId);
		if(isValidUser(user)){
			return ErrorCodeType.repeatUser;
		}else{
			String userId=TokenUtils.buildUserId(loginId);
			user = new UserBaseInfo(userId, loginId, password, role);
			boolean isSuccess=this.loginDao.insertUser(user);
			if(isSuccess){
				return ErrorCodeType.normal;
			}else{
				return ErrorCodeType.abnormal;
			}
		}
	}
	
	@Override
	public UserBaseInfo getUserByPassword(String loginId, String password) {
		if(StringUtils.isNotBlank(loginId) 
				&& StringUtils.isNotBlank(password)){
			return this.loginDao.selectUserBaseInfo(loginId,DigestUtils.md5Hex(password));
		}else{
			return null;
		}
	}

	@Override
	public UserBaseInfo getUserByToken(String loginId,String token) {
		if(StringUtils.isNotBlank(loginId) 
				&& StringUtils.isNotBlank(token)){
			UserBaseInfo user = this.loginDao.selectUserBaseInfo(loginId);
			if(user.validToken(token)){
				return user;
			}
		}
		return null;
	}

	@Override
	public boolean checkIdentifyingCode(String phoneNumber,
			String identifyingCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ErrorCodeType sendIdentifyingCode(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean isValidUser(UserBaseInfo user){
		return (user != null && StringUtils.isNotBlank(user.getUserId()));
	}
}
