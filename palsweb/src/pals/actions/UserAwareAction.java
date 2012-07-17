package pals.actions;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import pals.Configuration;
import pals.entity.User;
import pals.service.UserServiceInterface;
import pals.utils.LSMEvalConstants;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/***
 * Base class for most PALS actions. Requires user to be logged in.
 * Takes care of the <User> object and the session.
 * 
 * @author Stefan Gregory
 *
 */
public abstract class UserAwareAction extends ActionSupport implements SessionAware {

	private static final Logger log = Logger.getLogger(UserAwareAction.class);
	
	private UserServiceInterface userService;
	Map session;
	
	public UserServiceInterface getUserService() {
		return userService;
	}

	public void setUserService(UserServiceInterface userService) {
		this.userService = userService;
	}
	
	public void setSession(Map session) {
		this.session = session;
	}
	
	public User getUser() {
		User user = (User)session.get(LSMEvalConstants.USER);
		return user;
	}
	
	public boolean getLoggedIn() {
		return (getUser() != null);
	}
	
	public String getThumbnailWidth() {
		return Configuration.getInstance().IMAGE_THUMB_WIDTH;
	}
	
	public String getThumbnailHeight() {
		return Configuration.getInstance().IMAGE_THUMB_HEIGHT;
	}

	public String getWebAppURLBase() {
		return Configuration.getInstance().WEBAPP_URL_BASE;
	}
	
	public String getActionName()
	{
	    return ActionContext.getContext().getName();	
	}
}
