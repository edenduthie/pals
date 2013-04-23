package pals.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.database.PalsGrantedAuthorityDAO;
import pals.database.PalsUserDAO;
import pals.entity.PalsGrantedAuthority;
import pals.entity.PalsUser;
import pals.exception.InvalidInputException;

public class AccountServiceTest extends BaseTest
{
    @Autowired PalsUserDAO palsUserDAO;
    @Autowired PalsGrantedAuthorityDAO palsGrantedAuthorityDAO;
    @Autowired AccountService accountService;
    
    public static final String newEmail = "ya@gmail.com";
    public static final String newPassword = "holygoat";
    
    @Test
    public void testCreate() throws InvalidInputException
    {
    	PalsUser user = accountService.createAccount("eduthie@gmail.com","password","password","192.0.0.1");
    	Assert.assertNotNull(user);
    	Assert.assertEquals(user.getEmail(),"eduthie@gmail.com");
    	Assert.assertEquals(user.getPassword(),accountService.encryptPassword("password"));
    	PalsUser result = palsUserDAO.get("eduthie@gmail.com");
    	//Assert.assertTrue(user.equals(result));
    	
    	Assert.assertEquals(result.getAuthorities().size(), 1);
    	Assert.assertEquals(result.getAuthorities().get(0).getAuthority(),PalsGrantedAuthority.ROLE_USER);
    	Assert.assertEquals(result.getIp(),"192.0.0.1");
    	Assert.assertNotNull(result.getSignupTime());
    	
    	result.setAuthorities(new ArrayList<PalsGrantedAuthority>());
    	palsUserDAO.update(result);
    	palsGrantedAuthorityDAO.deleteAll();
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testCreateAlreadyExsits() throws InvalidInputException
    {
    	PalsUser user = accountService.createAccount("eduthie@gmail.com","password","password","192.0.0.1");
    	try
    	{
    	    user = accountService.createAccount(user.getEmail(),"password","password","192.0.0.1");
    	    Assert.fail("Allowed creation of a duplicate user");
    	}
    	catch( InvalidInputException e ) {}
    	
    	user.setAuthorities(new ArrayList<PalsGrantedAuthority>());
    	palsUserDAO.update(user);
    	palsGrantedAuthorityDAO.deleteAll();
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testCreateMissingUsername() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount(null,"password","password","192.0.0.1");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateUsernameTooShort() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("e@com","password","password","192.0.0.1");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateUsernameNotEmail() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eawef","password","password","192.0.0.1");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateeNoPassword() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eduthie@gmail.com",null,"password","192.0.0.1");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateNoRepeat() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eduthie@gmail.com","password",null,"192.0.0.1");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateNoMatch() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eduthie@gmail.com","password","passwor","192.0.0.1");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreatePasswordTooShort() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eduthie@gmail.com","ya","ya","192.0.0.1");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testUpdateNoUser()
    {
    	try
    	{
    	    accountService.update(null,newEmail,newPassword,newPassword,null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testUpdate() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	
    	accountService.update(user, newEmail, newPassword, newPassword, null);
    	PalsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),newEmail);
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateSameEmail() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	
    	accountService.update(user, user.getEmail(), newPassword, newPassword, null);
    	PalsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),user.getEmail());
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateNoPasswordChange() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	
    	accountService.update(user, newEmail, null, newPassword, null);
    	PalsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),newEmail);
    	Assert.assertEquals(result.getPassword(),user.getPassword());
    	
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateExistingEmail() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	
    	PalsUser existing = Generator.palsUser();
    	existing.setEmail(newEmail);
    	palsUserDAO.put(existing);
    	
    	try
    	{
    	    accountService.update(user, newEmail, newPassword, newPassword, null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdatePasswordsDontMatch() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    
    	try
    	{
    		accountService.update(user, newEmail, newPassword, "different", null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdatePasswordTooShort() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    
    	try
    	{
    		accountService.update(user, newEmail, "ya", "ya", null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateNoEmail() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	
    	accountService.update(user, null, newPassword, newPassword, null);
    	PalsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),user.getEmail());
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateZeroLengthEmail() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	
    	accountService.update(user, "", newPassword, newPassword, null);
    	PalsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),user.getEmail());
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	palsUserDAO.deleteAll();
    }
}
