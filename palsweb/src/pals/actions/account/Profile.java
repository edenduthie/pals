package pals.actions.account;

import pals.actions.UserAwareAction;
import pals.dao.DAO;
import pals.entity.User;

public class Profile extends UserAwareAction
{
	String username;
	DAO dao;
	User profileUser;
	
    public String execute()
    {
    	if( username != null )
    	{
    	    profileUser = (User) dao.get(User.class.getName(), "username", username);
    	}
    	return SUCCESS;
    }

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public User getProfileUser() {
		return profileUser;
	}

	public void setProfileUser(User profileUser) {
		this.profileUser = profileUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
