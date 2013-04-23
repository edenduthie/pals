package pals.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.controller.AutocompleteController;
import pals.database.SuburbDAO;
import pals.entity.Suburb;

public class AutocompleteControllerTest extends BaseTest 
{
    @Autowired SuburbDAO suburbDAO;
    @Autowired AutocompleteController contorller;
    
    @Test
    public void testSuburb()
    {
    	Suburb suburb = Generator.suburb();
    	suburbDAO.put(suburb);
    	suburb = Generator.suburb();
    	suburb.setName("KHEW");
    	suburbDAO.put(suburb);
    	
    	List<String> results = contorller.suburbs("k");
    	Assert.assertEquals(results.size(),2);
    	
    	suburbDAO.deleteAll();
    }
}
