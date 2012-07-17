package pals.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.entity.Institution;
import pals.entity.TestEntityFactory;
import pals.entity.User;

public class UserDAOTest extends BaseTest 
{
    @Autowired
    UserDAO userDAO;
    
    private User user;
    
    public static final String TEST_USERNAME = "testusername";
    public static final String TEST_USERNAME_NOT_EXISTS = "testusernamenotexists";
    public static final String TEST_PASSWORD = "password";
    
    public void setUp()
    {
    	user  = TestEntityFactory.user();
    	user.setUsername(TEST_USERNAME);
    	user.setAdmin(false);
    	user.setPassword(TEST_PASSWORD);
    	userDAO.create(user);
    }
    
    @AfterTest
    public void tearDown()
    {
//    	if( user != null )
//    	{
//    	    userDAO.delete(user);
//    	}
    	userDAO.deleteAll(User.class.getName());
    	userDAO.deleteAll(Institution.class.getName());
    }
    
    @Test
    public void testUsernameExistsExists()
    {
    	setUp();
    	Assert.assertTrue(userDAO.usernameExists(TEST_USERNAME));
    	tearDown();
    }
    
    @Test
    public void testUsernameExistsNotExists()
    {
    	Assert.assertFalse(userDAO.usernameExists(TEST_USERNAME_NOT_EXISTS));
    }
    
    @Test
    public void testUpdate()
    {
        setUp();
        String changedName = "Changed the name";
        user.setFullName(changedName);
        userDAO.update(user);
        User result = userDAO.getUser(user.getUsername());
		Assert.assertEquals(result.getFullName(),changedName);
        tearDown();
    }
    
    @Test 
    public void create()
    {
    	user = TestEntityFactory.user();
    	userDAO.create(user);
    	tearDown();
    }
    
    @Test 
    public void createInstitutionAlreadyExists()
    {
    	user = TestEntityFactory.user();
    	userDAO.persist(user.getInstitution());
    	Integer id = user.getInstitution().getId();
    	userDAO.create(user);
    	Assert.assertEquals(id,user.getInstitution().getId());
    	tearDown();
    }
}
