package com.lehealth.common.service;

import java.util.Map;
import org.springframework.core.io.FileSystemResource;

public interface SendMailService {
	// TODO 暂不处理模板形式
//	public String sendTempleteMail(String[] toMails, String[] ccMails, String mailTitle, String fromMail, String templateName, Map<String, Object> model, Map<String,FileSystemResource> inlineFiles, Map<String,FileSystemResource> attachmentFiles);
	
	public String sendContentMail(String[] toMails, String[] ccMails, String mailTitle, String fromMail, String content, Map<String,FileSystemResource> inlineFiles, Map<String,FileSystemResource> attachmentFiles);
	
}
