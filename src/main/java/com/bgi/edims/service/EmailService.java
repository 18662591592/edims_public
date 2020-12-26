package com.bgi.edims.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.bgi.edims.mapper.SysConfigMapper;
import com.bgi.edims.mapper.UserMapper;
import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiMethod;
import com.bgi.edims.model.EdiSysConfig;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.model.EmailConfig;
import com.bgi.edims.util.UtilTools;

@Service
public class EmailService {
	private Logger logger=LoggerFactory.getLogger(EmailService.class);
	
	public static  EmailConfig emailConfig;
	

    @Autowired
    private SysConfigMapper sysConfigMapper;
	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;
	@Autowired
	private UserMapper userMapper;
	public void sentEmailToAllAdmins(String subject,String context) {
		logger.info("[邮件服务]群发邮件");
		EdiUser paramUser=new EdiUser();
		paramUser.setIsEmailOn(0);
		List<EdiUser> ediUsers=userMapper.getBaseEdiUsers(paramUser);
		List<String> toEmails=new ArrayList<>();
		for(EdiUser ediUser:ediUsers) {
			String email=ediUser.getEmail();
			if(UtilTools.isEmail(email)) {
				toEmails.add(email);
				
			}
		}
		
		
		if(null!=toEmails&&toEmails.size()>0) {
			String[] toEmailArray=new String[toEmails.size()];
			for(int i=0;i<toEmails.size();i++) {
				toEmailArray[i]=toEmails.get(i);
			}
			sendEmail(toEmailArray, subject, context);
		}
	}
	
	
	/**
	 * 群发邮件
	 * @param toEmails
	 * @param subject
	 * @param context
	 */
	public void sendEmail(String[] toEmails,String subject,String context) {
		if(null==emailConfig) {
			logger.info("[邮件服务]邮箱配置不存在，从数据库获取邮箱配置");
			emailConfig=resetEmailConfig();
		}
		try {
			javaMailSenderImpl.testConnection();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailConfig.getEmail());
		message.setTo(toEmails);
		message.setSubject(subject);
		message.setText(context);
		javaMailSenderImpl.send(message);
	}
	
	public void sendEmail(String toEmail,String subject,String context) {
		if(null==emailConfig) {
			logger.info("[邮件服务]邮箱配置不存在，从数据库获取邮箱配置");
			emailConfig=resetEmailConfig();
		}
		try {
			javaMailSenderImpl.testConnection();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailConfig.getEmail());
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(context);
		javaMailSenderImpl.send(message);
	}
	@EdiMethod(value="testEmail")
	public void testConnection() throws EdiException {
		if(null==emailConfig) {
			logger.info("[邮件服务]邮箱配置不存在，从数据库获取邮箱配置");
			emailConfig=resetEmailConfig();
		}
		try {
			javaMailSenderImpl.testConnection();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "邮箱配置不可用");
		}
	}
	
	public EmailConfig resetEmailConfig() {
		Map<String, String> systemConfigs=getSystemConfigMap();
		emailConfig=new EmailConfig();
		emailConfig.setHost(systemConfigs.get("emailhost"));
		emailConfig.setPort(systemConfigs.get("emailport"));
		emailConfig.setEmail(systemConfigs.get("emailaddr"));
		emailConfig.setPassword(systemConfigs.get("emailpwd"));
		int port=465;
		try {
			port=Integer.parseInt(emailConfig.getPort());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		javaMailSenderImpl.setHost(emailConfig.getHost());
		javaMailSenderImpl.setPort(port);
		javaMailSenderImpl.setUsername(emailConfig.getEmail());
		javaMailSenderImpl.setPassword(emailConfig.getPassword());
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");//开启认证
        properties.setProperty("mail.debug", "true");//启用调试
        properties.setProperty("mail.smtp.timeout", "1000");//设置链接超时
        properties.setProperty("mail.smtp.port",Integer.toString(port));//设置端口
        properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(port));//设置ssl端口
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailSenderImpl.setJavaMailProperties(properties);
		return emailConfig;
	}
	
	public EmailConfig getEmailConfig() {
		
		if(null==emailConfig) {
			logger.info("[邮件服务]邮箱配置不存在，从数据库获取邮箱配置");
			emailConfig=resetEmailConfig();
		}
		logger.info("[邮件服务]获取邮箱配置"+emailConfig.toString());
		return emailConfig;
	}
	
    public Map<String, String> getSystemConfigMap(){
    	Map<String, String> systemConfigs=new HashMap<>();
		EdiSysConfig paramConfig=new EdiSysConfig();
		paramConfig.setType("1");
		List<EdiSysConfig> ediSysConfigs=sysConfigMapper.getEdiSysConfigs(paramConfig);
		for(EdiSysConfig ediSysConfig:ediSysConfigs) {
			systemConfigs.put(ediSysConfig.getConfigKey(), ediSysConfig.getConfigValue());
		}
		return systemConfigs;
    }
	
}
