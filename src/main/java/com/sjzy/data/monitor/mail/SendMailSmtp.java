package com.sjzy.data.monitor.mail;

/**
 * Created by wpp on 2020/08/12.
 */

import com.sjzy.data.monitor.common.Constants;
import com.sjzy.data.monitor.db.DBMailHelper;
import com.sjzy.data.monitor.utils.PropertiesUtil;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SendMailSmtp {

    /**
     * 发送邮件
     * @param mailInfos             要发送的内容
     */
    public static void SendMail (String mailInfos,String path) {
        //配置信息
        Properties serverProp = DBMailHelper.init(path);

        //分别要发送到的邮箱列表，发送的邮箱以及密码
        List<String> toMailAddrs = new DBMailHelper().getToMailAddrs(path);
        String fromMailAddr = serverProp.getProperty("fromMailAddr");
        String fromMailAddrPw = serverProp.getProperty("fromMailAddrPw");

        Properties prop = new Properties();
        prop.setProperty("mail.smtp.host", serverProp.getProperty("mailSmtpHost"));
        prop.setProperty("mail.smtp.port", serverProp.getProperty("mailSmtpPort"));
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        //获取Session对象
        Session s = Session.getDefaultInstance(prop, new MyAuthenticator(fromMailAddr,fromMailAddrPw));
        //设置session的调试模式，发布时取消
        s.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(s);
        try {
            mimeMessage.setFrom(new InternetAddress(fromMailAddr, Constants.EMAIL_PERSONAL));

            for ( int i = 0; i < toMailAddrs.size(); i++ ){
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMailAddrs.get(i)));
            }
            //设置主题
            mimeMessage.setSubject(Constants.EMAIL_TOPIC);
            mimeMessage.setSentDate(new Date());
            //设置内容
            mimeMessage.setText(mailInfos);
            mimeMessage.saveChanges();
            //发送
            Transport.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 继承Authenticator
     */
    public static class MyAuthenticator extends Authenticator {
        String fromMailAddr = null;
        String fromMailAddrPw = null;
        public MyAuthenticator(String fromMailAddr,String fromMailAddrPw){
            this.fromMailAddr = fromMailAddr;
            this.fromMailAddrPw = fromMailAddrPw;
        }
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            PasswordAuthentication pa = new PasswordAuthentication(fromMailAddr, fromMailAddrPw);
            return pa;
        }
    }

}

