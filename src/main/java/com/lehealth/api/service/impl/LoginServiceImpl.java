package com.lehealth.api.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.LoginDao;
import com.lehealth.api.service.LoginService;
import com.lehealth.bean.User;
import com.lehealth.type.ErrorCodeType;
import com.lehealth.util.TokenUtils;

@Service("loginService")
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	@Qualifier("loginDao")
	private LoginDao loginDao;
	
	private static Logger logger = Logger.getLogger(LoginServiceImpl.class);

	@Override
	public ErrorCodeType checkUser4Login(String loginId,String password) {
		boolean isVaild=this.loginDao.checkUser4Login(loginId,DigestUtils.md5Hex(password));
		if(isVaild){
			return ErrorCodeType.normal;
		}else{
			return ErrorCodeType.invalidUser;
		}
	}

	@Override
	public ErrorCodeType registerNewUser(String loginId,String password,int roleId) {
		//是否用户名存在
		User user=this.loginDao.getUser(loginId);
		if(StringUtils.isNotBlank(user.getUserId())){
			return ErrorCodeType.repeatUser;
		}
		String userId=TokenUtils.buildUserId(loginId);
		user.setUserId(userId);
		user.setLoginId(loginId);
		user.setPassword(password);
		user.setRoleID(roleId);
		boolean isSuccess=this.loginDao.insertUser(user);
		if(isSuccess){
			return ErrorCodeType.normal;
		}else{
			return ErrorCodeType.abnormal;
		}
	}

	@Override
	public String checkUser4Token(String loginId,String token) {
		if(StringUtils.isNotBlank(loginId)
				&&StringUtils.isNotBlank(token)){
			User user=this.loginDao.getUser(loginId);
			if(user.validToken(token)){
				return user.getUserId();
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
}
