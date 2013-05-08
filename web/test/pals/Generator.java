package pals;

import java.util.Calendar;

import pals.dto.SignupRequest;
import pals.entity.PalsGrantedAuthority;
import pals.entity.PalsUser;
import pals.entity.Workspace;

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
	
	public static SignupRequest signupRequest() 
	{
		SignupRequest request = new SignupRequest();
		request.setEmail("eduthie@gmail.com");
		request.setPassword("password");
		request.setRepeatPassword("password");
		return request;
	}

	public static Workspace workspace() 
	{
		Workspace workspace = new Workspace();
		workspace.setName("name");
		workspace.setName("workspace");
		return workspace;
	}
}
