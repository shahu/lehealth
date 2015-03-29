package com.lehealth.api.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
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
	
	@Override
	public ErrorCodeType registerUser(String loginId,String password,UserRoleType role) {
		//是否用户名存在
		UserBaseInfo user=this.loginDao.selectUserBaseInfo(loginId);
		if(isValidUser(user)){
			return ErrorCodeType.repeatPhoneNumber;
		}else{
			String userId=TokenUtils.buildUserId(loginId);
			user = new UserBaseInfo(userId, loginId, password, role);
			boolean isSuccess=this.loginDao.insertUser(user);
			if(isSuccess){
				// 如果注册成功则清理掉
				this.identifyingCodeCache.remove(loginId);
				this.identifyingCodeTime.remove(loginId);
				return ErrorCodeType.success;
			}else{
				return ErrorCodeType.failed;
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
	public ErrorCodeType checkIdentifyingCode(String phoneNumber, String identifyingCode) {
		//TODO test
		if("123456".equals(identifyingCode)){
			return ErrorCodeType.success;
		}
		
		if(this.identifyingCodeCache.containsKey(phoneNumber)
			&& identifyingCodeTime.containsKey(phoneNumber)){
			boolean codeCheck = identifyingCode.equals(this.identifyingCodeCache.get(phoneNumber));
			if(!codeCheck){
				return ErrorCodeType.failedIdentifyingCode;
			}
			long timeDiff = System.currentTimeMillis() - identifyingCodeTime.get(phoneNumber);
			boolean timeCheck = timeDiff < (Constant.identifyingCodeValidityTime * 2);
			if(!timeCheck){
				return ErrorCodeType.expireIdentifyingCode;
			}
			return ErrorCodeType.success;
		}else{
			return ErrorCodeType.failedIdentifyingCode;
		}
	}

	private Random random = new Random();
	
	// key = phone
	private Map<String, String> identifyingCodeCache = new ConcurrentHashMap<String, String>();
	private Map<String, Long> identifyingCodeTime = new ConcurrentHashMap<String, Long>();
	// key = ip
	private Map<String, AtomicInteger> identifyingCodeCount = new ConcurrentHashMap<String, AtomicInteger>();
	
	@Override
	public ErrorCodeType sendIdentifyingCode(String phoneNumber, String ip) {
		// ip检查
		if(this.identifyingCodeCount.containsKey(ip)){
			int count = this.identifyingCodeCount.get(ip).get();
			if(count > 5){
				return ErrorCodeType.muchIdentifyingCode;
			}
		}
		// 是否发送检查
		if(this.identifyingCodeCache.containsKey(phoneNumber)){
			long sendTime = System.currentTimeMillis();
			if(this.identifyingCodeTime.containsKey(phoneNumber)){
				sendTime = this.identifyingCodeTime.get(phoneNumber);
			}
			long diff = System.currentTimeMillis() - sendTime;
			if(diff < (Constant.identifyingCodeValidityTime * 2)){
				return ErrorCodeType.muchIdentifyingCode;
			}
		}
		
		// 发送验证短信
		String identifyingCode = String.valueOf(random.nextInt(89999999)+10000000);
		boolean flag = this.sendTemplateSMSService.sendIdentifyingCodeSMS(phoneNumber, identifyingCode);
		if(flag){
			this.identifyingCodeCache.put(phoneNumber, identifyingCode);
			this.identifyingCodeTime.put(phoneNumber, System.currentTimeMillis());
			if(!this.identifyingCodeCount.containsKey(ip)){
				this.identifyingCodeCount.put(ip, new AtomicInteger());
			}
			this.identifyingCodeCount.get(ip).incrementAndGet();
			return ErrorCodeType.success;
		}else{
			return ErrorCodeType.success;
			// return ErrorCodeType.failed;
		}
	}
	
	private boolean isValidUser(UserBaseInfo user){
		return (user != null && StringUtils.isNotBlank(user.getUserId()));
	}
	
	@Override
	public void clearIdentifyingCodeCache(){
		if(!this.identifyingCodeTime.isEmpty()){
			long now = System.currentTimeMillis();
			for(Entry<String, Long> e : this.identifyingCodeTime.entrySet()){
				long diff = now - e.getValue();
				if(diff > Constant.identifyingCodeValidityClearTime){
					this.identifyingCodeTime.remove(e.getKey());
					this.identifyingCodeCache.remove(e.getKey());
				}else if(!this.identifyingCodeCache.containsKey(e.getKey())){
					this.identifyingCodeTime.remove(e.getKey());
				}
			}
		}
		if(!this.identifyingCodeCache.isEmpty()){
			for(Entry<String, String> e : this.identifyingCodeCache.entrySet()){
				if(!this.identifyingCodeTime.containsKey(e.getKey())){
					this.identifyingCodeCache.remove(e.getKey());
				}
			}
		}
		if(!this.identifyingCodeCount.isEmpty()){
			this.identifyingCodeCount.clear();
		}
	}
	
}
