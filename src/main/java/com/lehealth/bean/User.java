package com.lehealth.bean;

import org.apache.commons.codec.digest.DigestUtils;

import com.lehealth.util.JacksonGlobalMappers;
import com.lehealth.util.TokenUtils;

public class User {

	private String userId;
	private String loginId;
	private String pwdmd5;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPwdmd5() {
		return pwdmd5;
	}
	public void setPassword(String password) {
		this.pwdmd5 = DigestUtils.md5Hex(password);
	}
	public String getToken(){
		return TokenUtils.buildToken(this.loginId);
	}
	
	public String toJson(){
		return JacksonGlobalMappers.getNoNullJsonStr(this);
	}
}
