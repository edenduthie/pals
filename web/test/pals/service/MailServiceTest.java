package pals.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.service.MailService;

public class MailServiceTest extends BaseTest
{
    @Autowired MailService mailService;
    
    @Test
    public void checkConfig()
    {
    	Assert.assertNotNull(mailService.getSmtpServer());
    }
    
    @Test
    public void sendMessage() throws AddressException, MessagingException
    {
    	mailService.sendMessage("eduthie@gmail.com","Subject","Content");
    }
}
