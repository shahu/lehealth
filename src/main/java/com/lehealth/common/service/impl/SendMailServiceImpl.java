package com.lehealth.common.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.lehealth.api.entity.SmtpAuthenticator;
import com.lehealth.common.service.SendMailService;
import com.lehealth.common.service.SystemVariableService;
import com.lehealth.data.type.SystemVariableKeyType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("sendMailService")
public class SendMailServiceImpl implements InitializingBean,SendMailService {
    
    private static Log logger = LogFactory.getLog(SendMailServiceImpl.class);
    
    private FreeMarkerConfigurer freeMarkerConf;
    private JavaMailSenderImpl sender;
    
    @Autowired
	@Qualifier("systemVariableService")
	private SystemVariableService systemVariableService;
    
    @Override
	public void afterPropertiesSet() throws Exception {
    	String userName = this.systemVariableService.getValue(SystemVariableKeyType.noticeFromMailAccount);
    	String password = this.systemVariableService.getValue(SystemVariableKeyType.noticeFromMailPassword);

    	// mail
    	SmtpAuthenticator smtpAuthenticator = new SmtpAuthenticator(userName, password);
    	Properties mailSettings = new Properties();
    	mailSettings.setProperty("mail.smtp.auth", "true");
    	Session mailSession = Session.getInstance(mailSettings, smtpAuthenticator);
    	this.sender = new JavaMailSenderImpl();
    	this.sender.setHost("host");
    	this.sender.setPort(111);
    	this.sender.setSession(mailSession);
    	
    	// freeMarker
    	this.freeMarkerConf = new FreeMarkerConfigurer();
    	this.freeMarkerConf.setTemplateLoaderPath("/freemarkertemplate/");
    	Properties freeMarkerSettings = new Properties();
    	freeMarkerSettings.setProperty("template_update_delay", "86400");
    	freeMarkerSettings.setProperty("default_encoding", "UTF-8");
    	freeMarkerSettings.setProperty("locale", "zh_CN");
    	this.freeMarkerConf.setFreemarkerSettings(freeMarkerSettings);
	}
    
    /**
     * 使用模版发送HTML格式的邮件
     * 
     * @param msg
     *            装有to,cc,from,subject信息的SimpleMailMessage
     * @param templateName
     *            模版名,模版根路径已在配置文件定义于freemakarengine中
     * @param model
     *            渲染模版所需的数据
     * @param inlineFiles
     *            需要内联的多媒体文件,key为contentId,value为该文件
     * @param attachmentFiles
     *            需要已附件形式发送的文件,key为附件文件名称,value为该文件
     * @param 
     *            邮件正文，为空则使用模版发送
     */
    @Override
    public String sendMail(String[] toMails,String[] ccMails,String mailTitle,String fromMail,String templateName,Map<String, Object> model,Map<String,FileSystemResource> inlineFiles,Map<String,FileSystemResource> attachmentFiles,String content) {
        String errmsg="";
        try {
            MimeMessage msg = sender.createMimeMessage();
            MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(msg, true, "utf8");
            if(toMails!=null){
                helper.setTo(toMails);
            }
            if(ccMails!=null){
                helper.setCc(ccMails);
            }
            helper.setSubject(mailTitle);
            helper.setFrom(fromMail);
            String htmlText = content;
            if(StringUtils.isBlank(htmlText)){
                htmlText = getEmailContent(templateName, model);//使用模板生成html邮件内容
                if (htmlText == null || htmlText.trim().equals("")) {
                    logger.info("No content is gernerated.");
                    return "No content is gernerated.";
                }
            }
            helper.setText(htmlText, true);
            //这个必须放在setText后面，否则图会挂
            //内联多媒体文件
            if(inlineFiles!=null&&inlineFiles.size()>0){
                for(Entry<String,FileSystemResource> e:inlineFiles.entrySet()){
                    helper.addInline(e.getKey(),e.getValue());
                }
            }
            //发送附件
            if(attachmentFiles!=null&&attachmentFiles.size()>0){
                for(Entry<String,FileSystemResource> e:attachmentFiles.entrySet()){
                    helper.addAttachment(e.getKey(),e.getValue());
                }
            }
            int retry = 0;
            boolean flag = true;
            while(retry < 3){
                try{
                    flag = true;
                    sender.send(msg);
                } catch (Exception ex) {
                    flag = false;
                    retry++;
                    logger.info("Send templated mail failed! retry:" + retry);
                    logger.error(ex.getMessage());
                    errmsg="MailException";
                } finally {
                    if (flag) {
                        errmsg="";
                        logger.info("Send templated mail success!");
                        break;
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            errmsg="MessagingException";
        } 
        return errmsg;
    }
    
    /**
     * 通过模板产生邮件正文
     * 
     * @param templateName
     *            邮件模板名称
     * @param map
     *            模板中要填充的对象
     * @return 邮件正文（HTML）
     */
    private String getEmailContent(String templateName, Map<?, ?> map) {
        try {
            if(map!=null){
                logger.info("Try to get content.");
                Configuration config = freeMarkerConf.getConfiguration();
                Template temp = config.getTemplate(templateName);
                return FreeMarkerTemplateUtils.processTemplateIntoString(temp, map);
            }else{
                return "";
            }
        } catch (TemplateException e) {
            logger.error("Error while processing freemarker template ", e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("Error while open template file ", e);
        } catch (IOException e) {
            logger.error("Error while generate Email Content ", e);
        } catch (Exception e) {
            logger.error("Error while generate Email Content ", e);
        }
        return null;
    }
	
}
