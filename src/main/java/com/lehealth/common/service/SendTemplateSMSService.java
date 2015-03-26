package com.lehealth.common.service;

public interface SendTemplateSMSService {
	
	public boolean sendIdentifyingCodeSMS(String phoneNumber, String identifyingCode);
	
	public boolean sendNoticeSMS(String phoneNumber,String userPhoneNumber, String sbp, String dbp);
	
}
