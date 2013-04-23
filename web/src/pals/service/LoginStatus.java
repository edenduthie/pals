package pals.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pals.entity.PalsGrantedAuthority;
import pals.entity.PalsUser;

@Service
public class LoginStatus 
{
	
	public boolean isLoggedInService()
	{
		return isLoggedIn();
	}
	
	public PalsUser getUserService()
	{
		return getUser();
	}
	
    public static boolean isLoggedIn()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = false;
        if( auth == null ) return false;
        for (GrantedAuthority authority : auth.getAuthorities()) 
        {
        	if(authority.getAuthority().equals(PalsGrantedAuthority.ROLE_USER)) 
        	    loggedIn = true;
        }
        return loggedIn;
    }
    
    public static boolean isAdmin()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean admin = false;
        for (GrantedAuthority authority : auth.getAuthorities()) 
        {
        	if(authority.getAuthority().equals(PalsGrantedAuthority.ADMIN_ROLE)) 
        	    admin = true;
        }
        return admin;
    }
    
    public static PalsUser getUser()
    {
    	Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if( o != null && o instanceof PalsUser )
    	{
            PalsUser user = (PalsUser) o;
            return user;
    	}
    	else
    	{
    		return null;
    	}
    }
    
    public static int getUserId()
    {
    	if( getUser() != null ) return getUser().getId();
    	else return -1;
    }
    
    /**
     * Returns true if the given user id matches the current logged in user
     * @return
     */
    public static boolean isUser(Integer id)
    {
    	PalsUser user = getUser();
    	if( user == null ) return false;
    	if( user.getId().equals(id) ) return true;
    	else return false;
    }
}