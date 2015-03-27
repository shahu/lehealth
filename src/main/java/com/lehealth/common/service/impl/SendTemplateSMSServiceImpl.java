package com.lehealth.common.service.impl;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.lehealth.common.sdk.CCPRestSDK;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.util.Constant;

@Service("sendTemplateSMSService")
public class SendTemplateSMSServiceImpl implements SendTemplateSMSService{

	private CCPRestSDK restAPI = new CCPRestSDK();
	
	@Override
	public boolean sendIdentifyingCodeSMS(String phoneNumber, String identifyingCode){
		String[] datas = {identifyingCode,Constant.identifyingCodeValidityMinute};
		return this.sendTemplateSMS(phoneNumber, "7779", datas);
	}
	
	@Override
	public boolean sendNoticeSMS(String phoneNumber,String userPhoneNumber, String sbp, String dbp){
		String[] datas = {userPhoneNumber, sbp, dbp};
		return this.sendTemplateSMS(phoneNumber, "7780", datas);
	}
	
	private boolean sendTemplateSMS(String to, String tid, String[] datas){
		JSONObject result = restAPI.sendTemplateSMS(to, tid, datas);
		System.out.println(result.toString());
		if(result.has("statusCode")
				&& "000000".equals(result.getString("statusCode"))){
			return true;
		}else{
			return false;
		}
	}
}
