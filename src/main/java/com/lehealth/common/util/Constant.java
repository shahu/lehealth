package com.lehealth.common.util;

public class Constant {
	public static final String dateFormat_hh = "HH";
	public static final String dateFormat_yyyy = "yyyy";
	public static final String dateFormat_yyyy_mm_dd = "yyyy-MM-dd";
	public static final String dateFormat_yyyy_mm_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String dateFormat_yyyymmddhhmmss = "yyyyMMddHHmmss";
	
	public static final long identifyingCodeValidityMinute = 1;
	public static final long identifyingCodeValidityTime = identifyingCodeValidityMinute*60*1000;
	public static final long identifyingCodeValidityClearTime = 60*60*1000;
	
	public static final String weixinNotifyUrl = "http://lehealth.net.cn/lehealth/weixin/callback/pay";
	public static final String weixinTradeType = "JSAPI";
	public static final String weixinPrePayApi = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String weixinSearchApi = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	public static String switchFlag = "1";
	public static String sendIdentifyingCodeTempleteId = "7779";
	public static String sendSituationNoticeTempleteId = "7780";
	public static String sendOrderNoticeTempleteId = "29694";
}
