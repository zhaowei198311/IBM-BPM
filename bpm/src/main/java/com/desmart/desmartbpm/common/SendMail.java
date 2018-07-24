package com.desmart.desmartbpm.common;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
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
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.desmart.desmartportal.entity.DhInstanceDocument;

/**
 * 邮件发送工具类
 */
public class SendMail {
    private static Logger logger = LoggerFactory.getLogger(SendMail.class);

   /* public static void main(String[] args) {
    	SendMail sendMail = SendMail.getInstaance();
    	List<String> toList = new ArrayList<>();
    	toList.add("helloSSM@163.com");
    	toList.add("1254431331@qq.com");
    	sendMail.send("测试邮件主题","测试邮件内容",toList);
    }*/
    
    private static String host;//SMTP服务器
    private static String user;// 用户名
    private static String password;// 密码
    private static String from;// 发件人邮箱
    private static String port;//SMTP服务器端口
    private static String protocol;// 发送邮件协议  
    
    private static SendMail sendMail = null;
    /**
     * 加载配置文件，初始化工具类
     */
    static {
    	  ResourceBundle resource = ResourceBundle.getBundle("mailServer");
    	  host = resource.getString("host"); 
    	  user = resource.getString("user"); 
    	  password = resource.getString("password"); 
    	  from = resource.getString("from"); 
    	  port = resource.getString("port");
    	  protocol = resource.getString("protocol");
    }
    
    /**
     * 获取工具类实例
     * @return
     */
    public static SendMail getInstaance() {
    	if(sendMail==null) {
    		sendMail = new SendMail();
    	}
    	return sendMail;
    }

    /**	
     * 	发送邮件
     * @param subject
     *   标题
     * @param content
     *   邮件内容
     * @param emailset
     *   收件人邮箱
     */
    public static boolean send(String subject, String content,List<String> emailset) {
        try {
            Properties properties = new Properties();
            //设置邮件服务器
            properties.setProperty("mail.smtp.host", host);  
            //properties.setProperty("mail.smtp.port", port);  
            properties.setProperty("mail.transport.protocol", protocol);
            properties.setProperty("mail.debug", "true");//设置debug模式  
            Session ssn = null;
            if(!"".equals(user)&&!"".equals(password)) {
                //使用SSL
                //开启安全协议
                /*MailSSLSocketFactory sf = new MailSSLSocketFactory();  
                sf.setTrustAllHosts(true);  
                properties.setProperty("mail.smtp.ssl.enable", "true");
                properties.setProperty("mail.smtp.ssl.socketFactory", sf);*/
            	
            	properties.setProperty("mail.smtp.auth", "true");// 需要验证  
                ssn = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });
            }else {
            	// 获取默认的 Session 对象。
                ssn = Session.getDefaultInstance(properties);
			}
            //设置session的调试模式，发布时取消
            //ssn.setDebug(true);

            //由邮件会话新建一个消息对象
            MimeMessage message = new MimeMessage(ssn);

            //发件人的邮件地址
            InternetAddress fromAddress = new InternetAddress(from);

            //设置发件人
            message.setFrom(fromAddress);

            InternetAddress[] sendTo = new InternetAddress[emailset.size()];
            int i = 0;
            for (String email:emailset) {
                sendTo[i] = new InternetAddress(email);
                i++;
            }
            message.setRecipients(Message.RecipientType.TO,sendTo);

            //设置标题
            message.setSubject(subject);

            Multipart mainPart = new MimeMultipart();
            //创建一个包含HTML内容的MimeBodyPart
            BodyPart body=new MimeBodyPart();
            
            //设置html内容
            try {
                body.setContent(content,"text/html;charset=utf-8");
                //将MimeMultipart设置为邮件内容
                mainPart.addBodyPart(body);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            //设置内容
//            message.setText(content);
            message.setContent(mainPart);
            //设置发信时间
            message.setSentDate(new Date());

            Transport transport = ssn.getTransport();
            transport.connect(host,user,password);
            transport.sendMessage(message, message
                    .getRecipients(Message.RecipientType.TO));
            transport.close();
            logger.error("邮件发送成功");
            return true;
        } catch (Exception e) {
            logger.warn("邮件发送失败", e);
            System.out.print("err"+e.getMessage());
            return false;
        }
    }

    /*附件发送*/
    public static void sendWithAccessory(List<String> to,DhInstanceDocument reportfile) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
//            props.setProperty("mail.smtp.port", "25");
            Session ssn = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            MimeMessage message = new MimeMessage(ssn);
            //由邮件会话新建一个消息对象
            InternetAddress fromAddress = new InternetAddress(from);
            //发件人的邮件地址
            message.setFrom(fromAddress);
            InternetAddress[] address = null;
            List<InternetAddress> addressList = new ArrayList<InternetAddress>();
            for (int i = 0; i < to.size(); i++){
                addressList.add(new InternetAddress(to.get(i)));
            }
            address = (InternetAddress[])addressList.toArray(new InternetAddress[addressList.size()]);
            message.addRecipients(javax.mail.Message.RecipientType.TO, address);
            //设置收件人
            message.setSubject(reportfile.getAppDocFileName());
            //设置主题

//            邮件附件发送
            // 设置邮件附件--start
            Multipart msgPart = new MimeMultipart();
            MimeBodyPart body = new MimeBodyPart();   // 正文
            MimeBodyPart attach = new MimeBodyPart(); // 附件
            // 设置正文内容
            body.setContent(reportfile.getAppDocFileName()+"报表", "text/html;charset=utf-8");
            // 设置附件内容
            attach.setDataHandler(new DataHandler(new FileDataSource(reportfile.getAppDocFileUrl())));
            attach.setFileName(MimeUtility.encodeText(reportfile.getAppDocFileName()+".xlsx"));
            msgPart.addBodyPart(body);
            msgPart.addBodyPart(attach);
            message.setContent(msgPart);
            //设置邮件附件--end

            message.setSentDate(new Date());
            //设置发信时间
            Transport transport = ssn.getTransport("smtp");
            transport.connect(host, user, password);
            transport.sendMessage(message, message
                    .getRecipients(Message.RecipientType.TO));
            transport.close();
            logger.error("邮件发送成功");
        } catch (Exception e) {
            logger.warn("邮件发送失败", e);
        }
    }
}
