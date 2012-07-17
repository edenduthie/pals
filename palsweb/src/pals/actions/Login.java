package pals.actions;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import pals.entity.User;
import pals.service.UserServiceInterface;
import pals.utils.LSMEvalConstants;

import com.opensymphony.xwork2.ActionSupport;


public class Login extends UserAwareAction {

	private String username;
	private String password;
	UserServiceInterface userService;
	
	public String execute() {
		User user = getUserService().authenticateUser(getUsername(), getPassword());
		if (user == null) {
			addActionError("Incorrect username or password.");
			return INPUT;
		} else {
			session.put(LSMEvalConstants.USER, user);
		}
		return SUCCESS;
		
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

	public UserServiceInterface getUserService() {
		return userService;
	}

	public void setUserService(UserServiceInterface userService) {
		this.userService = userService;
	}
	
}
