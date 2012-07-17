package pals.actions.admin;

import pals.actions.UserAwareAction;
import pals.service.UserServiceInterface;

public class NewUser  extends UserAwareAction {
	
	private String 		username;
	private String		password;
	private String 		fullName;
	private String 		shortName;
	private String 		email;
	
	String message;
	
	private UserServiceInterface userService;
	
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserServiceInterface getUserService() {
		return userService;
	}
	public void setUserService(UserServiceInterface userService) {
		this.userService = userService;
	}
	
	/**
	 * Warning: incomplete. Sets up pre-fab users only.
	 * @TODO
	 * @fix
	 * 
	 */
	public String execute() {
		if(!getUser().isAdmin())
		{
			message = "Only administrators can create dummy users";
			return ERROR;
		}
		dummySetup();
		return SUCCESS;
		/*
		if (getUsername() != null && getPassword() != null) {
			getUserService().newUser(getUsername(), getPassword(), getFullName(), getShortName(), getEmail());
			return SUCCESS;
		}
		return INPUT;*/
	}
	
	public void dummySetup() {
		getUserService().newUser("stefan","stefan","Stefan C Gregory","Stefan","Stefan@stefan.com");
		getUserService().newUser("gab","gab","Gab Sun","Gab","gab@gab.com");
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
