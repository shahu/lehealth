package com.lehealth.api.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.LoginDao;
import com.lehealth.api.service.LoginService;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.UserRoleType;
import com.lehealth.util.Constant;
import com.lehealth.util.TokenUtils;

@Service("loginService")
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	@Qualifier("loginDao")
	private LoginDao loginDao;
	
	@Autowired
	@Qualifier("sendTemplateSMSService")
	private SendTemplateSMSService sendTemplateSMSService;
	
	private Random random = new Random();
	private long limit = NumberUtils.toLong(Constant.identifyingCodeValidityMinute)*60*1000;
	
	private Map<String, String> identifyingCodeCache = new ConcurrentHashMap<String, String>();
	private Map<String, Long> identifyingCodeTime = new ConcurrentHashMap<String, Long>();
	
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
		if(this.identifyingCodeCache.containsKey(phoneNumber)
				&& identifyingCode.equals(this.identifyingCodeCache.get(phoneNumber))){
			this.identifyingCodeCache.remove(phoneNumber);
			this.identifyingCodeTime.remove(phoneNumber);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public ErrorCodeType sendIdentifyingCode(String phoneNumber) {
		String identifyingCode = String.valueOf(random.nextInt(89999999)+10000000);
		boolean flag = this.sendTemplateSMSService.sendIdentifyingCodeSMS(phoneNumber, identifyingCode);
		if(flag){
			this.identifyingCodeCache.put(phoneNumber, identifyingCode);
			this.identifyingCodeTime.put(phoneNumber, System.currentTimeMillis());
			return ErrorCodeType.normal;
		}else{
			return ErrorCodeType.abnormal;
		}
	}
	
	private boolean isValidUser(UserBaseInfo user){
		return (user != null && StringUtils.isNotBlank(user.getUserId()));
	}
	
	@Override
	public void clearIdentifyingCodeCache(){
		if(!this.identifyingCodeCache.isEmpty()
				&& !this.identifyingCodeTime.isEmpty()){
			long now = System.currentTimeMillis();
			for(Entry<String, Long> e : this.identifyingCodeTime.entrySet()){
				long diff = now - e.getValue();
				if(diff > limit){
					this.identifyingCodeCache.remove(e.getKey());
					this.identifyingCodeTime.remove(e.getKey());
				}
			}
		}
	}
}
