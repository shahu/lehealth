package com.lehealth.common.util;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class TokenUtils {

	private final static String tokenKey = "asjkhdwaiugbuaiwbruawbiubiuwr";
	private final static String userIdKey = "waheaurthaiowhrioawrboiawrboiaw";
	
	public static String buildToken(String loginId,String password){
		StringBuilder sb=new StringBuilder();
		sb.append("&lehealth=").append(DigestUtils.sha1Hex(loginId))
			.append("&loginId=").append(DigestUtils.sha1Hex(password))
			.append("&password=").append(DigestUtils.sha1Hex(tokenKey));
		return DigestUtils.md5Hex(sb.toString());
	}
	
	public static String buildUserId(String loginId){
		StringBuilder sb=new StringBuilder();
		sb.append("lehealth")
			.append(DigestUtils.md5Hex(loginId))
			.append(System.currentTimeMillis())
			.append(DigestUtils.md5Hex("lehealth"))
			.append(loginId)
			.append(DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis())));
		return DigestUtils.md5Hex(sb.toString());
	}
	
	public static String buildUserId1(String loginId){
		StringBuilder sb=new StringBuilder();
		String time = String.valueOf(System.currentTimeMillis());
		sb.append("&timestamp=").append(DigestUtils.sha1Hex(userIdKey))
			.append("&lehealth=").append(DigestUtils.sha1Hex(loginId))
			.append("&loginId=").append(DigestUtils.sha1Hex(time));
		return DigestUtils.md5Hex(sb.toString());
	}
	
	public static String buildUUid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
