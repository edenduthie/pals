package pals.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pals.database.PalsUserDAO;
import pals.dto.LoginRequest;
import pals.dto.PalsUserDTO;
import pals.dto.SignupRequest;
import pals.entity.PalsUser;
import pals.exception.InvalidInputException;
import pals.service.AccountService;
import pals.service.LoginStatus;
import pals.util.WebUtil;

@Controller
public class AjaxLoginController 
{
	@Autowired
	@Qualifier("authenticationManager")
    AuthenticationManager authenticationManager;

	@Autowired SecurityContextRepository repository;

	@Autowired RememberMeServices rememberMeServices;
	
	@Autowired PalsUserDAO palsUserDAO;
	@Autowired AccountService accountService;
	@Autowired LoginStatus loginStatus;
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public @ResponseBody PalsUserDTO getCurrentUser()
	{
		PalsUser user = loginStatus.getUserService();
		if( user == null ) return new PalsUserDTO("Not logged in",false);
		else return new PalsUserDTO(user);
	}
	

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public @ResponseBody PalsUserDTO performLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
		    signIn(loginRequest.getEmail(), loginRequest.getPassword(), request, response);	
		}
		catch( BadCredentialsException e )
		{
			return new PalsUserDTO(e.getMessage(), false);
		}
		PalsUser user = palsUserDAO.get(loginRequest.getEmail());
		return new PalsUserDTO(user);
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public @ResponseBody PalsUserDTO signup( 
			@RequestBody SignupRequest signupRequest,
			HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String ip = WebUtil.getIpAddress(request);
			PalsUser user = accountService.createAccount(signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getRepeatPassword(), ip);
			signIn(signupRequest.getEmail(), signupRequest.getPassword(), request, response);
			return new PalsUserDTO(user);
		}
		catch( InvalidInputException e )
		{
			return new PalsUserDTO(e.getMessage(), false);
		}
		catch( BadCredentialsException e )
		{
			return new PalsUserDTO(e.getMessage(), false);
		}
	}
	
	public void signIn(String email, String password, HttpServletRequest request, HttpServletResponse response) throws BadCredentialsException
	{
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password);
		Authentication auth = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(auth);
		repository.saveContext(SecurityContextHolder.getContext(), request, response);
		rememberMeServices.loginSuccess(new RememberMeWrapper(request), response, auth);
	}
	
	public class RememberMeWrapper extends HttpServletRequestWrapper
	{

		public RememberMeWrapper(HttpServletRequest request)
		{
			super(request);
		}
		
		@Override
		public String getParameter(String key)
		{
			if( key.equals("_spring_security_remember_me") ) return "true";
			else return super.getParameter(key);
		}
		
	}
}
