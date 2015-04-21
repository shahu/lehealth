package com.lehealth.api.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.api.dao.LoginDao;
import com.lehealth.api.service.LoginService;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.common.util.Constant;
import com.lehealth.common.util.TokenUtils;
import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.UserRoleType;

@Service("loginService")
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	@Qualifier("loginDao")
	private LoginDao loginDao;
	
	@Autowired
	@Qualifier("sendTemplateSMSService")
	private SendTemplateSMSService sendTemplateSMSService;
	
	@Override
	public ErrorCodeType registerUser(String loginId, String password, String identifyingCode, UserRoleType role) {
		//是否用户名存在
		UserBaseInfo user=this.loginDao.selectUserBaseInfo(loginId);
		if(isValidUser(user)){
			return ErrorCodeType.repeatPhoneNumber;
		}else{
			ErrorCodeType errorCode = this.checkIdentifyingCode(loginId, identifyingCode);
			if(errorCode == ErrorCodeType.success){
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
			}else{
				return errorCode;
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
			if(isValidUser(user) && user.validToken(token)){
				return user;
			}
		}
		return null;
	}

	@Override
	public ErrorCodeType checkIdentifyingCode(String phoneNumber, String identifyingCode) {
		if(this.identifyingCodeCache.containsKey(phoneNumber)
			&& identifyingCodeTime.containsKey(phoneNumber)){
			boolean codeCheck = identifyingCode.equals(this.identifyingCodeCache.get(phoneNumber));
			if(!codeCheck){
				return ErrorCodeType.invalidIdentifyingCode;
			}
			long timeDiff = System.currentTimeMillis() - identifyingCodeTime.get(phoneNumber);
			boolean timeCheck = timeDiff < (Constant.identifyingCodeValidityTime * 2);
			if(!timeCheck){
				return ErrorCodeType.expireIdentifyingCode;
			}
			return ErrorCodeType.success;
		}else{
			return ErrorCodeType.noneIdentifyingCode;
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
		// 手机号检查
		UserBaseInfo user=this.loginDao.selectUserBaseInfo(phoneNumber);
		if(isValidUser(user)){
			return ErrorCodeType.repeatPhoneNumber;
		}
		
		// ip检查
		if(this.identifyingCodeCount.containsKey(ip)){
			int count = this.identifyingCodeCount.get(ip).get();
			if(count >= 5){
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
		String identifyingCode = String.valueOf(random.nextInt(899999)+100000);
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
			return ErrorCodeType.failed;
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
	
	@Override
	public JSONObject getMapCache(){
		JSONObject result = new JSONObject();
		if(!this.identifyingCodeTime.isEmpty()){
			JSONArray arr = new JSONArray();
			arr.addAll(this.identifyingCodeTime.entrySet());
			result.accumulate("identifyingCodeTime", arr);
		}
		if(!this.identifyingCodeCache.isEmpty()){
			JSONArray arr = new JSONArray();
			arr.addAll(this.identifyingCodeCache.entrySet());
			result.accumulate("identifyingCodeCache", arr);
		}
		if(!this.identifyingCodeCount.isEmpty()){
			JSONArray arr = new JSONArray();
			arr.addAll(this.identifyingCodeCount.entrySet());
			result.accumulate("identifyingCodeCache", arr);
		}
		return result;
	}
}
