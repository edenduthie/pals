package pals.service;

import java.security.NoSuchAlgorithmException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Configuration;
import pals.dao.UserDAO;
import pals.entity.TestEntityFactory;
import pals.entity.User;

public class AccountServiceTest extends BaseTest
{	
    @Autowired
    AccountService accountService;
    @Autowired
    UserDAO userDAO;
    
    @Test
    public void emailNewPassword() throws AddressException, MessagingException
    {
    	User user = new User();
    	user.setUsername("username");
    	user.setFullName("Full Name");
    	user.setEmail(Configuration.getInstance().SMTP_USERNAME+"@gmail.com");
    	accountService.emailNewPassword(user,"password");
    }
    
    @Test
    public void generateRandomPass()
    {
    	String pass = accountService.generateRandomPass();
    	Assert.assertEquals(pass.length(),AccountService.PASS_LENGTH);
    }
    
    @Test
    public void resetPassword() throws AddressException, NoSuchAlgorithmException, MessagingException
    {
    	User user = TestEntityFactory.user();
    	user.setEmail(Configuration.getInstance().SMTP_USERNAME+"@gmail.com");
    	userDAO.persist(user);
    	accountService.resetPassword(user.getUsername());
    	userDAO.delete(user);
    }
}
