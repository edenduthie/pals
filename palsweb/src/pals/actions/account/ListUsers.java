package pals.actions.account;

import java.util.List;

import pals.actions.UserAwareAction;
import pals.entity.User;

public class ListUsers extends UserAwareAction
{
	private static final long serialVersionUID = 8034510373859365358L;
	List<User> allUsers;
	
    public String execute()
    {
    	allUsers = getUserService().getAllUsers();
    	return SUCCESS;
    }

	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
}
