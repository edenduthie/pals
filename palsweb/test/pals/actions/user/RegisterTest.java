package pals.actions.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.actions.account.Register;
import pals.dao.UserDAO;

public class RegisterTest extends BaseTest 
{
    @Autowired
    UserDAO userDAO;
	
    @Test
    public void testExecute()
    {
    	Register register = new Register();
    	//register.setUserDAO(userDAO);
    }
}
