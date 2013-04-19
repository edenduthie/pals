package pals.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pals.database.PalsUserDAO;
import pals.entity.PalsUser;

@Service(value="palsUserDetailsService")
public class PalsUserDetailsService implements UserDetailsService
{
	@Autowired PalsUserDAO tanlsUserDAO;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException 
	{
		PalsUser user = tanlsUserDAO.get(username);
		if( user == null ) throw new UsernameNotFoundException("No user was found with the email: " + username);
		else return user;
	}

	public Object get(Integer id) 
	{
		return tanlsUserDAO.get(id);
	}

}
