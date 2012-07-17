package pals.service;

import pals.entity.User;

public interface AdminServiceInterface {
	
	public User		createNewUser(User user);
	
	public void		deactivateUser(User user);
	
	

}
