package com.lehealth.data.type;

public enum SystemVariableKeyType {

	sendNoticeMessageSwitch("send_notice_message_switch"),
	
	sendIdentifyingCodeMessageSwitch("send_identifyingcode_message_switch"),
	
	sendSMSMessageDomain("send_SMS_message_domain");
	
	private SystemVariableKeyType(String key) {
		this.key = key;
	}
	
	private final String key;

	public String getKey() {
		return key;
	}
	
}
