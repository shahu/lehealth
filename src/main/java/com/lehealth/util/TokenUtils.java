package com.lehealth.util;

import org.apache.commons.codec.digest.DigestUtils;

public class TokenUtils {

	public static String buildToken(String loginId){
		StringBuilder sb=new StringBuilder();
		sb.append("lehealth")
			.append(DigestUtils.md5Hex(loginId))
			.append(DigestUtils.md5Hex("lehealth"))
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
}
