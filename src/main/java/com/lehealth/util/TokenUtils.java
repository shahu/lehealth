package com.lehealth.util;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class TokenUtils {

	public static String buildToken(String loginId,String password){
		StringBuilder sb=new StringBuilder();
		sb.append(password)
			.append("lehealth")
			.append(DigestUtils.md5Hex(loginId))
			.append(DigestUtils.md5Hex("lehealth"))
			.append(DigestUtils.md5Hex("password"))
			.append(loginId);
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
	
	public static String buildUUid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
