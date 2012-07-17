package pals.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Configuration;
import pals.entity.Experiment;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.PalsFile;
import pals.entity.TestEntityFactory;
import pals.entity.User;

public class ModelServiceTest extends BaseTest
{
	Model model;
	
	@Autowired ModelService modelService;
	
    @Autowired 
    ExperimentService experimentService;
    
    @Autowired FileService fileService;
    
	String filename = "testdata/cbSetup.9.5.1.212.exe";
	String smallFilename = "testdata/gif_test.gif";
	String copyFilename = "copy.txt";
	
    public void setUp()
    {
    	model = TestEntityFactory.model();
    	modelService.getDao().persist(model.getUser());
    	modelService.getDao().persist(model);
    }
    
    public void tearDown()
    {
    	modelService.getDao().deleteAll(Model.class.getName());
    	modelService.getDao().deleteAll(User.class.getName());
    }
    
    @Test
    public void testGetModels()
    {
        setUp();
        List<Model> models = modelService.getModels(model.getUser());
        Assert.assertEquals(models.size(),1);
        Assert.assertEquals(models.get(0),model);
        tearDown();
    }
    
    @Test
    public void testGetModelsWithExperiment()
    {
        setUp();
        
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(model.getUser());
		modelService.getDao().persist(experiment);
		model.setExperiment(experiment);
		modelService.getDao().update(model);
		
		model.getUser().setCurrentExperiment(experiment);
        
        List<Model> models = modelService.getModels(model.getUser());
        Assert.assertEquals(models.size(),1);
        Assert.assertEquals(models.get(0),model);
        
        experimentService.removeCurrentExperimentFromExperimentables(experiment);
        experimentService.remove(experiment);
        tearDown();
    }
    
    @Test
    public void testGetModelsWrongExperiment()
    {
        setUp();
        
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(model.getUser());
		modelService.getDao().persist(experiment);
		model.setExperiment(experiment);
		modelService.getDao().update(model);
		
		Experiment wrongExperiment = new Experiment();
		model.getUser().setCurrentExperiment(wrongExperiment);
        
        List<Model> models = modelService.getModels(model.getUser());
        Assert.assertEquals(models.size(),0);
        
        experimentService.removeCurrentExperimentFromExperimentables(experiment);
        experimentService.remove(experiment);
        tearDown();
    }
    
    @Test
    public void testNewModel() throws IOException
    {
    	setUp();
    	File file = new File(filename);
    	Assert.assertTrue(file.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	List<File> uploads = new ArrayList<File>();
    	uploads.add(copy);
    	List<String> fileNames = new ArrayList<String>();
    	fileNames.add("Test File");
    	List<String> contentTypes = new ArrayList<String>();
    	contentTypes.add("Test content type");
    	
    	Model result = modelService.newModel(model.getUser(),"TestNew",
    	    "TestVersion","TestRef","TestComments","TestURL", uploads, 
    	    fileNames, contentTypes);
    	Assert.assertNotNull(result);
    	Assert.assertEquals(result.getFiles().size(),1);
    	
    	modelService.getDao().delete(Model.class.getName(),"id",result.getId());
    	tearDown();
    }
    
    @Test
    public void testUpdateWithFiles() throws IOException, SecurityException
    {
    	setUp();
    	File file = new File(smallFilename);
    	Assert.assertTrue(file.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	List<File> uploads = new ArrayList<File>();
    	uploads.add(copy);
    	List<String> fileNames = new ArrayList<String>();
    	fileNames.add("Test File");
    	List<String> contentTypes = new ArrayList<String>();
    	contentTypes.add("Test content type");
    	
    	Model result = modelService.newModel(model.getUser(),"TestNew",
    	    "TestVersion","TestRef","TestComments","TestURL", uploads, 
    	    fileNames, contentTypes);
    	
    	Assert.assertEquals(result.getFiles().size(),1);
    	
    	copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	uploads = new ArrayList<File>();
    	uploads.add(copy);
    	fileNames = new ArrayList<String>();
    	fileNames.add("Test File");
    	contentTypes = new ArrayList<String>();
    	contentTypes.add("Test content type");
    	
    	model = modelService.update(result, uploads, fileNames, contentTypes, null);
    	
    	Assert.assertEquals(model.getFiles().size(),2);
    	
    	modelService.getDao().delete(Model.class.getName(),"id",result.getId());
    	tearDown();
    }
    
	@Test
	public void testRemoveFile() throws IOException, InvalidInputException
	{
    	setUp();
    	File file = new File(smallFilename);
    	Assert.assertTrue(file.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	List<File> uploads = new ArrayList<File>();
    	uploads.add(copy);
    	List<String> fileNames = new ArrayList<String>();
    	fileNames.add("Test File");
    	List<String> contentTypes = new ArrayList<String>();
    	contentTypes.add("Test content type");
    	
    	Model result = modelService.newModel(model.getUser(),"TestNew",
    	    "TestVersion","TestRef","TestComments","TestURL", uploads, 
    	    fileNames, contentTypes);
    	
    	Assert.assertEquals(result.getFiles().size(),1);
		
        modelService.removeFile(result, result.getFiles().get(0));
        
        try
        {
            PalsFile palsFile = fileService.getNoData(model.getFiles().get(0).getId());
            Assert.fail();
        }
        catch( Exception e )
        {
        	// ok
        }
		
		tearDown();
	}
	
	@Test
    public void testUpdateWithRemoveFiles() throws IOException, SecurityException
    {
    	setUp();
    	File file = new File(smallFilename);
    	Assert.assertTrue(file.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	List<File> uploads = new ArrayList<File>();
    	uploads.add(copy);
    	List<String> fileNames = new ArrayList<String>();
    	fileNames.add("Test File");
    	List<String> contentTypes = new ArrayList<String>();
    	contentTypes.add("Test content type");
    	
    	Model result = modelService.newModel(model.getUser(),"TestNew",
    	    "TestVersion","TestRef","TestComments","TestURL", uploads, 
    	    fileNames, contentTypes);
    	
    	Assert.assertEquals(result.getFiles().size(),1);
    	
    	copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	uploads = new ArrayList<File>();
    	uploads.add(copy);
    	fileNames = new ArrayList<String>();
    	fileNames.add("Test File");
    	contentTypes = new ArrayList<String>();
    	contentTypes.add("Test content type");
    	
    	String filesToRemove = result.getFiles().get(0).getId().toString();
    		
    	model = modelService.update(result, uploads, fileNames, contentTypes, filesToRemove);
    	
    	Assert.assertEquals(model.getFiles().size(),1);
    	
    	modelService.getDao().delete(Model.class.getName(),"id",result.getId());
    	tearDown();
    }
}
