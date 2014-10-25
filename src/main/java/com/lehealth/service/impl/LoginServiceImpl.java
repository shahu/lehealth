package com.lehealth.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.bean.User;
import com.lehealth.dao.LoginDao;
import com.lehealth.service.LoginService;
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
	public ErrorCodeType registerNewUser(String loginId,String password) {
		//是否用户名存在
		User user=this.loginDao.getUser(loginId);
		if(StringUtils.isNotBlank(user.getUserid())){
			return ErrorCodeType.repeatUser;
		}
		String userId=TokenUtils.buildUserId(loginId);
		user.setUserid(userId);
		user.setLoginid(loginId);
		user.setPassword(password);
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
			if(token.equals(user.getToken())){
				return user.getUserid();
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
}
