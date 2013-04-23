package pals.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.entity.PalsUser;

public class PalsUserDAOTest extends BaseTest
{
    @Autowired PalsUserDAO palsUserDAO;
    
    @Test
    public void testPutGet()
    {
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	PalsUser result = palsUserDAO.get(user.getUsername());
    	//Assert.assertTrue(user.equals(result));
        palsUserDAO.deleteAll();
    }
}
