package pals.actions.account;

import java.security.NoSuchAlgorithmException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import pals.entity.User;
import pals.service.AccountService;

import com.opensymphony.xwork2.ActionSupport;

public class ForgotPassword extends ActionSupport
{
	public static Logger log = Logger.getLogger(ForgotPassword.class);
	
	AccountService accountService;
	String username;
	String message;
	
	static String FORM = "form";
	static String RESET = "reset";
	
    public String form() 
    {
    	return FORM;
    }
    
    public String reset() 
    {
    	User user;
    	try {
			user = accountService.resetPassword(username);
		} catch (AddressException e) {
			log.error(e);
			message = "The email address provided with the account was invalid. " + e.getMessage();
			return RESET;
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
			message = "There was an internal problem encoding your new password. ";
			return RESET;
		} catch (MessagingException e) {
			log.error(e);
			message = "There was an error sending you the confirmation email, please try again.";
			return FORM;
		} catch (NoResultException e) {
			log.error(e);
			message = "The username " + username + " was not found in PALS.";
			return FORM;
		}
		message = "An email has been sent to your email address " + user.getEmail() +
		    " with your new password.";
    	return RESET;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
