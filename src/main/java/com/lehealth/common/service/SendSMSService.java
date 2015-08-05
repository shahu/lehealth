package com.lehealth.common.service;

public interface SendSMSService {
	
	public boolean sendTemplateSMS(String to, String tid, String[] datas);
	
}
