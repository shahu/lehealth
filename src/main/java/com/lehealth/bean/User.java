package com.lehealth.bean;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;

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
	public String getPwd() {
		return pwdmd5;
	}
	public void setPwdmd5(String pwdmd5) {
		this.pwdmd5 = pwdmd5;
	}
	public void setPassword(String password) {
		this.pwdmd5 = DigestUtils.md5Hex(password);
	}
	public boolean validToken(String token){
		return token.equals(TokenUtils.buildToken(this.loginId, this.pwdmd5));
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("loginid", loginId);
		obj.accumulate("token", TokenUtils.buildToken(this.loginId, this.pwdmd5));
		return obj;
	}
}
