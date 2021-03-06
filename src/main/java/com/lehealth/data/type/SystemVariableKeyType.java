package com.lehealth.data.type;

import java.util.HashMap;
import java.util.Map;

public enum SystemVariableKeyType {

	unknown("unknown"),
	sendSituationNoticeMessageSwitch("send_situation_notice_message_switch"),
	sendOrderNoticeMessageSwitch("send_order_notice_message_switch"),
	sendIdentifyingCodeMessageSwitch("send_identifyingcode_message_switch"),
	sendSMSMessageDomain("send_SMS_message_domain"),
	
	yuntongxunAccountSid("yuntongxun_account_sid"),
	yuntongxunAccountToken("yuntongxun_account_token"),
	yuntongxunAppId("yuntongxun_app_id"),
	
	weixinAppID("weixin_app_id"),
	weixinAppSecret("weixin_app_secret"),
	weixinConfigNoncestr("weixin_config_noncestr"),
	weixinPrePayNoncestr("weixin_pre_pay_noncestr"),
	weixinCheckOrderNocestr("weixin_check_order_noncestr"),
	weixinMchId("weixin_mch_id"),
	weixinApiKey("weixin_api_key"),
	
	noticeToPhoneNumber("notice_to_phone_number"),
	noticeToMailAddr("notice_to_mail_addr"),
	
	noticeFromMailAccount("notice_from_mail_account"),
	noticeFromMailPassword("notice_from_mail_password")
	;
	
	private SystemVariableKeyType(String key) {
		this.key = key;
	}
	
	private final String key;
	private final static Map<String, SystemVariableKeyType> map = new HashMap<String, SystemVariableKeyType>();
	
	static{
		for(SystemVariableKeyType key : SystemVariableKeyType.values()){
			map.put(key.getKey(), key);
		}
	}
	
	public String getKey() {
		return key;
	}
	
	public static SystemVariableKeyType getType(String key){
		if(map.containsKey(key)){
			return map.get(key);
		}else{
			return SystemVariableKeyType.unknown;
		}
	}
}
