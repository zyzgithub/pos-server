package com.dianba.pos.common.util;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
@SuppressWarnings("all")
public class EMailClient {
    //定义发件人、收件人、SMTP服务器、用户名、密码、主题、内容等
    private String from = "pengyuanyuan@0085.com";
    private String smtpServer = "smtp.ym.163.com";
    private String username = "pengyuanyuan@0085.com";
    private String password = "pyy0101";
    private boolean ifAuth; //服务器是否要身份认证

    private String displayName;
    private String subject;
    private String to;
    private String content;
    private Map<String, byte[]> files = new HashMap<String, byte[]>(); //用于保存发送附件的文件名的集合

    /**
     * 设置SMTP服务器地址
     */
    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    /**
     * 设置发件人的地址
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 设置显示的名称
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 设置服务器是否需要身份认证
     */
    public void setIfAuth(boolean ifAuth) {
        this.ifAuth = ifAuth;
    }

    /**
     * 设置E-mail用户名
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * 设置E-mail密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置接收者
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * 设置主题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 设置主体内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 该方法用于收集附件名
     */
    public void addAttachfile(String fname,byte[] bs) {
        files.put(fname,bs);
    }

    public EMailClient() {

    }

    public static void main(String[] s) throws IOException {
        EMailClient mail = new EMailClient("zhoucong@0085.com", "2016年11月25日景秀银湾店POS导出商品销售报表", "2016年11月25日景秀银湾店POS导出商品销售报表", "POS导出销售报表附件已发送，请查看！");
//        EMailClient mail = new EMailClient("pengyuanyuan@0085.com", "POS 报表", "报表详情", "是短发是短发");
        String fileName = "/Users/zc/Desktop/SalesReport.xlsx";
        FileInputStream is = new FileInputStream(fileName);
        byte [] bs = new byte[(int)is.available()];
        is.read(bs);
        mail.addAttachfile("销售报表_景秀银湾店2016_11_25.xlsx", bs);
        mail.send();

    }

    /**
     * 初始化SMTP服务器地址、发送者E-mail地址、用户名、密码、接收者、主题、内容
     */
    public EMailClient(String smtpServer, String from, String displayName, String username, String password, String to, String subject, String content) {
        this.smtpServer = smtpServer;
        this.from = from;
        this.displayName = displayName;
        this.ifAuth = true;
        this.username = username;
        this.password = password;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 初始化SMTP服务器地址、发送者E-mail地址、用户名、密码、接收者、主题、内容
     */
    public EMailClient(String to, String displayName, String subject, String content) {
        this.displayName = displayName;
        this.ifAuth = true;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 初始化SMTP服务器地址、发送者E-mail地址、接收者、主题、内容
     */
    public EMailClient(String smtpServer, String from, String displayName, String to, String subject, String content) {
        this.smtpServer = smtpServer;
        this.from = from;
        this.displayName = displayName;
        this.ifAuth = false;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 发送邮件
     */
    public HashMap send() {
        HashMap map = new HashMap();
        map.put("state", "success");
        String message = "邮件发送成功！";
        Session session = null;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpServer);
        if (ifAuth) { //服务器需要身份认证
            props.put("mail.smtp.auth", "true");
            SmtpAuth smtpAuth = new SmtpAuth(username, password);
            session = Session.getDefaultInstance(props, smtpAuth);
        } else {
            props.put("mail.smtp.auth", "false");
            session = Session.getDefaultInstance(props, null);
        }
        session.setDebug(true);
        Transport trans = null;
        try {
            Message msg = new MimeMessage(session);
            try {
                Address from_address = new InternetAddress(from, displayName);
                msg.setFrom(from_address);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(content.toString(), "text/html;charset=gb2312");
            mp.addBodyPart(mbp);
            if (!files.isEmpty()) {//有附件
                for (String key : files.keySet()) {
                    mbp = new MimeBodyPart();
//                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(files.get(key));
                    ByteArrayDataSource bds = new ByteArrayDataSource(files.get(key), "application/octet-stream");
                    mbp.setDataHandler(new DataHandler(bds)); //得到附件本身并至入BodyPart
                    try {
                        mbp.setFileName(MimeUtility.encodeWord(key));  //得到文件名同样至入BodyPart
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        mbp.setFileName("report.xlsx");
                    }
                    mp.addBodyPart(mbp);
                }
            }
            msg.setContent(mp); //Multipart加入到信件  
            msg.setSentDate(new Date());     //设置信件头的发送日期  
            //发送信件  
            msg.saveChanges();
            trans = session.getTransport("smtp");
            trans.connect(smtpServer, username, password);
            trans.sendMessage(msg, msg.getAllRecipients());
            trans.close();

        } catch (AuthenticationFailedException e) {
            map.put("state", "failed");
            message = "邮件发送失败！错误原因：\n" + "身份验证错误!";
            e.printStackTrace();
        } catch (MessagingException e) {
            message = "邮件发送失败！错误原因：\n" + e.getMessage();
            map.put("state", "failed");
            e.printStackTrace();
            Exception ex = null;
            if ((ex = e.getNextException()) != null) {
                System.out.println(ex.toString());
                ex.printStackTrace();
            }
        }
        //System.out.println("\n提示信息:"+message);  
        map.put("message", message);
        return map;
    }


}