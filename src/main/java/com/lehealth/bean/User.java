package com.lehealth.bean;

import org.apache.commons.codec.digest.DigestUtils;

import com.lehealth.util.TokenUtils;

public class User {

	private String userid;
	private String loginid;
	private String pwdmd5;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
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
	public String getToken(){
		return TokenUtils.buildToken(this.loginid, this.pwdmd5);
	}
}
