package pals.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.entity.Country;
import pals.entity.TestEntityFactory;

public class CountryDAOTest extends BaseTest
{
    @Autowired
    CountryDAO countryDAO;
    
    
    @Test
    public void testGetAllCountries()
    {
    	Country country1 = TestEntityFactory.country();
    	Country country2 = TestEntityFactory.country();
    	country2.setCode("BR");
    	country2.setName("Indonesia");
    	
    	countryDAO.create(country1);
    	countryDAO.create(country2);
    	
    	List<Country> countries = countryDAO.getAll();
    	
    	Assert.assertEquals(2,countries.size());
    	Assert.assertEquals(country1.getCode(),countries.get(0).getCode());
    	Assert.assertEquals(country1.getName(),countries.get(0).getName());
    	
    	countryDAO.deleteAll();
    }
}
