package pals.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.dao.DAO;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.entity.Experimentable;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.TestEntityFactory;
import pals.entity.User;

public class ExperimentServiceTest extends BaseTest
{
	@Autowired DAO dao;
	@Autowired ExperimentService experimentService;
	@Autowired ModelOutputService modelOutputService;
	
	
	@Test
    public void testGetMyExperiments()
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		experiment.addSharedUser(user2);
		
		dao.persist(experiment);
		
		List<Experiment> experiments = experimentService.getMyExperiments(experiment.getOwner());
		Assert.assertEquals(experiments.size(),1);
		Assert.assertEquals(experiments.get(0).getName(),experiment.getName());
		Assert.assertEquals(experiments.get(0).getId(),experiment.getId());
		Assert.assertEquals(experiments.get(0).getOwner().getUsername(),experiment.getOwner().getUsername());
		Assert.assertEquals(experiments.get(0).getSharedList().size(),2);
		
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());
	}
	
	@Test
    public void testGetSharedExperiments()
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		experiment.addSharedUser(user2);
		
		dao.persist(experiment);
		
		List<Experiment> experiments = experimentService.getSharedExperiments(user2);
		Assert.assertEquals(experiments.size(),1);
		Assert.assertEquals(experiments.get(0).getName(),experiment.getName());
		Assert.assertEquals(experiments.get(0).getId(),experiment.getId());
		Assert.assertEquals(experiments.get(0).getOwner().getUsername(),experiment.getOwner().getUsername());
		Assert.assertEquals(experiments.get(0).getSharedList().size(),2);
		
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());
	}
	
	@Test
	public void testPermitted() throws SecurityException
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		experiment.addSharedUser(user2);
		
		dao.persist(experiment);
		
		experimentService.permitted(user2,experiment.getId());
		
		Assert.assertEquals(user2.getCurrentExperiment().getId(),experiment.getId());
		
		User loadedUser = (User) dao.get(User.class.getName(), "username", user2.getUsername());
		Assert.assertEquals(loadedUser.getCurrentExperiment().getId(),experiment.getId());
	    
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());		
	}
	
	@Test
	public void testNotPermitted()
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		
		dao.persist(experiment);
		
		try {
			experimentService.permitted(user2,experiment.getId());
			Assert.fail();
		} catch (SecurityException e) {
			// ok
		}
		
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());		
	}
	
	@Test
	public void testContains()
	{
		Experiment experiment = TestEntityFactory.experiment();	
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		experiment.addSharedUser(user2);
		Assert.assertTrue(experiment.userAllowed(user2));
	}
	
	@Test
	public void testSetTheseUsersAsShared()
	{
		Experiment experiment = TestEntityFactory.experiment();	
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		experiment.addSharedUser(user2);
		dao.persist(user2);
		dao.persist(experiment.getOwner());
		dao.persist(experiment);
		
		User user3 = TestEntityFactory.user();
		user3.setUsername("user3");
		dao.persist(user3);
		
		List<String> selectedUsernames = new ArrayList<String>();
		selectedUsernames.add("user1");
		selectedUsernames.add("user3");
		experimentService.setTheseUsersAsShared(selectedUsernames, experiment);
		Experiment result = experimentService.getExperiment(experiment.getId());
		Assert.assertEquals(result.getSharedList().size(),2);
		for( User user : result.getSharedList() )
		{
			Assert.assertTrue(selectedUsernames.contains(user.getUsername()));
		}
		
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());
	}
	
	@Test
    public void testGetSharedExperimentsWith2()
	{
		Experiment experiment = TestEntityFactory.experiment();
		Experiment experiment2 = TestEntityFactory.experiment();
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		experiment2.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		experiment.addSharedUser(user2);
		experiment2.addSharedUser(user2);
		
		experiment.setOwner(user1);
		dao.persist(experiment);
		experiment2.setOwner(user1);
		dao.persist(experiment2);
		
		List<Experiment> experiments = experimentService.getSharedExperiments(user1);
        Assert.assertEquals(2,experiments.size());
		
		experimentService.delete(experiment.getId());
		experimentService.delete(experiment2.getId());
		dao.deleteAll(User.class.getName());
	}
	
	@Test
    public void testGetSharedExperimentsShareAll()
	{
		Experiment experiment = TestEntityFactory.experiment();
		Experiment experiment2 = TestEntityFactory.experiment();
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		//experiment.addSharedUser(user1);
		//experiment2.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		//experiment.addSharedUser(user2);
		//experiment2.addSharedUser(user2);
		
		experiment.setShareWithAll(true);
		experiment.setOwner(user2);
		dao.persist(experiment);
		experiment2.setShareWithAll(true);
		experiment2.setOwner(user2);
		dao.persist(experiment2);
		
		List<Experiment> experiments = experimentService.getSharedExperiments(user1);
        Assert.assertEquals(2,experiments.size());
		
		experimentService.delete(experiment.getId());
		experimentService.delete(experiment2.getId());
		dao.deleteAll(User.class.getName());
	}
	
	@Test
	public void testContainsShareAll()
	{
		Experiment experiment = TestEntityFactory.experiment();	
		experiment.setShareWithAll(true);
		// set up shared list
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		//experiment.addSharedUser(user2);
		Assert.assertTrue(experiment.userAllowed(user2));
	}
	
	@Test
	public void testPermittedShareWithAll() throws SecurityException
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		//experiment.addSharedUser(user2);
		
		experiment.setShareWithAll(true);
		dao.persist(experiment);
		
		experimentService.permitted(user2,experiment.getId());
		
		Assert.assertEquals(user2.getCurrentExperiment().getId(),experiment.getId());
		
		User loadedUser = (User) dao.get(User.class.getName(), "username", user2.getUsername());
		Assert.assertEquals(loadedUser.getCurrentExperiment().getId(),experiment.getId());
	    
		
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());		
	}
	
	@Test
    public void testDeleteExperiment() throws IOException, InvalidInputException
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		experiment.addSharedUser(user2);
		
    	DataSetVersion entity = TestEntityFactory.dataSetVersion();
    	dao.persist(entity.getDataSet().getCountry());
    	dao.persist(entity.getDataSet().getVegType());
    	entity.getDataSet().setOwner(experiment.getOwner());
    	dao.persist(entity.getDataSet());
    	entity.getDataSet().setLatestVersion(null);
    	entity.setDataSetId(entity.getDataSet().getId());
    	entity.setOwner(entity.getDataSet().getOwner());
    	entity.setIsPublic(false);
    	dao.persist(entity);
    	
    	experiment.setExperimentables(new ArrayList<Experimentable>());
    	experiment.getExperimentables().add(entity.getDataSet());
		dao.persist(experiment);

		entity.getDataSet().setExperiment(experiment);
		dao.update(entity.getDataSet());
		
		experiment.getOwner().setCurrentExperiment(experiment);
		dao.update(experiment.getOwner());
		
    	Model model = TestEntityFactory.model();
    	model.setExperiment(experiment);
    	dao.persist(model);
    	
    	File uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
					entity.getDataSet().getOwner(), uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
    	
		Integer id = experiment.getId();
		
		experimentService.delete(experiment.getId());
		
		try
		{
		    experiment = experimentService.getExperiment(id);
		    Assert.fail();
		}
		catch( NoResultException e )
		{
			//expected
		}
		
		testEmptyTable("DataSet");
		testEmptyTable("DataSetVersion");
		testEmptyTable("Model");
		
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("User");
		dao.deleteAll("Model");
	}
	
	@Test
    public void testRemoveCurrentExperimentFromUsers() throws IOException, InvalidInputException
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		dao.persist(experiment);
		experiment.getOwner().setCurrentExperiment(experiment);
		dao.update(experiment.getOwner());
		
		experimentService.removeCurrentExperimentFromUsers(experiment);
		
		List<User> users = dao.getAll(User.class.getName());
		for( User user : users )
		{
			Assert.assertNull(user.getCurrentExperiment());
		}
		
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());
	}
	
	public void testEmptyTable(String className)
	{
		List<Object> list = dao.getAll(className);
		Assert.assertEquals(list.size(), 0);
	}
	
	@Test
	public void testGetNumUsers()
	{
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setSharedList(new HashSet<User>());
		for( int i=0; i < 10; ++i )
		{
			User user = TestEntityFactory.user();
			user.setUsername(new Integer(i).toString());
			dao.persist(user);
			experiment.getSharedList().add(user);
		}
		dao.persist(experiment.getOwner());
		dao.persist(experiment);
		
		Assert.assertEquals(experimentService.getNumUsers(experiment),10);
		
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());
	}
	
	@Test
	public void testGetNumDataSets()
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		dao.persist(experiment);
		
		experiment.setExperimentables(new ArrayList<Experimentable>());
		
		for( int i=0; i < 10; ++i )
		{
	    	DataSetVersion entity = TestEntityFactory.dataSetVersion();
	    	dao.persist(entity.getDataSet().getCountry());
	    	if( i == 0 ) dao.persist(entity.getDataSet().getVegType());
	    	entity.getDataSet().setOwner(experiment.getOwner());
	    	dao.persist(entity.getDataSet());
	    	entity.getDataSet().setLatestVersion(null);
	    	entity.setDataSetId(entity.getDataSet().getId());
	    	entity.setOwner(entity.getDataSet().getOwner());
	    	entity.setIsPublic(false);
	    	dao.persist(entity);
	    	experiment.getExperimentables().add(entity.getDataSet());
			entity.getDataSet().setExperiment(experiment);
			dao.update(entity.getDataSet());
		}
		dao.update(experiment);
		
		Assert.assertEquals(experimentService.getNumDataSets(experiment),10);

		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		experimentService.delete(experiment.getId());
		dao.deleteAll(User.class.getName());
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("User");
		dao.deleteAll("Model");
	}
	
	@Test
    public void testDeleteExperimentWithCopiedFrom() throws IOException, InvalidInputException
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		experiment.addSharedUser(user2);
		
    	DataSetVersion entity = TestEntityFactory.dataSetVersion();
    	dao.persist(entity.getDataSet().getCountry());
    	dao.persist(entity.getDataSet().getVegType());
    	entity.getDataSet().setOwner(experiment.getOwner());
    	dao.persist(entity.getDataSet());
    	entity.getDataSet().setLatestVersion(null);
    	entity.setDataSetId(entity.getDataSet().getId());
    	entity.setOwner(entity.getDataSet().getOwner());
    	entity.setIsPublic(false);
    	dao.persist(entity);
    	
    	DataSetVersion dataSetVersion2 = TestEntityFactory.dataSetVersion();
    	dataSetVersion2.setDataSet(entity.getDataSet());
    	dataSetVersion2.setOwner(entity.getOwner());
    	dao.persist(dataSetVersion2);
    	entity.setCopiedFrom(dataSetVersion2);
    	dao.update(entity);
    	
    	experiment.setExperimentables(new ArrayList<Experimentable>());
    	experiment.getExperimentables().add(entity.getDataSet());
		dao.persist(experiment);

		entity.getDataSet().setExperiment(experiment);
		dao.update(entity.getDataSet());
		
		experiment.getOwner().setCurrentExperiment(experiment);
		dao.update(experiment.getOwner());
		
    	Model model = TestEntityFactory.model();
    	model.setExperiment(experiment);
    	dao.persist(model);
    	
    	File uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
					entity.getDataSet().getOwner(), uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
    	
		Integer id = experiment.getId();
		
		experimentService.delete(experiment.getId());
		
		try
		{
		    experiment = experimentService.getExperiment(id);
		    Assert.fail();
		}
		catch( NoResultException e )
		{
			//expected
		}
		
		testEmptyTable("DataSet");
		testEmptyTable("DataSetVersion");
		testEmptyTable("Model");
		
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("User");
		dao.deleteAll("Model");
	}
	
	@Test
    public void testDeleteExperimentWithCopiedTo() throws IOException, InvalidInputException
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		Experiment experiment2 = TestEntityFactory.experiment();
		experiment2.setOwner(experiment.getOwner());
		dao.persist(experiment2);
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		experiment.addSharedUser(user2);
		
    	DataSetVersion entity = TestEntityFactory.dataSetVersion();
    	dao.persist(entity.getDataSet().getCountry());
    	dao.persist(entity.getDataSet().getVegType());
    	entity.getDataSet().setOwner(experiment.getOwner());
    	dao.persist(entity.getDataSet());
    	entity.getDataSet().setLatestVersion(null);
    	entity.setDataSetId(entity.getDataSet().getId());
    	entity.setOwner(entity.getDataSet().getOwner());
    	entity.setIsPublic(false);
    	dao.persist(entity);
    	
    	DataSetVersion dataSetVersion2 = TestEntityFactory.dataSetVersion();
    	dataSetVersion2.setDataSet(entity.getDataSet());
    	dataSetVersion2.setOwner(entity.getOwner());
    	dataSetVersion2.getCopiedTo().add(experiment2);
    	dao.persist(dataSetVersion2);
    	entity.setCopiedFrom(dataSetVersion2);
    	dao.update(entity);
    	
    	experiment.setExperimentables(new ArrayList<Experimentable>());
    	experiment.getExperimentables().add(entity.getDataSet());
		dao.persist(experiment);

		entity.getDataSet().setExperiment(experiment);
		dao.update(entity.getDataSet());
		
		experiment.getOwner().setCurrentExperiment(experiment);
		dao.update(experiment.getOwner());
		
    	Model model = TestEntityFactory.model();
    	model.setExperiment(experiment);
    	dao.persist(model);
    	
    	entity.getCopiedTo().add(experiment);
    	dao.update(entity);
    	
    	File uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
					entity.getDataSet().getOwner(), uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
    	
		Integer id = experiment.getId();
		
		experimentService.delete(experiment.getId());
		
		try
		{
		    experiment = experimentService.getExperiment(id);
		    Assert.fail();
		}
		catch( NoResultException e )
		{
			//expected
		}
		
		testEmptyTable("DataSet");
		testEmptyTable("DataSetVersion");
		testEmptyTable("Model");
		
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Experiment");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("User");
		dao.deleteAll("Model");
	}
	
	@Test
    public void testDeleteExperimentWithExperimentable() throws IOException, InvalidInputException
	{
		Experiment experiment = TestEntityFactory.experiment();
		dao.persist(experiment.getOwner());
		
		// set up shared list
		User user1 = TestEntityFactory.user();
		user1.setUsername("user1");
		dao.persist(user1);
		experiment.addSharedUser(user1);
		User user2 = TestEntityFactory.user();
		user2.setUsername("user2");
		dao.persist(user2);
		experiment.addSharedUser(user2);
		
    	DataSetVersion entity = TestEntityFactory.dataSetVersion();
    	dao.persist(entity.getDataSet().getCountry());
    	dao.persist(entity.getDataSet().getVegType());
    	entity.getDataSet().setOwner(experiment.getOwner());
    	dao.persist(entity.getDataSet());
    	entity.getDataSet().setLatestVersion(null);
    	entity.setDataSetId(entity.getDataSet().getId());
    	entity.setOwner(entity.getDataSet().getOwner());
    	entity.setIsPublic(false);
    	dao.persist(entity);
    	
    	DataSetVersion dsvWithCopiedTo = TestEntityFactory.dataSetVersion();
    	dsvWithCopiedTo.getDataSet().setCountry(entity.getDataSet().getCountry());
    	dsvWithCopiedTo.getDataSet().setVegType(entity.getDataSet().getVegType());
    	dsvWithCopiedTo.getDataSet().setOwner(experiment.getOwner());
    	dao.persist(dsvWithCopiedTo.getDataSet());
    	dsvWithCopiedTo.getDataSet().setLatestVersion(null);
    	dsvWithCopiedTo.setDataSetId(dsvWithCopiedTo.getDataSet().getId());
    	dsvWithCopiedTo.setOwner(dsvWithCopiedTo.getDataSet().getOwner());
    	dsvWithCopiedTo.setIsPublic(false);
    	dao.persist(dsvWithCopiedTo);
    	
    	experiment.setExperimentables(new ArrayList<Experimentable>());
    	experiment.getExperimentables().add(entity.getDataSet());
		dao.persist(experiment);

		entity.getDataSet().setExperiment(experiment);
		dao.update(entity.getDataSet());
		
		experiment.getOwner().setCurrentExperiment(experiment);
		dao.update(experiment.getOwner());
		
		dsvWithCopiedTo.getCopiedTo().add(experiment);
		dao.update(dsvWithCopiedTo);
		
    	Model model = TestEntityFactory.model();
    	model.setExperiment(experiment);
    	dao.persist(model);
    	
    	File uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
					entity.getDataSet().getOwner(), uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
    	
		Integer id = experiment.getId();
		
		experimentService.delete(experiment.getId());
		
		try
		{
		    experiment = experimentService.getExperiment(id);
		    Assert.fail();
		}
		catch( NoResultException e )
		{
			//expected
		}
		
		testEmptyTable("Model");
		
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("User");
		dao.deleteAll("Model");
	}

}
