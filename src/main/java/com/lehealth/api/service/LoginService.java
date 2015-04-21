package com.lehealth.api.service;

import net.sf.json.JSONObject;

import com.lehealth.data.bean.UserBaseInfo;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.data.type.UserRoleType;

public interface LoginService {
	
	// 注册新用户
	public ErrorCodeType registerUser(String loginId, String password, String identifyingCode, UserRoleType role);
	
	// 用户登录
	public UserBaseInfo getUserByPassword(String loginId, String password);
	
	// 获取用户基础信息，并token校验
	public UserBaseInfo getUserByToken(String loginId, String token);
	
	// 检查短信验证码
	public ErrorCodeType checkIdentifyingCode(String phoneNumber, String identifyingCode);
	
	// 发送短信验证码
	public ErrorCodeType sendIdentifyingCode(String phoneNumber, String ip);
	
	// 清除验证码缓存
	public void clearIdentifyingCodeCache();
	
	// 查看内存信息
	public JSONObject getMapCache();
}
