package com.cellmania.cmreports.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.cellmania.cmreports.db.request.MailDTO;

public class MailAgent {
	private static Logger log = Logger.getLogger(MailAgent.class); 
	
	private static String _REPORT_DIR = null;
	private static String _MAIL_SMTP = null; 
	private static String _SERVER_URL = null;

	private static String _MAIL_FROM_ADDRESS;
	private static String _MAIL_DEFAULT_HEADER;
	private static String _MAIL_FROM_NAME;
	private static String _MAIL_FILEDOWNLOAD_MSG;
	private static String _MAIL_DEFAULT_FOOTER;
	
	private String smtpHost;
	private MailDTO mailDto;
	private String fileName;
	private long exeId;

	
	public MailAgent() {
		
	}
	
	public MailAgent(MailDTO mailDto) {
		this.mailDto = mailDto;
	}
	
	public MailAgent(String smtpHost) {
		super();
		this.smtpHost = smtpHost;
	}
	
	static {
		reloadStaticConfig();
	}
	
	public static void reloadStaticConfig(){
		try{
			_SERVER_URL = CMDBService.getServerSettingsValue(ServerSettingsConstants._SERVER_URL);
			_MAIL_SMTP = CMDBService.getServerSettingsValue(ServerSettingsConstants._MAIL_SMTP_HOST);
			_REPORT_DIR = CMDBService.getServerSettingsValue(ServerSettingsConstants._REPORTS_DIR_PATH);
			_MAIL_FROM_ADDRESS = CMDBService.getServerSettingsValue(ServerSettingsConstants._MAIL_FROM_ADDRESS);
			_MAIL_FROM_NAME = CMDBService.getServerSettingsValue(ServerSettingsConstants._MAIL_FROM_NAME);
			_MAIL_DEFAULT_HEADER = CMDBService.getServerSettingsValue(ServerSettingsConstants._MAIL_DEFAULT_HEADER);
			_MAIL_FILEDOWNLOAD_MSG = CMDBService.getServerSettingsValue(ServerSettingsConstants._MAIL_FILEDOWNLOAD_MSG);
			_MAIL_DEFAULT_FOOTER = CMDBService.getServerSettingsValue(ServerSettingsConstants._MAIL_DEFAULT_FOOTER);
		} catch(Exception e){
			log.error("Error initializing report direcory variable",e);
		}
		if(null != _REPORT_DIR){
			if(!_REPORT_DIR.endsWith(File.separator))
				_REPORT_DIR += File.separator;
		} else {
			_REPORT_DIR = System.getProperty("java.io.temp");
		}
	}

	private InternetAddress[] getEmailList(String address) {
		if(address!=null && address.contains(",")){
			address = address.replace(",", ";");
		}
		StringTokenizer st = null;
		//if(address.indexOf(";")>0)
			st = new StringTokenizer(address,";");
		
		//System.out.println("ST TOKENS :"+st.countTokens());
 
		InternetAddress[] addressTo = new InternetAddress[st.countTokens()]; 
		int i = 0;
		 while (st.hasMoreTokens()) {
			 String to = st.nextToken();
			if(null!=to && !"".equals(to))
				try {
					addressTo[i++] = new InternetAddress(to);
				} catch (AddressException e) {
					log.error("INVALID TO ADDRESS : "+to,e);
					i--;
				}
		}
		return addressTo;
	}
	
	
	public long getExeId() {
		return exeId;
	}

	public void setExeId(long exeId) {
		this.exeId = exeId;
	}

	public boolean send() throws Exception {
		boolean bSuccess = true;
		Properties props = new Properties();
	    props.put("mail.smtp.host",_MAIL_SMTP); // TODO: get From Server Settings
	    
	    
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(false);
	    
	    MimeMessage mimeMsg = new MimeMessage(session);
	    if(mailDto.getFromAddress()!=null ){
	    	mimeMsg.setFrom(new InternetAddress(mailDto.getFromAddress(),mailDto.getFromName()));
	    } else {
	    	mimeMsg.setFrom(new InternetAddress(_MAIL_FROM_ADDRESS,_MAIL_FROM_NAME));
	    }
	    
	    if(mailDto.getReplyToAddress()!=null){
	    	InternetAddress[] replyTo = new InternetAddress[1];
	    	replyTo[0] = new InternetAddress(mailDto.getReplyToAddress(),mailDto.getFromName());
	    	mimeMsg.setReplyTo(replyTo);
	    }
	    
	    
	    mimeMsg.setRecipients(Message.RecipientType.TO, getEmailList(mailDto.getToAddress()));
	    if(mailDto.getCcAddress()!=null) {
	    	mimeMsg.setRecipients(Message.RecipientType.CC, getEmailList(mailDto.getCcAddress()));
	    }
	    mimeMsg.setSubject(mailDto.getSubject());
	    MimeMultipart multiPart = new MimeMultipart();
	    MimeBodyPart body = new MimeBodyPart();
	    StringBuffer mailBody = new StringBuffer();
	    mailBody.append("<html>")
	    .append("<head>")
	    .append("<link href=\""+_SERVER_URL+"includes/styles_cm_mail.css\" type=text/css rel=stylesheet>")
	    .append("</head>")
	    .append("<body>");
	    
	    if(mailDto.getBody()==null || mailDto.getBody().isEmpty()){
	    	mailBody.append(_MAIL_DEFAULT_HEADER);
	    } else {
	    	mailBody.append(mailDto.getBody());
	    }
	    
	    if(mailDto.getIncludeFileLink()){
	    	mailBody.append(_MAIL_FILEDOWNLOAD_MSG);
	    	mailBody.append("<a href=\"")
	    	.append(_SERVER_URL)
	    	.append("dlFile.do?id="+exeId+"\">"+fileName+"<a><br/>");
	    }
	    
	    if(mailDto.getIncludeDefaultFooter()) {
		    mailBody.append(_MAIL_DEFAULT_FOOTER);
	    }
	    mailBody.append("</body></html>");
	    body.setContent(mailBody.toString(),"text/html");
	    
	    multiPart.addBodyPart(body);
	    
	    if(mailDto.getAttachment()){
	    	 
		    body = new MimeBodyPart();
		    String zipFileName = null;
		    if(this.fileName.endsWith(".xlsx")){
		    	zipFileName = zipFile(this.fileName.replace(".xlsx", ".zip"),this.fileName);
		    } if(this.fileName.endsWith(".xls")){
		    	zipFileName = zipFile(this.fileName.replace(".xls", ".zip"),this.fileName);
		    } else if(this.fileName.endsWith(".csv")){
		    	zipFileName = zipFile(this.fileName.replace(".csv", ".zip"),this.fileName);
		    } else if(this.fileName.endsWith(".zip")){
		    	zipFileName = this.fileName;
		    }
		    
		    
		    File file= new File(_REPORT_DIR + zipFileName);
		    FileDataSource fileds = new FileDataSource(file);
		    body.setDataHandler(new DataHandler(fileds));
		    body.setFileName(zipFileName);
		    multiPart.addBodyPart(body);
			
	    } 
	    
	    mimeMsg.setContent(multiPart);
	    Transport.send(mimeMsg);	    
	    return bSuccess;
	}
	
	public boolean sendPasswordRecoveryMail() throws Exception{
		boolean bSuccess = true;
		Properties props = new Properties();
	    props.put("mail.smtp.host",_MAIL_SMTP); // TODO: get From Server Settings
	    
	    
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(false);
	    
	    MimeMessage mimeMsg = new MimeMessage(session);
	    if(mailDto.getFromAddress()!=null ){
	    	mimeMsg.setFrom(new InternetAddress(mailDto.getFromAddress(),mailDto.getFromName()));
	    } else {
	    	mimeMsg.setFrom(new InternetAddress(_MAIL_FROM_ADDRESS,_MAIL_FROM_NAME));
	    }
	    
	    if(mailDto.getReplyToAddress()!=null){
	    	InternetAddress[] replyTo = new InternetAddress[1];
	    	replyTo[0] = new InternetAddress(mailDto.getReplyToAddress(),mailDto.getFromName());
	    	mimeMsg.setReplyTo(replyTo);
	    }
	    
	    
	    mimeMsg.setRecipients(Message.RecipientType.TO, getEmailList(mailDto.getToAddress()));
	    if(mailDto.getCcAddress()!=null) {
	    	mimeMsg.setRecipients(Message.RecipientType.CC, getEmailList(mailDto.getToAddress()));
	    }
	    mimeMsg.setSubject(mailDto.getSubject());
	    MimeMultipart multiPart = new MimeMultipart();
	    MimeBodyPart body = new MimeBodyPart();
	    
	    
	    if(mailDto.getIncludeDefaultFooter()) {
		    body.setContent(mailDto.getBody() + _MAIL_DEFAULT_FOOTER, "text/html");
	    } else {
	    	body.setContent(mailDto.getBody(),"text/html");
	    }
	 
	    multiPart.addBodyPart(body);
	    mimeMsg.setContent(multiPart);
	    Transport.send(mimeMsg);
		return bSuccess;
	}
	
	public boolean sendNewRegister() throws Exception{
		boolean bSuccess = true;
		Properties props = new Properties();
	    props.put("mail.smtp.host",_MAIL_SMTP); // TODO: get From Server Settings
	    
	    
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(false);
	    
	    MimeMessage mimeMsg = new MimeMessage(session);
	    if(mailDto.getFromAddress()!=null ){
	    	mimeMsg.setFrom(new InternetAddress(mailDto.getFromAddress(),mailDto.getFromName()));
	    } else {
	    	mimeMsg.setFrom(new InternetAddress(_MAIL_FROM_ADDRESS,_MAIL_FROM_NAME));
	    }
	    
	    if(mailDto.getReplyToAddress()!=null){
	    	InternetAddress[] replyTo = new InternetAddress[1];
	    	replyTo[0] = new InternetAddress(mailDto.getReplyToAddress(),mailDto.getFromName());
	    	mimeMsg.setReplyTo(replyTo);
	    }
	    
	    
	    mimeMsg.setRecipients(Message.RecipientType.TO, getEmailList(mailDto.getToAddress()));
	    if(mailDto.getCcAddress()!=null) {
	    	mimeMsg.setRecipients(Message.RecipientType.CC, getEmailList(mailDto.getToAddress()));
	    }
	    mimeMsg.setSubject(mailDto.getSubject());
	    MimeMultipart multiPart = new MimeMultipart();
	    MimeBodyPart body = new MimeBodyPart();
	    StringBuffer mailBody = new StringBuffer();
	    mailBody.append("<html>")
	    .append("<head>")
	    .append("<link href=\""+_SERVER_URL+"includes/styles_cm_mail.css\" type=text/css rel=stylesheet>")
	    .append("</head>")
	    .append("<body>");
	    
	    mailBody.append(mailDto.getBody());
	   	mailBody.append(_MAIL_DEFAULT_FOOTER);
	   	
	   	mailBody.append("</body></html>");
	    body.setContent(mailBody.toString(),"text/html");
	    
	    multiPart.addBodyPart(body);
	    mimeMsg.setContent(multiPart);
	    Transport.send(mimeMsg);
		return bSuccess;
	}
	
	private String zipFile(String p_szZipFileName, String rptFileName)
			throws Exception {
		File rptFile = new File(_REPORT_DIR + rptFileName);
		File zipFile = new File(_REPORT_DIR + p_szZipFileName);
		log.debug("ZIP DIR : " + _REPORT_DIR);
		int bytesIn = 0;
		byte[] readBuffer = new byte[2156];
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

		FileInputStream fis = new FileInputStream(rptFile);
		ZipEntry anEntry = new ZipEntry(rptFile.getName());
		out.putNextEntry(anEntry);
		while ((bytesIn = fis.read(readBuffer)) != -1) {
			out.write(readBuffer, 0, bytesIn);
		}
		fis.close();
		out.close();
		log.debug("ZIP FILE NAME : " + zipFile.getAbsolutePath());
		if (rptFile.exists())
			rptFile.delete(); // Delete the original File as its Zip is
								// available
		return zipFile.getName();

	}
	
	public void zipDir(String dir2zip, ZipOutputStream zos) { 
	    try {
	    	File zipDir = new File(dir2zip); 
	    	String[] dirList = zipDir.list(); 
	        byte[] readBuffer = new byte[2156]; 
	        int bytesIn = 0; 
	         
	        for(int i=0; i<dirList.length; i++) { 
	            File f = new File(zipDir, dirList[i]); 
	            if(f.isDirectory()){  
		            String filePath = f.getPath(); 
		            zipDir(filePath, zos); 
		            continue; 
		        }
	            FileInputStream fis = new FileInputStream(f); 
	            log.debug("Path : "+f.getAbsolutePath());
	            ZipEntry anEntry = new ZipEntry(f.getName()); 
	            zos.putNextEntry(anEntry); 
	            while((bytesIn = fis.read(readBuffer)) != -1) { 
	                zos.write(readBuffer, 0, bytesIn); 
	            } 
	           fis.close(); 
	        } 
	    } catch(Exception e) {
	    	log.error("Error : "+e.getLocalizedMessage(),e);
	    }
	}
	
	public String getSmtpHost() {
		return smtpHost;
	}
	
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public static void main(String[] arg){
		String str = "abakle@cellmania.com, abakle@cellmania.com";
		MailAgent m = new MailAgent();
		m.getEmailList(str);
		
	}
}
