package com.lehealth.common.service;

public interface SendSMSService {
	
	public boolean sendIdentifyingCodeSMS(String phoneNumber, String identifyingCode);
	
	public void sendSituationNoticeSMS(String phoneNumber,String userPhoneNumber, String sbp, String dbp);
	
	public void sendOrderNoticeSMS(String userPhoneNumber);
}
