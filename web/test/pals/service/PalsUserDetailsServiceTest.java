package pals.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.database.AddressDAO;
import pals.database.PalsUserDAO;
import pals.entity.Address;
import pals.entity.PalsUser;
import pals.exception.InvalidInputException;
import pals.service.PalsUserDetailsService;

public class PalsUserDetailsServiceTest extends BaseTest
{
    @Autowired PalsUserDetailsService palsUserDetailsService;
    @Autowired PalsUserDAO palsUserDAO;
    @Autowired AddressDAO addressDAO;
    
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
    
    @Test
    public void createOrUpdateAddressCreate() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	user.setName("TO BE CHANGED");
    	palsUserDAO.put(user);
    	Address address = Generator.address();
    	
    	palsUserDetailsService.createOrUpdateAddress(user, address);
    	
    	PalsUser retrievedUser = palsUserDAO.get(user.getId());
    	Assert.assertNotNull(retrievedUser.getHomeAddress());
    	Assert.assertEquals(retrievedUser.getName(),address.getFirstName() + " " + address.getLastName());
    	Assert.assertEquals(retrievedUser.getHomeAddress().getStreet(),address.getStreet());
    	
    	palsUserDAO.deleteAll();
    	addressDAO.deleteAll();
    }
    
    @Test
    public void createOrUpdateAddressUpdate() throws InvalidInputException
    {
    	Address address = Generator.address();
    	addressDAO.put(address);
    	PalsUser user = Generator.palsUser();
    	user.setName("TO BE CHANGED");
    	user.setHomeAddress(address);
    	palsUserDAO.put(user);
    	
    	Address newAddress = Generator.address();
    	newAddress.setStreet("CHANGED STREET");
    	newAddress.setFirstName("hoho");
    	newAddress.setLastName("haha");
    	
    	palsUserDetailsService.createOrUpdateAddress(user, newAddress);
    	
    	PalsUser retrievedUser = palsUserDAO.get(user.getId());
    	Assert.assertNotNull(retrievedUser.getHomeAddress());
    	Assert.assertEquals(retrievedUser.getName(),newAddress.getFirstName() + " " + newAddress.getLastName());
    	Assert.assertEquals(retrievedUser.getHomeAddress().getStreet(),newAddress.getStreet());
    	Assert.assertEquals(retrievedUser.getHomeAddress().getId(),address.getId());
    	
    	palsUserDAO.deleteAll();
    	addressDAO.deleteAll();
    }
    
    @Test
    public void createOrUpdateAddressCreateValidationFail() throws InvalidInputException
    {
    	PalsUser user = Generator.palsUser();
    	user.setName("TO BE CHANGED");
    	palsUserDAO.put(user);
    	Address address = Generator.address();
    	
    	address.setState(null);
    	
    	try
    	{
    	    palsUserDetailsService.createOrUpdateAddress(user, address);
    	    Assert.fail("null State was allowed");
    	}
    	catch( InvalidInputException e) {}
    	
    	palsUserDAO.deleteAll();
    	addressDAO.deleteAll();
    }
}
