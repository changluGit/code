﻿package com.six.service;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class SendMail {
 //若发件人邮箱不固定，此处的三个参数可写成set方法形式进行赋值
 private String sender = "lilpjob@gmail.com";// 发件人邮箱地址
 private String password = "lilepinglilpjob";// 发件人邮箱密码
 private String smtpHost = "smtp.gmail.com";// 邮件发送服务器（smtp）比如163就是smtp.163.com
 /**
  * 使用smtp发送邮件 主程序
  *
  * @param receiver
  *            收件人地址
  * @param subject
  *            邮件主题
  * @param content
  *            邮件内容
  * @throws MessagingException
  *             mail发送失败
  */
 private void smtp(String receiver, String subject, String content)
   throws MessagingException {

  if (smtpHost == null || smtpHost.trim().equals(""))
   throw new MessagingException("smtpHost   not   found ");
  if (sender == null || sender.trim().equals(""))
   throw new MessagingException("user   not   found ");
  if (password == null || password.trim().equals(""))
   throw new MessagingException("password   not   found ");
  Properties properties = new Properties(); 
  properties.put("mail.smtp.host", smtpHost);// 设置smtp主机
  properties.put("mail.smtp.auth", "true");// 使用smtp身份验证
  properties.put("mail.smtp.starttls.enable","true");

  Session session = Session.getInstance(properties, new Authenticator() {
   public PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(sender, password);
   }
  });
  // 获得邮件会话对象 
  MimeMessage mimeMsg = new MimeMessage(session);// 创建MIME邮件对象
  // 设置发件人地址
  mimeMsg.setFrom(new InternetAddress(sender));
  // 设置收件人地址
  mimeMsg.setRecipients(Message.RecipientType.TO, receiver);
  // 设置邮件主题
  mimeMsg.setSubject(subject, "UTF-8");
  //mail内容部分
  MimeBodyPart part = new MimeBodyPart();
  part.setText(content.toString(), "UTF-8 ");
  // 设置邮件格式为html
  part.setContent(content.toString(), "text/html;charset=UTF-8 ");
  Multipart multipart = new MimeMultipart();
  multipart.addBodyPart(part);// 在 Multipart 中增加mail内容部分
  mimeMsg.setContent(multipart);// 增加 Multipart 到信息体
  mimeMsg.setSentDate(new Date());// 设置发送日期
  Transport.send(mimeMsg);// 发送邮件
 }
 /**
  * 发送邮件
  * 
  * @param mailAddress
  *            收件人地址
  * @param subject
  *            邮件主题
  * @param content
  *            邮件内容
  * @return Boolean true 发送成功 false 发送失败
  */
 public boolean sendMails(String mailAddress, String subject, String content) {
  if (mailAddress == null || subject == null || content == null
    || mailAddress.trim().equals("") || subject.trim().equals("")
    || content.equals("")) {
   return false;
  }
  try {
   this.smtp(mailAddress, subject, content);
  } catch (Exception ex) {
   System.out.println(" Sendmail ERROR: " + ex.toString());
   return false;
  }
  return true;
 }
 

}
