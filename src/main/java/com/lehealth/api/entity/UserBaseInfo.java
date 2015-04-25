package com.lehealth.api.entity;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.lehealth.common.util.TokenUtils;
import com.lehealth.data.type.UserRoleType;

public class UserBaseInfo {

	private String userId = "";
	private String loginId = "";
	private String pwdmd5 = "";
	private UserRoleType role = UserRoleType.panient;
	private String token = "";
	
	public UserBaseInfo(String userId, String loginId, String password, UserRoleType role, boolean md5Flag){
		this.userId = userId;
		this.loginId = loginId;
		if(md5Flag){
			this.pwdmd5 = DigestUtils.md5Hex(password);
		}else{
			this.pwdmd5 = password;
		}
		this.role = role;
		this.token = TokenUtils.buildToken(this.loginId, this.pwdmd5);
	}
	
	public UserBaseInfo(String loginId, String password,UserRoleType role, boolean md5Flag){
		this.loginId = loginId;
		if(md5Flag){
			this.pwdmd5 = DigestUtils.md5Hex(password);
		}else{
			this.pwdmd5 = password;
		}
		this.role = role;
		this.token = TokenUtils.buildToken(this.loginId, this.pwdmd5);
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
		return StringUtils.isNotBlank(this.userId) 
				&& StringUtils.equals(token, this.token);
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("loginid", loginId);
		obj.accumulate("token", this.token);
		obj.accumulate("roleid", role.getRoleId());
		return obj;
	}
}
