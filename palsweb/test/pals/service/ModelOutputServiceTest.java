package pals.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Configuration;
import pals.dao.DAO;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.PalsFile;
import pals.entity.TestEntityFactory;
import pals.entity.User;

public class ModelOutputServiceTest extends BaseTest
{
    @Autowired
    DAO dao;
    
    @Autowired
    ModelOutputService modelOutputService;
    
    @Autowired 
    ExperimentService experimentService;
    
    @Autowired 
    FileService fileService;
    
    DataSet dataSet;
    DataSetVersion entity;
    Model model;
    User user;
    File uploadedFile;
    
	String filename = "testdata/cbSetup.9.5.1.212.exe";
	String smallFilename = "testdata/gif_test.gif";
	String copyFilename = "copy.txt";
	
    public void setUp() throws IOException
    {    	
    	entity = TestEntityFactory.dataSetVersion();
    	dataSet = entity.getDataSet();
    	dao.persist(dataSet.getCountry());
    	dao.persist(dataSet.getVegType());
    	dao.persist(dataSet.getOwner());
    	dao.persist(dataSet);
    	entity.setDataSetId(dataSet.getId());
    	entity.setOwner(dataSet.getOwner());
    	dao.persist(entity);
    	
    	model = TestEntityFactory.model();
    	dao.persist(model);
    	
    	user = dataSet.getOwner();
    	
    	uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
    }
    
	public void tearDown() throws IOException
	{
		modelOutputService.removeAllFiles();
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("Model");
		dao.deleteAll("User");
	}
	
	public void tearDown(Experiment experiment)
	{
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		experimentService.remove(experiment);
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("Model");
		dao.deleteAll("User");
	}
	
	@Test
	public void testNewModelOutput() throws IOException, InvalidInputException
	{
		setUp();
		
		// we make sure the files will not interfere with the real data sets
		Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
		
		model.getId();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC", false);
		Assert.assertNotNull(modelOutput.getId());
		ModelOutput result = (ModelOutput) 
		    dao.get(ModelOutput.class.getName(), "id", modelOutput.getId());
		Assert.assertEquals(result.getDataSetVersionId(), modelOutput.getDataSetVersionId());
		Assert.assertEquals(result.getDataSetVersion().getId(), modelOutput.getDataSetVersion().getId());
		Assert.assertFalse(result.getAllowDownload());
		
		// we also check that we cannot create another model output with the same name
		try
		{
		    modelOutput = 
			    modelOutputService.newModelOutput(
			        user, uploadedFile, "Model Output Name",
			        model.getId(), entity, "SS", "PS", "UC");
		    Assert.fail();
		}
		catch( InvalidInputException iie )
		{
			// what we want
		}
		
		// now we check that the model output file has been copied correctly
		File file = new File(result.retrieveOutputFilePath());
		Assert.assertTrue(file.exists());
		file.delete();
		
		tearDown();
	}
	
	@Test
	public void testDeleteModelOutput() throws IOException, InvalidInputException, SecurityException
	{
		setUp();
		
		// we make sure the files will not interfere with the real data sets
		Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
		
		model.getId();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutputService.deleteModelOutput(modelOutput.getOwner(), modelOutput);
		
		// check that it has been deleted
		List<Object> results = dao.getAll(ModelOutput.class.getName());
		Assert.assertEquals(results.size(),0);
		
		// check the file has been removed
		File file = new File(modelOutput.retrieveOutputFilePath());
		Assert.assertFalse(file.exists());
		
		tearDown();
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputs() throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		modelOutputService.getDao().update(modelOutput);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner());
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());
		tearDown();
	}
	
	@Test
	public void getModelOutputsForModelDataSet() throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		List<ModelOutput> results = modelOutputService.getModelOutputsForModelDataSet(
		    model, dataSet, modelOutput.getOwner());
		Assert.assertEquals(results.size(),1);
		tearDown();
	}
	
	@Test
	public void getModelOutputsForModelDataSetWrongDataSet() throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		dataSet.setId(9999);
		List<ModelOutput> results = modelOutputService.getModelOutputsForModelDataSet(
		    model, dataSet, modelOutput.getOwner());
		Assert.assertEquals(results.size(),0);
		tearDown();
	}
	
	@Test
	public void getModelOutputsForModelDataSetWrongModel() throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		model.setId(9999);
		List<ModelOutput> results = modelOutputService.getModelOutputsForModelDataSet(
		    model, dataSet, modelOutput.getOwner());
		Assert.assertEquals(results.size(),0);
		tearDown();
	}
	
	@Test
	public void getModelOutputsForModelDataSetWrongAccessLevel() throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PRIVATE);
		modelOutputService.getDao().update(modelOutput);
		List<ModelOutput> results = modelOutputService.getModelOutputsForModelDataSet(
		    model, dataSet, modelOutput.getOwner());
		Assert.assertEquals(results.size(),0);
		tearDown();
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsWithExperiment() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(experiment);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner());
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

        tearDown(experiment);
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsWithExperimentWrongExperiment() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		
		Experiment wrongExperiment = new Experiment();
		wrongExperiment.setId(-999);
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(wrongExperiment);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner());
		Assert.assertEquals(results.size(),0);

		tearDown(experiment);
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsWithExperimentNoNull() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner());
		Assert.assertEquals(results.size(),0);

		tearDown(experiment);
	}
	
	@Test
	public void testGetModelOutputs() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner());
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetModelOutputsWithExperiment() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(experiment);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner());
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown(experiment);
	}
	
	@Test
	public void testGetModelOutputsWithExperimentWrongExperiment() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		Experiment wrongExperiment = new Experiment();
		wrongExperiment.setId(-999);
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(wrongExperiment);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner());
		Assert.assertEquals(results.size(),0);

		tearDown(experiment);
	}
	
	@Test
	public void testGetModelOutputsNoNullExperiment() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner());
		Assert.assertEquals(results.size(),0);

		tearDown(experiment);
	}
	
	@Test
	public void testGetPublicModelOutputs() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsNoPublic() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PRIVATE);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user);
		Assert.assertEquals(results.size(),0);

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsWithExperiment() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		

		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		modelOutput.setExperiment(experiment);

		modelOutputService.getDao().update(modelOutput);
		
		user.setCurrentExperiment(experiment);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown(experiment);
	}
	
	@Test
	public void testGetPublicModelOutputsWithExperimentSharedWithAll() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		

		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		experiment.setShareWithAll(true);
		dao.persist(experiment);
		modelOutput.setExperiment(experiment);

		modelOutputService.getDao().update(modelOutput);
		
		user.setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown(experiment);
	}
	
	@Test
	public void testGetPublicModelOutputsWithExperimentWrongExperiment() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		

		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		modelOutput.setExperiment(experiment);

		modelOutputService.getDao().update(modelOutput);
		
		Experiment wrongExperiment = new Experiment();
		wrongExperiment.setId(-999);
		user.setCurrentExperiment(wrongExperiment);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user);
		Assert.assertEquals(results.size(),0);

		tearDown(experiment);
	}
	
	@Test
	public void testGetPublicModelOutputsNoNullExperiment() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		

		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		modelOutput.setExperiment(experiment);

		modelOutputService.getDao().update(modelOutput);
		
		user.setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user);
		Assert.assertEquals(results.size(),0);

		tearDown(experiment);
	}
	
	@Test
	public void testNewModelOutputAncillaryFiles() throws IOException, InvalidInputException
	{
		setUp();
		
    	File ancillaryFile = new File(filename);
    	Assert.assertTrue(ancillaryFile.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(ancillaryFile, copy);
    	
    	List<File> fileList = new ArrayList<File>();
    	fileList.add(copy);
    	List<String> fileNames = new ArrayList<String>();
    	fileNames.add(filename);
    	List<String> contentTypes = new ArrayList<String>();
    	contentTypes.add("binary");
		
		// we make sure the files will not interfere with the real data sets
		Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
		
		model.getId();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC",
			    fileList,fileNames,contentTypes,true);
		Assert.assertNotNull(modelOutput.getId());
		ModelOutput result = (ModelOutput) 
		    dao.get(ModelOutput.class.getName(), "id", modelOutput.getId());
		Assert.assertEquals(result.getDataSetVersionId(), modelOutput.getDataSetVersionId());
		Assert.assertEquals(result.getDataSetVersion().getId(), modelOutput.getDataSetVersion().getId());
		
		// we also check that we cannot create another model output with the same name
		try
		{
		    modelOutput = 
			    modelOutputService.newModelOutput(
			        user, uploadedFile, "Model Output Name",
			        model.getId(), entity, "SS", "PS", "UC");
		    Assert.fail();
		}
		catch( InvalidInputException iie )
		{
			// what we want
		}
		
		// now we check that the model output file has been copied correctly
		File file = new File(result.retrieveOutputFilePath());
		Assert.assertTrue(file.exists());
		file.delete();
		
		tearDown();
	}
	
	@Test
	public void testGetUniqueModels()
	{
		List<ModelOutput> mos = new ArrayList<ModelOutput>();
		for( int i=0; i < 10; ++i )
		{
			ModelOutput mo = TestEntityFactory.modelOutput();
			mo.getModel().setId(i);
			mos.add(mo);
			mo = TestEntityFactory.modelOutput();
			mo.getModel().setId(i);
			mos.add(mo);
		}
		
		Collection<Model> models = modelOutputService.getUniqueModels(mos);
		Assert.assertEquals(models.size(), 10);
	}
	
	@Test
	public void testGetUniqueUsers()
	{
		List<ModelOutput> mos = new ArrayList<ModelOutput>();
		for( int i=0; i < 10; ++i )
		{
			ModelOutput mo = TestEntityFactory.modelOutput();
			mo.getOwner().setUsername(new Integer(i).toString());
			mos.add(mo);
			mo = TestEntityFactory.modelOutput();
			mo.getOwner().setUsername(new Integer(i).toString());
			mos.add(mo);
		}
		
		List<User> users = modelOutputService.getUniqueUsers(mos);
		Assert.assertEquals(users.size(), 10);
	}
	
	@Test
	public void testGetUniqueDSV()
	{
		List<ModelOutput> mos = new ArrayList<ModelOutput>();
		for( int i=0; i < 10; ++i )
		{
			ModelOutput mo = TestEntityFactory.modelOutput();
			mo.getDataSetVersion().setId(i);
			mo.setDataSetVersionId(i);
			mos.add(mo);
			mo = TestEntityFactory.modelOutput();
			mo.getDataSetVersion().setId(i);
			mo.setDataSetVersionId(i);
			mos.add(mo);
		}
		
		List<DataSetVersion> dsvs = modelOutputService.getUniqueDataSetVersions(mos);
		Assert.assertEquals(dsvs.size(), 10);
	}
	
	@Test
	public void testGetModelOutputsParticularModelId() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner(),
	        modelOutput.getModelId(),null,null,null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetModelOutputsParticularUsername() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner(),
	        null,modelOutput.getOwner().getUsername(),null, null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetModelOutputsParticularDSVID() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner(),
	        null,null,modelOutput.getDataSetVersionId(), null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetModelOutputsParticularAccess() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner(),
	        null,null,null,modelOutput.getAccessLevel());
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetModelOutputsPAllFilterParams() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner(),
	        modelOutput.getModelId(),modelOutput.getOwner().getUsername(),modelOutput.getDataSetVersionId(),null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsModelId() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user,modelOutput.getModelId(),null,null,null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsUsername() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user,null,
				modelOutput.getOwner().getUsername(),null,null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsDSV() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user,null,
				null,modelOutput.getDataSetVersionId(),null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsAccess() 
	    throws IOException, InvalidInputException
	{
		setUp();
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    otherUser, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		
		modelOutput.setExperiment(null);
		modelOutputService.getDao().update(modelOutput);
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user,null,
				null,null,modelOutput.getAccessLevel());
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

		tearDown();
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsWithExperimentModelId() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(experiment);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner(),
		    modelOutput.getModelId(),null,null,null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

        tearDown(experiment);
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsWithExperimentUsername() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(experiment);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner(),
		    null,modelOutput.getOwner().getUsername(),null,null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

        tearDown(experiment);
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsWithExperimentDSVId() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(experiment);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner(),
		    null,null,modelOutput.getDataSetVersion().getId(),null);
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

        tearDown(experiment);
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsWithExperimentAccess() 
	    throws IOException, InvalidInputException
	{
		setUp();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(user);
		dao.persist(experiment);
		
		modelOutput.setExperiment(experiment);
		modelOutputService.getDao().update(modelOutput);
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(experiment);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner(),
		    null,null,null,modelOutput.getAccessLevel());
		Assert.assertEquals(results.size(),1);
		Assert.assertEquals(results.get(0).getId(),modelOutput.getId());

        tearDown(experiment);
	}
	
	@Test
	public void testRemoveFile() throws IOException, InvalidInputException
	{
		setUp();
		
    	File ancillaryFile = new File(smallFilename);
    	Assert.assertTrue(ancillaryFile.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(ancillaryFile, copy);
    	
    	List<File> fileList = new ArrayList<File>();
    	fileList.add(copy);
    	List<String> fileNames = new ArrayList<String>();
    	fileNames.add(filename);
    	List<String> contentTypes = new ArrayList<String>();
    	contentTypes.add("binary");
		
		// we make sure the files will not interfere with the real data sets
		Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
		
		model.getId();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC",
			    fileList,fileNames,contentTypes,true);
		Assert.assertNotNull(modelOutput.getId());
		ModelOutput result = (ModelOutput) 
		    dao.get(ModelOutput.class.getName(), "id", modelOutput.getId());
		Assert.assertEquals(result.getDataSetVersionId(), modelOutput.getDataSetVersionId());
		Assert.assertEquals(result.getDataSetVersion().getId(), modelOutput.getDataSetVersion().getId());
		
        modelOutputService.removeFile(modelOutput, modelOutput.getFiles().get(0));
        
        try
        {
            PalsFile palsFile = fileService.getNoData(modelOutput.getFiles().get(0).getId());
            Assert.fail();
        }
        catch( Exception e )
        {
        	// ok
        }
		
		// now we check that the model output file has been copied correctly
		File file = new File(result.retrieveOutputFilePath());
		Assert.assertTrue(file.exists());
		file.delete();
		
		tearDown();
	}
	
	@Test
	public void testUpdateModelOutputAncillaryFiles() throws IOException, InvalidInputException, SecurityException
	{
		setUp();
		
    	File ancillaryFile = new File(smallFilename);
    	Assert.assertTrue(ancillaryFile.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(ancillaryFile, copy);
    	
    	List<File> fileList = new ArrayList<File>();
    	fileList.add(copy);
    	List<String> fileNames = new ArrayList<String>();
    	fileNames.add(filename);
    	List<String> contentTypes = new ArrayList<String>();
    	contentTypes.add("binary");
		
		// we make sure the files will not interfere with the real data sets
		Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
		
		model.getId();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC",
			    fileList,fileNames,contentTypes,true);

    	copy = new File(copyFilename);
    	FileUtils.copyFile(ancillaryFile, copy);
    	
    	fileList = new ArrayList<File>();
    	fileList.add(copy);
    	fileNames = new ArrayList<String>();
    	fileNames.add(filename);
    	contentTypes = new ArrayList<String>();
    	contentTypes.add("binary");
    	modelOutputService.updateModelOutput(user,modelOutput,null,null,null,null,null,null,
    			fileList,fileNames,contentTypes);
		
		Assert.assertEquals(new Integer(2),modelOutputService.getNumFiles(modelOutput.getId()));
		
		tearDown();
	}
	
	@Test
	public void testGetCSV() throws IOException, InvalidInputException, SecurityException
	{
		setUp();
		
		// we make sure the files will not interfere with the real data sets
		Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
		
		model.getId();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		ModelOutput result = (ModelOutput) 
		    dao.get(ModelOutput.class.getName(), "id", modelOutput.getId());
		Assert.assertEquals(result.getDataSetVersionId(), modelOutput.getDataSetVersionId());
		Assert.assertEquals(result.getDataSetVersion().getId(), modelOutput.getDataSetVersion().getId());
		
		// here we check that csv works
		List<String> csv = modelOutputService.getCSV(result.getId(),null,10);
		for( String line : csv )
		{
			System.out.println(line);
			String[] splitLine = line.split("\\,");
			Assert.assertEquals(splitLine.length,14);
		}
		
		// now we check that the model output file has been copied correctly
		File file = new File(result.retrieveOutputFilePath());
//		System.out.println(file.getPath());
		Assert.assertTrue(file.exists());
		file.delete();
		
		tearDown();
	}
	
	@Test
	public void testGetCSVWithDateAndLimit() throws IOException, InvalidInputException, SecurityException
	{
		setUp();
		
		// we make sure the files will not interfere with the real data sets
		Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
		
		model.getId();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		Assert.assertNotNull(modelOutput.getId());
		ModelOutput result = (ModelOutput) 
		    dao.get(ModelOutput.class.getName(), "id", modelOutput.getId());
		Assert.assertEquals(result.getDataSetVersionId(), modelOutput.getDataSetVersionId());
		Assert.assertEquals(result.getDataSetVersion().getId(), modelOutput.getDataSetVersion().getId());
		
		// here we check that csv works
		List<String> csv = modelOutputService.getCSV(result.getId(),"2002,2,2",10);
		for( String line : csv )
		{
			String[] splitLine = line.split("\\,");
			Assert.assertEquals(splitLine.length,14);
		}
		System.out.println(csv.get(0));
		//Assert.assertEquals(csv.get(0),"2002-02-02 00:01,0.0,328.9967346191406,287.2200012207031,0.008792570792138577,5.900000095367432,0.0,0.0,-50.146568298339844,0.0,39.207550048828125,-38.994300842285156,2.052924633026123,-50.64447021484375\n");
		//Assert.assertEquals(csv.get(0),"2002-02-02 00:01,826.2999877929688,381.11065673828125,294.2200012207031,0.007860624231398106,2.180000066757202,0.0,0.0,630.006591796875,697.9876708984375,370.79730224609375,159.04127502441406,-13.573633193969727,98.008056640625\
		Assert.assertEquals(csv.size(),10);
		
		// now we check that the model output file has been copied correctly
		File file = new File(result.retrieveOutputFilePath());
		Assert.assertTrue(file.exists());
		file.delete();
		
		tearDown();
	}
	
	@Test
	public void testGetModelOutputsPagination() 
	    throws IOException, InvalidInputException
	{
		DataSet old = null;
		ModelOutput modelOutput = null;
		
		for( int i=0; i < 10; ++i )
		{
	    	entity = TestEntityFactory.dataSetVersion();
	    	dataSet = entity.getDataSet();
	    	if( i == 0 )
	    	{
	    	    dao.persist(dataSet.getCountry());
	    	    dao.persist(dataSet.getVegType());
	    	    dao.persist(dataSet.getOwner());
	    	}
	    	else
	    	{
	    		dataSet.setCountry(old.getCountry());
	    		dataSet.setVegType(old.getVegType());
	    		dataSet.setOwner(old.getOwner());
	    	}
	    	dao.persist(dataSet);
	    	entity.setDataSetId(dataSet.getId());
	    	entity.setOwner(dataSet.getOwner());
	    	dao.persist(entity);
	    	
	    	if( i == 0 )
	    	{
	    	    model = TestEntityFactory.model();
	    	    dao.persist(model);
	    	}
	    
	    	user = dataSet.getOwner();
	    	old = dataSet;
	    	
	    	uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
	    	
			modelOutput = 
				modelOutputService.newModelOutput(
				    user, uploadedFile, new Integer(i).toString(),
				    model.getId(), entity, "SS", "PS", "UC");
			Assert.assertNotNull(modelOutput.getId());
			modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);		
			modelOutput.setExperiment(null);
			modelOutputService.getDao().update(modelOutput);
	    }
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getModelOutputs(modelOutput.getDataSetVersion().getOwner(),
		        modelOutput.getModelId(),null,null,null,2,5);
		Assert.assertEquals(results.size(),2);

		tearDown();
	}
	
	@Test
	public void testGetModelOutputsPaginationCount() 
	    throws IOException, InvalidInputException
	{
		DataSet old = null;
		ModelOutput modelOutput = null;
		
		for( int i=0; i < 10; ++i )
		{
	    	entity = TestEntityFactory.dataSetVersion();
	    	dataSet = entity.getDataSet();
	    	if( i == 0 )
	    	{
	    	    dao.persist(dataSet.getCountry());
	    	    dao.persist(dataSet.getVegType());
	    	    dao.persist(dataSet.getOwner());
	    	}
	    	else
	    	{
	    		dataSet.setCountry(old.getCountry());
	    		dataSet.setVegType(old.getVegType());
	    		dataSet.setOwner(old.getOwner());
	    	}
	    	dao.persist(dataSet);
	    	entity.setDataSetId(dataSet.getId());
	    	entity.setOwner(dataSet.getOwner());
	    	dao.persist(entity);
	    	
	    	if( i == 0 )
	    	{
	    	    model = TestEntityFactory.model();
	    	    dao.persist(model);
	    	}
	    
	    	user = dataSet.getOwner();
	    	old = dataSet;
	    	
	    	uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
	    	
			modelOutput = 
				modelOutputService.newModelOutput(
				    user, uploadedFile, new Integer(i).toString(),
				    model.getId(), entity, "SS", "PS", "UC");
			Assert.assertNotNull(modelOutput.getId());
			modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);		
			modelOutput.setExperiment(null);
			modelOutputService.getDao().update(modelOutput);
	    }
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		long count = modelOutputService.getModelOutputsCount(modelOutput.getDataSetVersion().getOwner(),
		        modelOutput.getModelId(),null,null,null);
		Assert.assertEquals(count,10);

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsPagination() 
	    throws IOException, InvalidInputException
	{
		DataSet old = null;
		ModelOutput modelOutput = null;
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		for( int i=0; i < 10; ++i )
		{
	    	entity = TestEntityFactory.dataSetVersion();
	    	dataSet = entity.getDataSet();
	    	if( i == 0 )
	    	{
	    	    dao.persist(dataSet.getCountry());
	    	    dao.persist(dataSet.getVegType());
	    	    dao.persist(dataSet.getOwner());
	    	}
	    	else
	    	{
	    		dataSet.setCountry(old.getCountry());
	    		dataSet.setVegType(old.getVegType());
	    		dataSet.setOwner(old.getOwner());
	    	}
	    	dao.persist(dataSet);
	    	entity.setDataSetId(dataSet.getId());
	    	entity.setOwner(dataSet.getOwner());
	    	dao.persist(entity);
	    	
	    	if( i == 0 )
	    	{
	    	    model = TestEntityFactory.model();
	    	    dao.persist(model);
	    	}
	    
	    	user = dataSet.getOwner();
	    	old = dataSet;
	    	
	    	uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
	    	
			modelOutput = 
				modelOutputService.newModelOutput(
				    otherUser, uploadedFile, new Integer(i).toString(),
				    model.getId(), entity, "SS", "PS", "UC");
			Assert.assertNotNull(modelOutput.getId());
			modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		
			modelOutput.setExperiment(null);
			modelOutputService.getDao().update(modelOutput);
	    }
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		List<ModelOutput> results = modelOutputService.getPublicModelOutputs(user,modelOutput.getModelId(),null,null,null,2,5);
		Assert.assertEquals(results.size(),2);

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsCount() 
	    throws IOException, InvalidInputException
	{
		DataSet old = null;
		ModelOutput modelOutput = null;
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		for( int i=0; i < 10; ++i )
		{
	    	entity = TestEntityFactory.dataSetVersion();
	    	dataSet = entity.getDataSet();
	    	if( i == 0 )
	    	{
	    	    dao.persist(dataSet.getCountry());
	    	    dao.persist(dataSet.getVegType());
	    	    dao.persist(dataSet.getOwner());
	    	}
	    	else
	    	{
	    		dataSet.setCountry(old.getCountry());
	    		dataSet.setVegType(old.getVegType());
	    		dataSet.setOwner(old.getOwner());
	    	}
	    	dao.persist(dataSet);
	    	entity.setDataSetId(dataSet.getId());
	    	entity.setOwner(dataSet.getOwner());
	    	dao.persist(entity);
	    	
	    	if( i == 0 )
	    	{
	    	    model = TestEntityFactory.model();
	    	    dao.persist(model);
	    	}
	    
	    	user = dataSet.getOwner();
	    	old = dataSet;
	    	
	    	uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
	    	
			modelOutput = 
				modelOutputService.newModelOutput(
				    otherUser, uploadedFile, new Integer(i).toString(),
				    model.getId(), entity, "SS", "PS", "UC");
			Assert.assertNotNull(modelOutput.getId());
			modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);		
			modelOutput.setExperiment(null);
			modelOutputService.getDao().update(modelOutput);
	    }
		
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(null);
		long count = modelOutputService.getPublicModelOutputsCount(user,modelOutput.getModelId(),null,null,null);
		Assert.assertEquals(count,10);

		tearDown();
	}
	
	@Test
	public void testGetPublicModelOutputsCountWithExperiment() 
	    throws IOException, InvalidInputException
	{
		DataSet old = null;
		ModelOutput modelOutput = null;
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		Experiment experiment =  null;
		
		for( int i=0; i < 10; ++i )
		{
	    	entity = TestEntityFactory.dataSetVersion();
	    	dataSet = entity.getDataSet();
	    	if( i == 0 )
	    	{
	    	    dao.persist(dataSet.getCountry());
	    	    dao.persist(dataSet.getVegType());
	    	    dao.persist(dataSet.getOwner());
	    	}
	    	else
	    	{
	    		dataSet.setCountry(old.getCountry());
	    		dataSet.setVegType(old.getVegType());
	    		dataSet.setOwner(old.getOwner());
	    	}
	    	dao.persist(dataSet);
	    	entity.setDataSetId(dataSet.getId());
	    	entity.setOwner(dataSet.getOwner());
	    	dao.persist(entity);
	    	
	    	if( i == 0 )
	    	{
	    	    model = TestEntityFactory.model();
	    	    dao.persist(model);
	    	}
	    
	    	user = dataSet.getOwner();
	    	old = dataSet;
	    	
	    	uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
	    	
			modelOutput = 
				modelOutputService.newModelOutput(
				    otherUser, uploadedFile, new Integer(i).toString(),
				    model.getId(), entity, "SS", "PS", "UC");
			Assert.assertNotNull(modelOutput.getId());
			modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);
			
			if( i == 0 )
			{
				experiment = TestEntityFactory.experiment();
				experiment.setOwner(user);
				dao.persist(experiment);
			}
			
			modelOutput.setExperiment(experiment);
			modelOutputService.getDao().update(modelOutput);
	    }
		
		
		user.setCurrentExperiment(experiment);
		long count = modelOutputService.getPublicModelOutputsCount(user,modelOutput.getModelId(),null,null,null);
		Assert.assertEquals(count,10);

		tearDown(experiment);
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsPagination() 
	    throws IOException, InvalidInputException
	{
		DataSet old = null;
		ModelOutput modelOutput = null;
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		Experiment experiment =  null;
		
		for( int i=0; i < 10; ++i )
		{
	    	entity = TestEntityFactory.dataSetVersion();
	    	dataSet = entity.getDataSet();
	    	if( i == 0 )
	    	{
	    	    dao.persist(dataSet.getCountry());
	    	    dao.persist(dataSet.getVegType());
	    	    dao.persist(dataSet.getOwner());
	    	}
	    	else
	    	{
	    		dataSet.setCountry(old.getCountry());
	    		dataSet.setVegType(old.getVegType());
	    		dataSet.setOwner(old.getOwner());
	    	}
	    	dao.persist(dataSet);
	    	entity.setDataSetId(dataSet.getId());
	    	entity.setOwner(dataSet.getOwner());
	    	dao.persist(entity);
	    	
	    	if( i == 0 )
	    	{
	    	    model = TestEntityFactory.model();
	    	    dao.persist(model);
	    	}
	    
	    	user = dataSet.getOwner();
	    	old = dataSet;
	    	
	    	uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
	    	
			modelOutput = 
				modelOutputService.newModelOutput(
				    otherUser, uploadedFile, new Integer(i).toString(),
				    model.getId(), entity, "SS", "PS", "UC");
			Assert.assertNotNull(modelOutput.getId());
			modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);
			
			if( i == 0 )
			{
				experiment = TestEntityFactory.experiment();
				experiment.setOwner(user);
				dao.persist(experiment);
			}
			
			modelOutput.setExperiment(experiment);
			modelOutputService.getDao().update(modelOutput);
	    }
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(experiment);
		List<ModelOutput> results = modelOutputService.getDataSetOwnerModelOutputs(modelOutput.getDataSetVersion().getOwner(),
		    modelOutput.getModelId(),null,null,null,2,5);
		Assert.assertEquals(results.size(),2);

        tearDown(experiment);
	}
	
	@Test
	public void testGetDataSetOwnerModelOutputsPaginationCount() 
	    throws IOException, InvalidInputException
	{
		DataSet old = null;
		ModelOutput modelOutput = null;
		
		User otherUser = TestEntityFactory.user();
		otherUser.setUsername("newusername");
		dao.persist(otherUser);
		
		Experiment experiment =  null;
		
		for( int i=0; i < 10; ++i )
		{
	    	entity = TestEntityFactory.dataSetVersion();
	    	dataSet = entity.getDataSet();
	    	if( i == 0 )
	    	{
	    	    dao.persist(dataSet.getCountry());
	    	    dao.persist(dataSet.getVegType());
	    	    dao.persist(dataSet.getOwner());
	    	}
	    	else
	    	{
	    		dataSet.setCountry(old.getCountry());
	    		dataSet.setVegType(old.getVegType());
	    		dataSet.setOwner(old.getOwner());
	    	}
	    	dao.persist(dataSet);
	    	entity.setDataSetId(dataSet.getId());
	    	entity.setOwner(dataSet.getOwner());
	    	dao.persist(entity);
	    	
	    	if( i == 0 )
	    	{
	    	    model = TestEntityFactory.model();
	    	    dao.persist(model);
	    	}
	    
	    	user = dataSet.getOwner();
	    	old = dataSet;
	    	
	    	uploadedFile = new File("testdata/CABLE_Tumbarumba.nc");
	    	
			modelOutput = 
				modelOutputService.newModelOutput(
				    otherUser, uploadedFile, new Integer(i).toString(),
				    model.getId(), entity, "SS", "PS", "UC");
			Assert.assertNotNull(modelOutput.getId());
			modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);
			
			if( i == 0 )
			{
				experiment = TestEntityFactory.experiment();
				experiment.setOwner(user);
				dao.persist(experiment);
			}
			
			modelOutput.setExperiment(experiment);
			modelOutputService.getDao().update(modelOutput);
	    }
		
		modelOutput.getDataSetVersion().getOwner().setCurrentExperiment(experiment);
		long count = modelOutputService.getDataSetOwnerModelOutputsCount(modelOutput.getDataSetVersion().getOwner(),
		    modelOutput.getModelId(),null,null,null);
		Assert.assertEquals(count,10);

        tearDown(experiment);
	}
	
	@Test
	public void testUpdateModelOutputAllowDownload() throws IOException, InvalidInputException, SecurityException
	{
		setUp();
		
		// we make sure the files will not interfere with the real data sets
		Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
		
		model.getId();
		ModelOutput modelOutput = 
			modelOutputService.newModelOutput(
			    user, uploadedFile, "Model Output Name",
			    model.getId(), entity, "SS", "PS", "UC");
		
		ModelOutput result = (ModelOutput) 
	       dao.get(ModelOutput.class.getName(), "id", modelOutput.getId());
		Assert.assertTrue(result.getAllowDownload());
    	
    	modelOutputService.updateModelOutput(user,modelOutput,null,null,null,null,null,null,
    			null,null,null,false);
    	
		result = (ModelOutput) 
	       dao.get(ModelOutput.class.getName(), "id", modelOutput.getId());
		Assert.assertFalse(result.getAllowDownload());
    	
		tearDown();
	}

}
