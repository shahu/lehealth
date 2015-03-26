package com.lehealth.data.bean;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.lehealth.data.type.UserRoleType;
import com.lehealth.util.TokenUtils;

public class UserBaseInfo {

	private String userId="";
	private String loginId="";
	private String pwdmd5="";
	private UserRoleType role=UserRoleType.panient;
	
	public UserBaseInfo(String userId, String loginId, String password,UserRoleType role){
		this.userId = userId;
		this.loginId = loginId;
		this.pwdmd5 = DigestUtils.md5Hex(password);
		this.role = role;
	}
	
	public UserBaseInfo(String loginId, String pwdmd5,UserRoleType role){
		this.loginId = loginId;
		this.pwdmd5 = pwdmd5;
		this.role = role;
	}
	
	public String getUserId() {
		return userId;
	}
	public String getLoginId() {
		return loginId;
	}
	public String getPwdmd5() {
		return pwdmd5;
	}
	public UserRoleType getRole() {
		return role;
	}
	
	public boolean validToken(String token){
		return StringUtils.isNotBlank(this.userId) && token.equals(TokenUtils.buildToken(this.loginId, this.pwdmd5));
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("loginid", loginId);
		obj.accumulate("token", TokenUtils.buildToken(this.loginId, this.pwdmd5));
		obj.accumulate("roleid", role.getRoleId());
		return obj;
	}
}
