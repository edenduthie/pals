package pals.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.entity.DataSet;
import pals.entity.Institution;
import pals.entity.TestEntityFactory;

public class DataSetDAOTest extends BaseTest
{
    @Autowired
    DataSetDAO dataSetDAO;
    
    @Autowired 
    CountryDAO countryDAO;
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    VegetationTypeDAO vegetationTypeDAO;
    
    DataSet dataSet;
    
    public void setUp()
    {
    	countryDAO.deleteAll(Institution.class.getName());
    	dataSet = TestEntityFactory.dataSet();
    	countryDAO.create(dataSet.getCountry());
    	userDAO.create(dataSet.getOwner());
    	vegetationTypeDAO.create(dataSet.getVegType());
    }
    
    public void tearDown()
    {
    	countryDAO.deleteAll();
    	userDAO.delete(dataSet.getOwner());
    	vegetationTypeDAO.deleteAll();
    	countryDAO.deleteAll(Institution.class.getName());
    }

    @Test
    public void testCreate()
    {
    	setUp();
    	dataSetDAO.create(dataSet);
    	dataSetDAO.deleteAll();
    	tearDown();
    }

    @Test
    public void testGet()
    {
    	setUp();
    	System.out.println("Country id: " + dataSet.getCountry().getId());
    	dataSetDAO.create(dataSet);
    	System.out.println("DATA SET ID: " + dataSet.getId());
    	dataSetDAO.deleteAll();
    	tearDown();
    }
}
