package pals.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import pals.Configuration;

import com.sun.mail.smtp.SMTPTransport;

public class MailService 
{
	Properties props;
	String smtpServer;
	String username;
	String password;
	String from;
	Integer port;
	
	private static final Logger log = Logger.getLogger(MailService.class);
	
	public MailService()
	{
		smtpServer = Configuration.getInstance().SMTP_SERVER;
		port = Configuration.getInstance().SMTP_PORT;
		username = Configuration.getInstance().SMTP_USERNAME;
		password = Configuration.getInstance().SMTP_PASSWORD;
		from = Configuration.getInstance().FROM_EMAIL;
		props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", port.toString() );
		props.put("mail.smtp.starttls.enable","true");
	}
	
    public void sendMessage(String to, String subject, String text) throws AddressException, MessagingException
    {
    	Session session = Session.getInstance(props, null);
    	Message msg = new MimeMessage(session);
    	msg.setFrom(new InternetAddress(from));
    	msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
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
}
