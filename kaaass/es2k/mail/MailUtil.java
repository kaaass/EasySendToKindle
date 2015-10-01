package kaaass.es2k.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import kaaass.es2k.Main;
import kaaass.es2k.crashreport.ErrorUtil;
import kaaass.es2k.file.ZipCompressor;

public class MailUtil {
	private MimeMessage message;
	private Session session;
	private Transport transport;

	private String mailHost = "";
	private String senderUsername = "";
	private String senderPassword = "";
	private String kindleMail = "";

	private Properties properties = new Properties();

	public MailUtil(boolean debug) {
		InputStream in;
		this.message = new MimeMessage(session);
		try {
			in = getInputStream(new FileInputStream(new File("mail.properties")));
			properties.load(in);
			this.mailHost = properties.getProperty("mail.smtp.host");
			this.senderUsername = properties.getProperty("mail.sender.username");
			this.senderPassword = properties.getProperty("mail.sender.password");
			this.kindleMail = properties.getProperty("es2k.mail.username");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.session = Session.getInstance(this.properties);
		this.session.setDebug(debug);
		if (Main.otherM) {
			this.mailHost = "smtp.sina.com";
			this.senderUsername = "es2kindle@sina.com";
			this.senderPassword = "es2kindle";
		}
	}
	
	public Result sendHtmlEmail(String subject, String sendHtml) {
		try {
			InternetAddress from = new InternetAddress(senderUsername);
			InternetAddress to = new InternetAddress(kindleMail);
			Multipart multipart = new MimeMultipart();
			BodyPart contentPart = new MimeBodyPart();
			
			message.setFrom(from);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject(subject);
			contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
			multipart.addBodyPart(contentPart);
			message.setContent(multipart);
			message.saveChanges();
			transport = session.getTransport("smtp");
			transport.connect(mailHost, senderUsername, senderPassword);
			transport.sendMessage(message, message.getAllRecipients());
			return new Result();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(e);
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
					return new Result(e);
				}
			}
		}
	}
	
	public class Result {
		boolean isSuccess = false;
		public Exception e = null;
		
		public Result(){
			this.isSuccess = true;
		}
		
		public Result(Exception e){
			this.isSuccess = false;
			this.e = e;
		}
		
		public Result(boolean warn, Exception e){
			this.isSuccess = true;
			if(warn){
				this.e = e;
			}
		}
		
		public boolean isSuccess(){
			return this.isSuccess; 
		}
		
		public String getError(){
			if (this.e == null) {
				return ""; 
			} else {
				return ErrorUtil.getCauseStackString(this.e);
			}
		}
	}
	
	public Result send(String subject, String content, String fileList[]) {
		try {
			session = Session.getInstance(properties);
			Message msg = new MimeMessage(session);
			BodyPart messagePart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			msg.setFrom(new InternetAddress(senderUsername));
			
			msg.setRecipient(Message.RecipientType.TO, (new InternetAddress(kindleMail)));
			
			msg.setSentDate(new Date());
			msg.setSubject(subject);
			messagePart.setContent(content, "text/html;charset=UTF-8");
			multipart.addBodyPart(messagePart);
			
			if (fileList != null) {
				addTach(fileList, multipart);
			}

			msg.setContent(multipart);
			Transport tran = session.getTransport("smtp");
			tran.connect(mailHost, senderUsername, senderPassword);
			tran.sendMessage(msg, msg.getAllRecipients());
			return new Result();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(e);
		}
	}
	
	private Result sendToDev(String subject, String content, String fileList[]) {
		try {
			session = Session.getInstance(properties);
			Message msg = new MimeMessage(session);
			BodyPart messagePart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			msg.setFrom(new InternetAddress("es2kindle@sina.com"));
			
			msg.setRecipient(Message.RecipientType.TO, (new InternetAddress("1029089048@qq.com")));
			
			msg.setSentDate(new Date());
			msg.setSubject(subject);
			messagePart.setContent(content, "text/html;charset=GBK");
			multipart.addBodyPart(messagePart);
			
			if (fileList != null) {
				addTach(fileList, multipart);
			}

			msg.setContent(multipart);
			Transport tran = session.getTransport("smtp");
			tran.connect("smtp.sina.com", "es2kindle@sina.com", "es2kindle");
			tran.sendMessage(msg, msg.getAllRecipients());
			
			return new Result();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(e);
		}
	}
	
	public void addTach(String fileList[], Multipart multipart)
			throws MessagingException, UnsupportedEncodingException {
		for (int i = 0; i < fileList.length; i++) {
			MimeBodyPart mailArchieve = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(fileList[i]);
			mailArchieve.setDataHandler(new DataHandler(fds));
			mailArchieve.setFileName(MimeUtility.encodeText(fds.getName(), "utf-8", null));
			multipart.addBodyPart(mailArchieve);
		}
	}
	
	public static InputStream getInputStream(FileInputStream fileInput) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = -1;
		InputStream inputStream = null;
		try {
			while ((n=fileInput.read(buffer)) != -1) {
				baos.write(buffer, 0, n);
			}
			byte[] byteArray = baos.toByteArray();
			inputStream = new ByteArrayInputStream(byteArray);
			return inputStream;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Result sendDebugInfo(String author, String address, String discribe){
		ZipCompressor zc = new ZipCompressor("crashreport.zip");
		try {
			zc.compress("CrashReport"); 
		} catch (RuntimeException e) {
			e.printStackTrace();
			(new ErrorUtil(e)).dealWithException();
		}
		File file = new File("crashreport.zip");
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("<p>------------- 用户反馈 -------------\n</p>");
		stringbuilder.append("<p>反馈人：\n\n</p>");
		stringbuilder.append(author);
		stringbuilder.append("<p>\n邮箱：\n\n</p>");
		stringbuilder.append(address);
		stringbuilder.append("<p>\n描述:\n\n</p>");
		stringbuilder.append(discribe);
		String[] fileList = {"crashreport.zip"};
		Result r = sendToDev(author + " 反馈了ES2Kindle的一个bug", stringbuilder.toString(), fileList);
		file.delete();
		return r;
	}
}
