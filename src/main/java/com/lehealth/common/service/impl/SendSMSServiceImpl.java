package com.lehealth.common.service.impl;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.common.sdk.CCPRestSDK;
import com.lehealth.common.service.SendSMSService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.data.type.SystemVariableKeyType;

@Service("sendSMSService")
public class SendSMSServiceImpl implements SendSMSService{

	@Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;
	
	private CCPRestSDK restAPI = new CCPRestSDK();
	
	private static Logger logger = Logger.getLogger(SendSMSServiceImpl.class);
	
	@Override
	public boolean sendTemplateSMS(String to, String tid, String[] datas){
		String domain = this.systemVariableService.getValue(SystemVariableKeyType.sendSMSMessageDomain);
		String appId = this.systemVariableService.getValue(SystemVariableKeyType.yuntongxunAppId);
		String sid = this.systemVariableService.getValue(SystemVariableKeyType.yuntongxunAccountSid);
		String token = this.systemVariableService.getValue(SystemVariableKeyType.yuntongxunAccountToken);
//		(开发)sandboxapp.cloopen.com
//		(生产)app.cloopen.com
		if(StringUtils.isBlank(domain)){
			logger.info("domain is null ,set default=app.cloopen.com");
			domain = "app.cloopen.com";
		}
		
		JSONObject result = restAPI.sendTemplateSMS(to, tid, datas, domain, appId, sid, token);
		
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
		sb.append("domain=").append(domain).append(";")
			.append("result=").append(result.toString()).append(";");
		logger.info(sb.toString());
		
		if(result.has("statusCode")
				&& StringUtils.equals("000000", result.getString("statusCode"))){
			return true;
		}else{
			return false;
		}
	}
}
