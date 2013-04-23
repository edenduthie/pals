package pals.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.database.SuburbDAO;
import pals.entity.Suburb;

public class SuburbDAOTest extends BaseTest
{
	@Autowired SuburbDAO suburbDAO;
	
    @Test
    public void testSearchByName()
    {
    	Suburb suburb = Generator.suburb();
    	suburbDAO.put(suburb);
    	suburb = Generator.suburb();
    	suburb.setName("KPHEW");
    	suburbDAO.put(suburb);
    	
    	List<String> results = suburbDAO.searchByName("k");
    	Assert.assertEquals(results.size(),2);
    	
    	suburbDAO.deleteAll();
    }
    
    @Test
    public void testSearchByNameNoFound()
    {
    	Suburb suburb = Generator.suburb();
    	suburbDAO.put(suburb);
    	suburb = Generator.suburb();
    	suburb.setName("PHEW");
    	suburbDAO.put(suburb);
    	
    	List<String> results = suburbDAO.searchByName("HOLY MOTHER");
    	Assert.assertEquals(results.size(),0);
    	
    	suburbDAO.deleteAll();
    }
    
    @Test
    public void testSearchByNameNoQuery()
    {
    	Suburb suburb = Generator.suburb();
    	suburbDAO.put(suburb);
    	suburb = Generator.suburb();
    	suburb.setName("PHEW");
    	suburbDAO.put(suburb);
    	
    	List<String> results = suburbDAO.searchByName(null);
    	Assert.assertEquals(results.size(),2);
    	
    	suburbDAO.deleteAll();
    }
}
