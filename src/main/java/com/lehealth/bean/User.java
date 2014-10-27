package com.lehealth.bean;

import net.sf.json.JSONObject;

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
	public boolean validToken(String token){
		return token.equals(TokenUtils.buildToken(this.loginid, this.pwdmd5));
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("loginid", loginid);
		obj.accumulate("token", TokenUtils.buildToken(this.loginid, this.pwdmd5));
		return obj;
	}
}
