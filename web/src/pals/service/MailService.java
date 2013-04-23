package pals.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

@Service
public class MailService 
{
	
	Properties props;
	
	public String smtpServer;
	public String username;
	public String password;
	public String from;
	public String bcc;
	public Integer port;
	public String host;
	public String auth;
	public String tls;
	public String ssl;
	public String baseUrl;
	
	private static final Logger log = Logger.getLogger(MailService.class);
	
	public void createProperties()
	{
		if( props == null )
		{
			props = new Properties();
			props.setProperty("mail.smtp.host", smtpServer);
			props.setProperty("mail.smtp.auth", auth);
			props.setProperty("mail.smtp.port", port.toString() );
			props.setProperty("mail.smtp.starttls.enable",tls);
			props.setProperty("mail.smtp.ssl.enable",ssl);
			
			log.debug("Setting up mail service");
			log.debug("mail.smtp.host: " + props.getProperty("mail.smtp.host"));
			log.debug("mail.smtp.auth: " + props.getProperty("mail.smtp.auth"));
			log.debug("mail.smtp.port: " + props.getProperty("mail.smtp.port"));
			log.debug("mail.smtp.starttls.enable: " + props.getProperty("mail.smtp.starttls.enable"));
		}
	}

	
    public void sendMessage(String to, String subject, String text) throws AddressException, MessagingException
    {
    	createProperties();
    	Session session = Session.getInstance(props, null);
    	Message msg = new MimeMessage(session);
    	Address[] replyToList = new Address[1];
    	replyToList[0] = new InternetAddress(from);
    	log.debug("Mail reply to: " + from);
    	msg.setReplyTo(replyToList);
    	msg.setFrom(new InternetAddress(host));
    	log.debug("Mail from: " + host);
    	log.debug("Recipient: " + to);
    	msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
    	//msg.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(bcc, false));
    	msg.setSubject(subject);
    	msg.setText(text);
    	
    	SMTPTransport t = (SMTPTransport)session.getTransport("smtp");
    	try
    	{
    	    t.connect(smtpServer, username, password);
    	    t.sendMessage(msg, msg.getAllRecipients());
    	}
    	finally
    	{
    		log.info("Mail server response: " + t.getLastServerResponse());
    		t.close();
    	}
    }
    
    public void sendMessageHTML(String to, String subject, String text) throws AddressException, MessagingException
    {
    	createProperties();
    	Session session = Session.getInstance(props, null);
    	Message msg = new MimeMessage(session);
    	Address[] replyToList = new Address[1];
    	replyToList[0] = new InternetAddress(from);
    	log.debug("Mail reply to: " + from);
    	msg.setReplyTo(replyToList);
    	msg.setFrom(new InternetAddress(host));
    	log.debug("Mail from: " + host);
    	log.debug("Recipient: " + to);
    	msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
    	//msg.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(bcc, false));
    	msg.setSubject(subject);
    	msg.setContent(text, "text/html; charset=utf-8");
    	
    	SMTPTransport t = (SMTPTransport)session.getTransport("smtp");
    	try
    	{
    	    t.connect(smtpServer, username, password);
    	    t.sendMessage(msg, msg.getAllRecipients());
    	}
    	finally
    	{
    		log.info("Mail server response: " + t.getLastServerResponse());
    		t.close();
    	}
    }


	public String getSmtpServer() {
		return smtpServer;
	}


	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getBcc() {
		return bcc;
	}


	public void setBcc(String bcc) {
		this.bcc = bcc;
	}


	public Integer getPort() {
		return port;
	}


	public void setPort(Integer port) {
		this.port = port;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public String getAuth() {
		return auth;
	}


	public void setAuth(String auth) {
		this.auth = auth;
	}


	public String getTls() {
		return tls;
	}


	public void setTls(String tls) {
		this.tls = tls;
	}


	public String getSsl() {
		return ssl;
	}


	public void setSsl(String ssl) {
		this.ssl = ssl;
	}


	public String getBaseUrl() {
		return baseUrl;
	}


	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}