package pals.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.database.PalsUserDAO;
import pals.entity.PalsUser;

public class PalsUserDetailsServiceTest extends BaseTest
{
    @Autowired PalsUserDetailsService palsUserDetailsService;
    @Autowired PalsUserDAO palsUserDAO;
    
    @Test
    public void loadUserByUsername()
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	UserDetails result = palsUserDetailsService.loadUserByUsername(user.getUsername());
    	Assert.assertEquals(result.getUsername(),user.getUsername());
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void loadUserByUsernameNoneFound()
    {
    	try
    	{
    	    palsUserDetailsService.loadUserByUsername("missing");
    	    Assert.fail();
    	}
    	catch( UsernameNotFoundException e ) {}
    }
}
