package com.lehealth.common.service.impl;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.common.sdk.CCPRestSDK;
import com.lehealth.common.service.SendTemplateSMSService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.common.util.Constant;
import com.lehealth.data.type.SystemVariableKeyType;

@Service("sendTemplateSMSService")
public class SendTemplateSMSServiceImpl implements SendTemplateSMSService{

	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;
	
	private CCPRestSDK restAPI = new CCPRestSDK();
	
	private static Logger logger = Logger.getLogger(SendTemplateSMSServiceImpl.class);
	
	@Override
	public boolean sendIdentifyingCodeSMS(String phoneNumber, String identifyingCode){
		String flag = this.systemVariableService.getValue(SystemVariableKeyType.sendIdentifyingCodeMessageSwitch);
		logger.info("sendIdentifyingCodeSMS flag is : " + flag + "!");
		if(StringUtils.isNotBlank(flag)){
			String[] datas = {identifyingCode, String.valueOf(Constant.identifyingCodeValidityMinute)};
			return this.sendTemplateSMS(phoneNumber, "7779", datas);
		}else{
			return true;
		}
	}
	
	@Override
	public boolean sendNoticeSMS(String phoneNumber,String userPhoneNumber, String sbp, String dbp){
		String flag = this.systemVariableService.getValue(SystemVariableKeyType.sendNoticeMessageSwitch);
		logger.info("sendNoticeSMS flag is : " + flag + "!");
		if(StringUtils.isNotBlank(flag)){
			String[] datas = {userPhoneNumber, sbp, dbp};
			return this.sendTemplateSMS(phoneNumber, "7780", datas);
		}else{
			return true;
		}
	}
	
	private boolean sendTemplateSMS(String to, String tid, String[] datas){
		String domain = this.systemVariableService.getValue(SystemVariableKeyType.sendSMSMessageDomain);
		
		if(StringUtils.isBlank(domain)){
			logger.info("domain is null ,set default=app.cloopen.com");
			domain = "app.cloopen.com";
		}
		
		JSONObject result = restAPI.sendTemplateSMS(to, tid, datas, domain);
		
		StringBuilder sb = new StringBuilder();
		sb.append("send sms : ")
			.append("to=").append(to).append(";")
			.append("tid=").append(tid).append(";");
		if(datas != null){
			sb.append("datas=");
			for(String data : datas){
				sb.append(data).append(",");
			}
			sb.append(";");
		}
		sb.append("result=").append(result.toString());
		logger.info(sb.toString());
		
		if(result.has("statusCode")
				&& "000000".equals(result.getString("statusCode"))){
			return true;
		}else{
			return false;
		}
	}
}
