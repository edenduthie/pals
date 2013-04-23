package pals.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import pals.database.PalsGrantedAuthorityDAO;
import pals.database.PalsUserDAO;
import pals.entity.PalsUser;
import pals.exception.InvalidInputException;

@Service
@Transactional
public class AccountService 
{
    @Autowired PalsUserDAO palsUserDAO;
    @Autowired PalsGrantedAuthorityDAO authDAO;
    
    @Transactional(isolation=Isolation.SERIALIZABLE, rollbackFor={InvalidInputException.class})
    public PalsUser createAccount(String email, String password, String repeatPassword, String ip) throws InvalidInputException
    {
    	if( email == null ) throw new InvalidInputException("Please enter an email address");
    	if( email.length() < 6 || !email.contains("@") ) throw new InvalidInputException("Email address is not valid");
    	
    	PalsUser existingUser = palsUserDAO.get(email);
    	if( existingUser != null ) throw new InvalidInputException("An account already exists with the email: " + email);
    	
    	if( password == null ) throw new InvalidInputException("Please provide a password");
    	if( password.length() < 6 ) throw new InvalidInputException("Password must be at least 6 characters");
    	if( repeatPassword == null ) throw new InvalidInputException("Please repeat the password");
    	password = password.trim();
    	repeatPassword = repeatPassword.trim();
    	
    	if( !password.equals(repeatPassword) ) throw new InvalidInputException("Passwords do not match");
    	
    	PalsUser user = new PalsUser();
    	
    	user.setEmail(email);
    	user.setPassword(encryptPassword(password));
    	
    	//PalsGrantedAuthority auth = authDAO.getRoleUser();
    	user.add(authDAO.getRoleUser());
    	
    	user.setIp(ip);
    	user.setSignupTime(Calendar.getInstance().getTimeInMillis());

    	palsUserDAO.put(user);
    	
    	return user;
    }
    
    @Transactional(isolation=Isolation.SERIALIZABLE, rollbackFor={InvalidInputException.class})
    public PalsUser update(PalsUser user, String email, String password, String repeatPassword, Boolean emailNotifications) throws InvalidInputException
    {
    	if( user == null ) throw new InvalidInputException("You must be logged in to change your details");
    	
    	boolean haveChanged = false;
    	
    	if( email != null && !email.equals(user.getEmail()) && email.length() > 0 )
    	{
    		PalsUser existingUser = palsUserDAO.get(email);
    		if( existingUser != null ) throw new InvalidInputException("An account already exists with the email: " + email);
    		user.setEmail(email);
    		haveChanged = true;
    	}
    	if( password != null && password.length() > 0 ) // changing the password
    	{
    		if( password.length() < 6 ) throw new InvalidInputException("Password must be at least 6 characters");
        	if( repeatPassword == null ) throw new InvalidInputException("Please repeat the password");
        	password = password.trim();
        	repeatPassword = repeatPassword.trim();
        	if( !password.equals(repeatPassword) ) throw new InvalidInputException("Passwords do not match");
        	user.setPassword(encryptPassword(password));
        	haveChanged = true;
    	}
    	if( haveChanged )
    	{
    		palsUserDAO.update(user);
    	}
    	
    	return user;
    }
    
    public String encryptPassword(String password)
	{
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		encoder.setEncodeHashAsBase64(false);
		return  encoder.encodePassword(password,null);	
	}
    
    public PalsUser get(String email)
    {
    	return palsUserDAO.get(email);
    }
    
    public PalsUser get(Integer id)
    {
    	return palsUserDAO.get(id);
    }
}
