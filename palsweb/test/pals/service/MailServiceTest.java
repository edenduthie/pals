package pals.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import pals.BaseTest;

public class MailServiceTest extends BaseTest
{
   @Autowired
   MailService mailService;
   
   String email = "palstester@gmail.com";
   
   @Test
   public void sendEmail() throws AddressException, MessagingException
   {
	   mailService.sendMessage(email,"Email subject","Test message");
   }
}
