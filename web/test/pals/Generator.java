package pals;

import java.util.Calendar;

import pals.dto.SignupRequest;
import pals.entity.Address;
import pals.entity.PalsGrantedAuthority;
import pals.entity.PalsUser;
import pals.entity.Suburb;

public class Generator 
{

	public static PalsUser palsUser() 
	{
    	PalsUser palsUser = new PalsUser();
    	palsUser.setCreatedTime(Calendar.getInstance().getTimeInMillis());
    	palsUser.setCredentialsExpired(false);
    	palsUser.setEmail("eduthie@jurisd.com");
    	palsUser.setEnabled(true);
    	palsUser.setExpired(false);
    	palsUser.setLocked(false);
    	palsUser.setName("Ana Catanchin");
    	palsUser.setPassword("password");
    	return palsUser;
	}

	public static PalsGrantedAuthority palsGrantedAuthority() 
	{
		PalsGrantedAuthority auth = new PalsGrantedAuthority();
		auth.setAuthority(PalsGrantedAuthority.ROLE_USER);
		return auth;
	}

	public static Address address() 
	{
		Address address = new Address();
		address.setFirstName("Eden");
		address.setLastName("Duthie");
		address.setCompany("Gator Logic");
		address.setLat("37.1922");
		address.setLon("127.02392");
		address.setPhone("0438045662");
		address.setPostcode("3101");
		address.setState("VIC");
		address.setStreet("Marshall Avenue");
		address.setStreetNumber("32");
		address.setSuburb("Kew");
		address.setUnit(null);
		return address;
	}

	public static Suburb suburb() 
	{
		Suburb suburb = new Suburb("KEW");
		return suburb;
	}

	public static SignupRequest signupRequest() 
	{
		SignupRequest request = new SignupRequest();
		request.setEmail("eduthie@gmail.com");
		request.setPassword("password");
		request.setRepeatPassword("password");
		return request;
	}
}
