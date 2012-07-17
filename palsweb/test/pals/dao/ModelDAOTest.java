package pals.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.entity.Experiment;
import pals.entity.Institution;
import pals.entity.Model;
import pals.entity.TestEntityFactory;
import pals.entity.User;
import pals.service.ExperimentService;

public class ModelDAOTest extends BaseTest
{
    @Autowired
    ModelDAO dao;
    
    Model model;
    
    @Autowired 
    ExperimentService experimentService;
    
    Experiment experiment;
    
    public void setUp()
    {
	    model = TestEntityFactory.model();
	    dao.persist(model.getUser());
	    dao.persist(model);

    	for( int i=0; i < 9; ++i )
    	{
    	    model = TestEntityFactory.model();
    	    dao.persist(model);
    	}
    }
    
    public void tearDown()
    {
    	dao.deleteAll(Model.class.getName());
    	dao.deleteAll(User.class.getName());
    }
    
    @Test
    public void testGetAll()
    {
        setUp();
        List<Model> models = dao.getAll(model.getUser());
        Assert.assertEquals(models.size(), 10);
        for( Model result : models )
        {
        	Assert.assertNotNull(result.getId());
            Assert.assertNotNull(result.getModelName());
        }
        tearDown();
    }
    
    @Test
    public void testGetAllWithExperiment()
    {
	    setUpExperiment();
    	
    	model.getUser().setCurrentExperiment(experiment);
        List<Model> models = dao.getAll(model.getUser());
        Assert.assertEquals(models.size(), 10);
        for( Model result : models )
        {
        	Assert.assertNotNull(result.getId());
            Assert.assertNotNull(result.getModelName());
        }
        
    	tearDownExperiment();
    }
    
    public void setUpExperiment()
    {
	    model = TestEntityFactory.model();
	    dao.persist(model.getUser());
	    
		experiment = TestEntityFactory.experiment();
		experiment.setOwner(model.getUser());
		dao.persist(experiment);
		model.setExperiment(experiment);
	    
	    dao.persist(model);

    	for( int i=0; i < 9; ++i )
    	{
    	    model = TestEntityFactory.model();
    	    model.setExperiment(experiment);
    	    dao.persist(model);
    	}
    }
    
    public void tearDownExperiment()
    {
    	dao.deleteAll(Model.class.getName());
    	experimentService.remove(experiment);
    	dao.deleteAll(User.class.getName());
    	dao.deleteAll(Institution.class.getName());
    }
    
    @Test
    public void testGetAllWithExperimentWrongExperiment()
    {
        setUpExperiment();
    	
    	Experiment wrongExperiment = new Experiment();
    	wrongExperiment.setId(-999);
    	model.getUser().setCurrentExperiment(wrongExperiment);
        List<Model> models = dao.getAll(model.getUser());
        Assert.assertEquals(models.size(),0);
    	tearDownExperiment();
    }
    
    @Test
    public void testGetAllNoNullExperiment()
    {
        setUpExperiment();
    	model.getUser().setCurrentExperiment(null);
        List<Model> models = dao.getAll(model.getUser());
        Assert.assertEquals(models.size(),0);
    	tearDownExperiment();
    }
}
