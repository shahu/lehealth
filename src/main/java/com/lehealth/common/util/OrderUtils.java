package com.lehealth.common.util;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DateFormatUtils;

public class OrderUtils {
	
	private static AtomicInteger count = new AtomicInteger();
	
	public static String buiildOrderId(){
		String date = DateFormatUtils.format(new Date(), Constant.dateFormat_yyyymmddhhmmss);
		return "O" + date + count.incrementAndGet();
	}
	
}
