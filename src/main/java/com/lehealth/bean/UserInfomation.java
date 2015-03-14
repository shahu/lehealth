package com.lehealth.bean;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.lehealth.util.TokenUtils;

public class UserInfomation {

	private String userId="";
	private String loginId="";
	private String pwdmd5="";
	private int roleId=3;
	
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
	public int getRoleId() {
		return roleId;
	}
	public void setRoleID(int roleId) {
		this.roleId = roleId;
	}
	public boolean validToken(String token){
		return StringUtils.isNotBlank(this.userId) && token.equals(TokenUtils.buildToken(this.loginId, this.pwdmd5));
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("loginid", loginId);
		obj.accumulate("token", TokenUtils.buildToken(this.loginId, this.pwdmd5));
		obj.accumulate("roleid", roleId);
		return obj;
	}
}
