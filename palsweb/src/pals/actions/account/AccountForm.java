package pals.actions.account;

import org.apache.log4j.Logger;

import pals.actions.UserAwareAction;
import pals.dao.UserDAO;
import pals.entity.User;

public class AccountForm extends UserAwareAction
{
	private static final Logger log = Logger.getLogger(AccountForm.class);
	
	User userToEdit;
	UserDAO userDAO;
	
	public String execute()
	{
		log.debug("Executing Action: " + getClass().getName());
		
		if( getUserToEdit() == null )
		{
			setUserToEdit(getUser());
			return INPUT;
		}
		return SUCCESS;
	}

	public User getUserToEdit() {
		return userToEdit;
	}

	public void setUserToEdit(User userToEdit) {
		this.userToEdit = userToEdit;
	}
}
