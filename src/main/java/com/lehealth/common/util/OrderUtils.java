package com.lehealth.common.util;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class OrderUtils {
	
	private static AtomicInteger count = new AtomicInteger();
	
	public static String buiildOrderId(){
		String date = DateFormatUtils.format(new Date(), Constant.dateFormat_yyyymmddhhmmss);
		return "O" + date + count.incrementAndGet();
	}
	
	private final static String key = "asdiaubdoiwanoiawbntfbaworbaio";
	
	public static String buiildOrderSecret(String orderId, String openId){
		StringBuilder sb = new StringBuilder();
		sb.append("&key=").append(DigestUtils.sha1Hex(orderId))
			.append("&orderId=").append(DigestUtils.sha1Hex(openId))
			.append("&openId=").append(DigestUtils.sha1Hex(key));
		return DigestUtils.md5Hex(sb.toString());
	}
}
