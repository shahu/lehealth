package com.lehealth.common.service.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.util.Constant;

@Service("sendTemplateSMSService")
public class SendTemplateSMSServiceImpl implements SendTemplateSMSService,InitializingBean{

	private CCPRestSDK restAPI = new CCPRestSDK();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		restAPI.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount("accountSid", "accountToken");// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId("AppId");// 初始化应用ID
	}
	
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
		Map<String, Object> result = restAPI.sendTemplateSMS(to, tid, datas);
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			Map<String,Object> data = (Map<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
			return true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return false;
		}
	}
}
