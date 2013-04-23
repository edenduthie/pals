package pals.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.entity.PalsGrantedAuthority;

public class PalsGrantedAuthorityDAOTest extends BaseTest
{
    @Autowired PalsGrantedAuthorityDAO dao;
    
    @Test
    public void getRoleUser()
    {
    	PalsGrantedAuthority auth = Generator.palsGrantedAuthority();
    	dao.put(auth);
    	PalsGrantedAuthority result = dao.getRoleUser();
    	Assert.assertEquals(result.getAuthority(),auth.getAuthority());
    	Assert.assertEquals(result.getId(),auth.getId());
    	dao.deleteAll();
    }
    
    @Test
    public void getRoleUserNotFound()
    {
    	PalsGrantedAuthority result = dao.getRoleUser();
    	Assert.assertNotNull(result);
    	Assert.assertEquals(result.getAuthority(),PalsGrantedAuthority.ROLE_USER);
    	dao.deleteAll();
    }
    
    @Test
    public void getRolePractitioner()
    {
    	PalsGrantedAuthority auth = Generator.palsGrantedAuthority();
    	auth.setAuthority(PalsGrantedAuthority.ROLE_PRACTITIONER);
    	dao.put(auth);
    	PalsGrantedAuthority result = dao.getRolePractitioner();
    	Assert.assertEquals(result.getAuthority(),auth.getAuthority());
    	Assert.assertEquals(result.getId(),auth.getId());
    	dao.deleteAll();
    }
    
    @Test
    public void getRolePractitionerNotFound()
    {
    	PalsGrantedAuthority result = dao.getRolePractitioner();
    	Assert.assertNotNull(result);
    	Assert.assertEquals(result.getAuthority(),PalsGrantedAuthority.ROLE_PRACTITIONER);
    	dao.deleteAll();
    }
}
