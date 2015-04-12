package com.lehealth.common.service.impl;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.lehealth.common.sdk.CCPRestSDK;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.common.util.Constant;

@Service("sendTemplateSMSService")
public class SendTemplateSMSServiceImpl implements SendTemplateSMSService{

	private CCPRestSDK restAPI = new CCPRestSDK();
	
	private static Logger logger = Logger.getLogger(SendTemplateSMSServiceImpl.class);
	
	@Override
	public boolean sendIdentifyingCodeSMS(String phoneNumber, String identifyingCode){
		String[] datas = {identifyingCode, String.valueOf(Constant.identifyingCodeValidityMinute)};
		return this.sendTemplateSMS(phoneNumber, "7779", datas);
	}
	
	@Override
	public boolean sendNoticeSMS(String phoneNumber,String userPhoneNumber, String sbp, String dbp){
		// String[] datas = {userPhoneNumber, sbp, dbp};
		// return this.sendTemplateSMS(phoneNumber, "7780", datas);
		return true;
	}
	
	private boolean sendTemplateSMS(String to, String tid, String[] datas){
		JSONObject result = restAPI.sendTemplateSMS(to, tid, datas);
		logger.info("send sms :" + result.toString());
		if(result.has("statusCode")
				&& "000000".equals(result.getString("statusCode"))){
			return true;
		}else{
			return false;
		}
	}
}
