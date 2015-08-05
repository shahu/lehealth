package com.lehealth.common.service;

import java.util.Map;
import org.springframework.core.io.FileSystemResource;

public interface SendMailService {
	
	public String sendMail(String[] toMails,String[] ccMails,String mailTitle,String fromMail,String templateName,Map<String, Object> model,Map<String,FileSystemResource> inlineFiles,Map<String,FileSystemResource> attachmentFiles,String content);
	
}
