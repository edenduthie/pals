package pals.service;

import java.security.NoSuchAlgorithmException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.transaction.annotation.Transactional;

import pals.dao.DAO;
import pals.dao.UserDAO;
import pals.entity.User;

@Transactional
public class AccountService {

	DAO dao;
	MailService mailService;
	UserDAO userDAO;
	
	static final int PASS_LENGTH = 6;
	
	public User resetPassword(String username) throws NoSuchAlgorithmException, AddressException, MessagingException
	{
		User user = userDAO.getUser(username);
		String password = generateRandomPass();
		user.setPassword(password);
		user.encryptPassword();
		userDAO.update(user);
		emailNewPassword(user,password);
		return user;
	}
	
	public void emailNewPassword(User user,String password) throws AddressException, MessagingException
	{
		String subject = "Your PALS password has been changed";
		String message =
			"Hi " + user.getFullName() + "\n\n" +
			"Your PALS password has been reset. Your new password is: \n\n" +
			password + "\n\n" +
			" Please log on to http://pals.unsw.edu.au and change your password as soon as possible. \n\n" +
		    " Kind regards, \n\n" +
		    " PALS";
		mailService.sendMessage(user.getEmail(),subject,message);
	}
	
	public String generateRandomPass()
	{
		String newPass = "";
		for( int i=0; i < PASS_LENGTH; ++i )
		{
			newPass += Math.round(Math.abs((Math.random()*10)-1));
		}
		return newPass;
	}
	
	public DAO getDao() {
		return dao;
	}
	public void setDao(DAO dao) {
		this.dao = dao;
	}
	public MailService getMailService() {
		return mailService;
	}
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
