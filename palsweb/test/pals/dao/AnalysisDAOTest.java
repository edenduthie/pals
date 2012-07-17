package pals.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.entity.Analysis;
import pals.entity.AnalysisType;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.entity.ModelOutput;
import pals.entity.TestEntityFactory;
import pals.service.AnalysisService;
import pals.service.AnalysisServiceInterface;
import pals.service.ExperimentService;

public class AnalysisDAOTest extends BaseTest
{
    @Autowired
    AnalysisService analysisEntityService;
    
    @Autowired
    AnalysisServiceInterface analysisService;
    
    @Autowired
    AnalysisDAO dao;
    
    @Autowired 
    ExperimentService experimentService;
    
	String filename ="testdata/flux1.nc";
	DataSetVersion entity;
	File testFile;
	String path;
	
	String executablePath;
	List<AnalysisType> analysisList;
	List<Object> analysisRuns;
	
    public void setUp() throws IOException
    {    	
    	entity = TestEntityFactory.dataSetVersion();
    	dao.persist(entity.getDataSet().getCountry());
    	dao.persist(entity.getDataSet().getVegType());
    	dao.persist(entity.getDataSet().getOwner());
    	dao.persist(entity.getDataSet());
    	entity.setDataSetId(entity.getDataSet().getId());
    	entity.setOwner(entity.getDataSet().getOwner());
    	dao.persist(entity);
    	
		File testFileSrc = new File(filename);
		path = entity.retrieveOutputFilePath();
		testFile = new File(path);
		FileUtils.copyFile(testFileSrc, testFile);
    }
    
    public void setUpPersistent() throws IOException
    {
    	setUp();
		analysisList = TestEntityFactory.analysisList();
		for( AnalysisType analysis : analysisList )
		{
			analysis.setExecutablePath(executablePath);
			analysis.setType(AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE);
			analysis.setVariableName("NEE");
			analysis.setAnalysisTypeName("AnalysisTypeName");
			dao.persist(analysis);
		}
    	analysisService.pollPreparedAnalysable();
    }
    
	public void tearDown() throws IOException
	{
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
		dao.deleteAll("Analysis");
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("Model");
		dao.deleteAll("User");
		dao.deleteAll("Institution");
		dao.deleteAll("Photo");
		dao.deleteAll("AnalysisType");
	}
	
	public void tearDown(Experiment experiment) throws IOException
	{		
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
		dao.deleteAll("Analysis");
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		experimentService.remove(experiment);
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("Model");
		dao.deleteAll("User");
		dao.deleteAll("AnalysisType");
	}
	
	@Test
	public void testGetAnalysisByAnalysisType() throws IOException
	{
		setUpPersistent();
		String analysisType = AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE;
		List<Analysis> analysisList = dao.getAnalysisByAnalysisType(analysisType,Analysis.STATUS_NEW);
		Assert.assertEquals(analysisList.size(),1);
		Analysis analysis = analysisList.get(0);
		Assert.assertNotNull(analysis.getId());
		Assert.assertNotNull(analysis.getAnalysisType());
		Assert.assertNotNull(analysis.getAnalysisType().getAnalysisTypeName());
		Assert.assertNotNull(analysis.getAnalysisType().getVariableName());
		Assert.assertNotNull(analysis.getAnalysable());
		Assert.assertNotNull(analysis.getAnalysable().getId());
		Assert.assertNotNull(analysis.getAnalysable().getName());
		DataSetVersion dsv = (DataSetVersion) analysis.getAnalysable();
		Assert.assertNotNull(dsv.getDataSet());
		Assert.assertNotNull(dsv.getDataSet().getId());
		Assert.assertNotNull(dsv.getDataSet().getName());
		Assert.assertNotNull(dsv.getOwner());
		Assert.assertNotNull(dsv.getOwner().getUsername());
		tearDown();
	}
	
	@Test
	public void testGetAnalysisByAnalysisTypeNoneFound() throws IOException
	{
		setUpPersistent();
		String analysisType = "Non-existent analysis type";
		List<Analysis> analysisList = dao.getAnalysisByAnalysisType(analysisType,Analysis.STATUS_NEW);
		Assert.assertEquals(analysisList.size(),0);
		tearDown();
	}
	
	@Test
	public void testGetAnalysisByModelId() throws IOException 
	{
		setUp();
		Analysis analysis = TestEntityFactory.analysis();
		ModelOutput modelOutput = TestEntityFactory.modelOutput();
		modelOutput.setDataSetVersion(entity);
		modelOutput.setDataSetVersionId(entity.getId());
		analysis.setAnalysable(modelOutput);
    	modelOutput.setOwner(modelOutput.getDataSetVersion().getOwner());
    	modelOutput.getModel().setUser(modelOutput.getOwner());
    	dao.persist(modelOutput.getModel());
    	modelOutput.setModelId(modelOutput.getModel().getId());
    	dao.persist(modelOutput);
    	dao.persist(analysis.getAnalysisType());
    	dao.persist(analysis);
    	
    	List<Analysis> analysisList = dao.getAnalysisByModelId(modelOutput.getModel().getId(),
            analysis.getStatus(), modelOutput.getOwner());
    	Assert.assertTrue( analysisList.size() > 0 );
    	for( Analysis result : analysisList )
    	{
    		assertAnalysis(result);
    	}
    	tearDown();
	}
	
	public void assertAnalysis(Analysis result)
	{
		Assert.assertNotNull(result.getId());
		Assert.assertNotNull(result.getAnalysable());
		Assert.assertNotNull(result.getAnalysable().getId());
		Assert.assertNotNull(result.getAnalysable().getName());
		Assert.assertNotNull(result.getAnalysisType());
		Assert.assertNotNull(result.getAnalysisType().getAnalysisTypeName());
		Assert.assertNotNull(result.getAnalysisType().getVariableName());
		Assert.assertNotNull(result.getAnalysable().getOwner());
		Assert.assertNotNull(result.getAnalysable().getOwner().getUsername());
		ModelOutput mo = (ModelOutput) result.getAnalysable();
		Assert.assertNotNull(mo.getModelId());
		Assert.assertNotNull(mo.getDataSetVersion());
		Assert.assertNotNull(mo.getDataSetVersion().getId());
		Assert.assertNotNull(mo.getDataSetVersion().getDataSetId());
		Assert.assertNotNull(mo.getDataSetVersion().getDataSet());
		Assert.assertNotNull(mo.getDataSetVersion().getDataSet().getId());
		Assert.assertNotNull(mo.getDataSetVersion().getDataSet().getName());
		Assert.assertNotNull(mo.getAccessLevel());
		Assert.assertNotNull(mo.getDataSetVersion().getIsPublic());
		Assert.assertFalse(mo.getDataSetVersion().getIsPublic());
	}
	
	@Test
	public void testGetAnalysisByAnalysisTypeWithExperiment() throws IOException
	{
		setUpPersistent();
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(entity.getDataSet().getOwner());
		dao.persist(experiment);
		entity.setExperiment(experiment);
        dao.update(entity);
        entity.getDataSet().getOwner().setCurrentExperiment(experiment);
		
		String analysisType = AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE;
		List<Analysis> analysisList = dao.getAnalysisByAnalysisType(analysisType,Analysis.STATUS_NEW,
				entity.getDataSet().getOwner());
		Assert.assertEquals(analysisList.size(),1);
		Analysis analysis = analysisList.get(0);
		Assert.assertNotNull(analysis.getId());
		Assert.assertNotNull(analysis.getAnalysisType());
		Assert.assertNotNull(analysis.getAnalysisType().getAnalysisTypeName());
		Assert.assertNotNull(analysis.getAnalysisType().getVariableName());
		Assert.assertNotNull(analysis.getAnalysable());
		Assert.assertNotNull(analysis.getAnalysable().getId());
		Assert.assertNotNull(analysis.getAnalysable().getName());
		DataSetVersion dsv = (DataSetVersion) analysis.getAnalysable();
		Assert.assertNotNull(dsv.getDataSet());
		Assert.assertNotNull(dsv.getDataSet().getId());
		Assert.assertNotNull(dsv.getDataSet().getName());
		Assert.assertNotNull(dsv.getOwner());
		Assert.assertNotNull(dsv.getOwner().getUsername());
		
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
		dao.deleteAll("Analysis");
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		experimentService.remove(experiment);
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("Model");
		dao.deleteAll("User");
		dao.deleteAll("AnalysisType");
	}
	
	@Test
	public void testGetAnalysisByAnalysisTypeWrongExperiment() throws IOException
	{
		setUpPersistent();
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(entity.getDataSet().getOwner());
		dao.persist(experiment);
		entity.setExperiment(experiment);
        dao.update(entity);
        
        Experiment wrongExperiment = new Experiment();
        wrongExperiment.setId(-111);
        entity.getDataSet().getOwner().setCurrentExperiment(wrongExperiment);
		
		String analysisType = AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE;
		List<Analysis> analysisList = dao.getAnalysisByAnalysisType(analysisType,Analysis.STATUS_NEW,
				entity.getDataSet().getOwner());
		Assert.assertEquals(analysisList.size(),0);
		
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
		dao.deleteAll("Analysis");
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		experimentService.remove(experiment);
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("Model");
		dao.deleteAll("User");
		dao.deleteAll("AnalysisType");
	}
	
	@Test
	public void testGetAnalysisByModelIdWithExperiment() throws IOException 
	{
		setUp();
		Analysis analysis = TestEntityFactory.analysis();
		ModelOutput modelOutput = TestEntityFactory.modelOutput();
		modelOutput.setDataSetVersion(entity);
		modelOutput.setDataSetVersionId(entity.getId());
		analysis.setAnalysable(modelOutput);
    	modelOutput.setOwner(modelOutput.getDataSetVersion().getOwner());
    	modelOutput.getModel().setUser(modelOutput.getOwner());
    	dao.persist(modelOutput.getModel());
    	modelOutput.setModelId(modelOutput.getModel().getId());
    	
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(entity.getDataSet().getOwner());
		dao.persist(experiment);
		modelOutput.setExperiment(experiment);
    	
    	dao.persist(modelOutput);
    	dao.persist(analysis.getAnalysisType());
    	dao.persist(analysis);
    	
    	modelOutput.getOwner().setCurrentExperiment(experiment);
    	List<Analysis> analysisList = dao.getAnalysisByModelId(modelOutput.getModel().getId(),
            analysis.getStatus(), modelOutput.getOwner());
    	Assert.assertTrue( analysisList.size() > 0 );
    	for( Analysis result : analysisList )
    	{
    		assertAnalysis(result);
    	}
    	tearDown(experiment);
	}
	
	@Test
	public void testGetAnalysisByModelIdWrongExperiment() throws IOException 
	{
		setUp();
		Analysis analysis = TestEntityFactory.analysis();
		ModelOutput modelOutput = TestEntityFactory.modelOutput();
		modelOutput.setDataSetVersion(entity);
		modelOutput.setDataSetVersionId(entity.getId());
		analysis.setAnalysable(modelOutput);
    	modelOutput.setOwner(modelOutput.getDataSetVersion().getOwner());
    	modelOutput.getModel().setUser(modelOutput.getOwner());
    	dao.persist(modelOutput.getModel());
    	modelOutput.setModelId(modelOutput.getModel().getId());
    	
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(entity.getDataSet().getOwner());
		dao.persist(experiment);
		modelOutput.setExperiment(experiment);
    	
    	dao.persist(modelOutput);
    	dao.persist(analysis.getAnalysisType());
    	dao.persist(analysis);
    	
    	Experiment wrongExperiment = new Experiment();
    	wrongExperiment.setId(-999);
    	modelOutput.getOwner().setCurrentExperiment(wrongExperiment);
    	List<Analysis> analysisList = dao.getAnalysisByModelId(modelOutput.getModel().getId(),
            analysis.getStatus(), modelOutput.getOwner());
    	Assert.assertEquals( analysisList.size(), 0);
    	tearDown(experiment);
	}
	
	@Test
	public void testGetAnalysisByModelIdNoNull() throws IOException 
	{
		setUp();
		Analysis analysis = TestEntityFactory.analysis();
		ModelOutput modelOutput = TestEntityFactory.modelOutput();
		modelOutput.setDataSetVersion(entity);
		modelOutput.setDataSetVersionId(entity.getId());
		analysis.setAnalysable(modelOutput);
    	modelOutput.setOwner(modelOutput.getDataSetVersion().getOwner());
    	modelOutput.getModel().setUser(modelOutput.getOwner());
    	dao.persist(modelOutput.getModel());
    	modelOutput.setModelId(modelOutput.getModel().getId());
    	
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(entity.getDataSet().getOwner());
		dao.persist(experiment);
		modelOutput.setExperiment(experiment);
    	
    	dao.persist(modelOutput);
    	dao.persist(analysis.getAnalysisType());
    	dao.persist(analysis);
    	
    	modelOutput.getOwner().setCurrentExperiment(null);
    	List<Analysis> analysisList = dao.getAnalysisByModelId(modelOutput.getModel().getId(),
            analysis.getStatus(), modelOutput.getOwner());
    	Assert.assertEquals( analysisList.size(), 0);
    	tearDown(experiment);
	}
	
	@Test
	public void testGetAnalysisByAnalysisTypeWithExperimentSharedWithAll() throws IOException
	{
		setUpPersistent();
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(entity.getDataSet().getOwner());
		experiment.setShareWithAll(true);
		dao.persist(experiment);
		entity.setExperiment(experiment);
        dao.update(entity);
        entity.getDataSet().getOwner().setCurrentExperiment(null);
		
		String analysisType = AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE;
		List<Analysis> analysisList = dao.getAnalysisByAnalysisType(analysisType,Analysis.STATUS_NEW,
				entity.getDataSet().getOwner());
		Assert.assertEquals(analysisList.size(),1);
		Analysis analysis = analysisList.get(0);
		Assert.assertNotNull(analysis.getId());
		Assert.assertNotNull(analysis.getAnalysisType());
		Assert.assertNotNull(analysis.getAnalysisType().getAnalysisTypeName());
		Assert.assertNotNull(analysis.getAnalysisType().getVariableName());
		Assert.assertNotNull(analysis.getAnalysable());
		Assert.assertNotNull(analysis.getAnalysable().getId());
		Assert.assertNotNull(analysis.getAnalysable().getName());
		DataSetVersion dsv = (DataSetVersion) analysis.getAnalysable();
		Assert.assertNotNull(dsv.getDataSet());
		Assert.assertNotNull(dsv.getDataSet().getId());
		Assert.assertNotNull(dsv.getDataSet().getName());
		Assert.assertNotNull(dsv.getOwner());
		Assert.assertNotNull(dsv.getOwner().getUsername());
		
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
		dao.deleteAll("Analysis");
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		experimentService.remove(experiment);
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("Model");
		dao.deleteAll("User");
		dao.deleteAll("AnalysisType");
	}
	
	@Test
	public void testGetAnalysisByModelIdWithExperimentShareWithAll() throws IOException 
	{
		setUp();
		Analysis analysis = TestEntityFactory.analysis();
		ModelOutput modelOutput = TestEntityFactory.modelOutput();
		modelOutput.setDataSetVersion(entity);
		modelOutput.setDataSetVersionId(entity.getId());
		analysis.setAnalysable(modelOutput);
    	modelOutput.setOwner(modelOutput.getDataSetVersion().getOwner());
    	modelOutput.getModel().setUser(modelOutput.getOwner());
    	dao.persist(modelOutput.getModel());
    	modelOutput.setModelId(modelOutput.getModel().getId());
    	
		Experiment experiment = TestEntityFactory.experiment();
		experiment.setOwner(entity.getDataSet().getOwner());
		experiment.setShareWithAll(true);
		dao.persist(experiment);
		modelOutput.setExperiment(experiment);
    	
    	dao.persist(modelOutput);
    	dao.persist(analysis.getAnalysisType());
    	dao.persist(analysis);
    	
    	modelOutput.getOwner().setCurrentExperiment(null);
    	List<Analysis> analysisList = dao.getAnalysisByModelId(modelOutput.getModel().getId(),
            analysis.getStatus(), modelOutput.getOwner());
    	Assert.assertTrue( analysisList.size() > 0 );
    	for( Analysis result : analysisList )
    	{
    		assertAnalysis(result);
    	}
    	tearDown(experiment);
	}
}
