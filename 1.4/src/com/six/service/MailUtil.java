package com.six.service;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtil {
	public static void sendmail(String to_mail_address,String subject,String context) {
		try{
			String userName="842221425@qq.com";
			//String userName="changlu@sf-express.com";
			String password="842221425(cl)";
			//String password="842221425@cl";
			String smtp_server="smtp.qq.com";
			//String smtp_server="smtp.sf-express.com";
			String from_mail_address=userName;
			//String to_mail_address="changlu@sf-express.com";
			//String to_mail_address="zhaoyongyang@sf-express.com";
			Authenticator auth=new PopupAuthenticator(userName,password);
			Properties mailProps=new Properties();
			mailProps.put("mail.smtp.host", smtp_server);
			mailProps.put("mail.smtp.auth", "true");
			mailProps.put("username", userName);
			mailProps.put("password", password);
			
			Session mailSession=Session.getDefaultInstance(mailProps, auth);
			mailSession.setDebug(true);
			MimeMessage message=new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(from_mail_address));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to_mail_address));
			message.setSubject(subject);
			
			MimeMultipart multi=new MimeMultipart();
			BodyPart textBodyPart=new MimeBodyPart();
			textBodyPart.setText(context);
			
			multi.addBodyPart(textBodyPart);
			message.setContent(multi);
			message.saveChanges();
			Transport.send(message);
		}catch(Exception ex){
			System.err.println("邮件发送失败的原因是："+ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}
}
class PopupAuthenticator extends Authenticator{
	private String username;
	private String password;
	public PopupAuthenticator(String username,String pwd){
	this.username=username;
	this.password=pwd;
	}
		public PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication(this.username,this.password);
	}
}