package pals.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.analysis.NetcdfUtil;
import pals.dao.DAO;
import pals.entity.Analysable;
import pals.entity.AnalysisType;
import pals.entity.Analysis;
import pals.entity.ModelOutput;
import pals.entity.TestEntityFactory;
import ucar.nc2.NetcdfFile;

public class AnalysableServiceTest extends BaseTest
{
    @Autowired
    AnalysableService as;
    
    @Autowired
    DAO dao;
    
	String filename ="testdata/flux1.nc";
	ModelOutput entity;
	File testFile;
	String path;
	NetcdfFile ncFile;
	List<AnalysisType> analysisList;
    
    public void setUp() throws IOException
    {
    	entity = TestEntityFactory.modelOutput();
		//entity.setModelOutputId(1);
    	System.out.println(entity.getOwner().getUsername());
    	dao.persist(entity.getOwner());
    	dao.persist(entity.getDataSetVersion().getDataSet().getVegType());
    	dao.persist(entity.getDataSetVersion().getDataSet().getCountry());
    	dao.persist(entity.getDataSetVersion().getDataSet());
    	entity.getDataSetVersion().setDataSetId(entity.getDataSetVersion().getDataSet().getId());
    	dao.persist(entity.getDataSetVersion());
    	entity.getModel().setUser(entity.getOwner());
    	dao.persist(entity.getModel());
    	dao.persist(entity);
		File testFileSrc = new File(filename);
		path = entity.retrieveOutputFilePath();
		testFile = new File(path);
		FileUtils.copyFile(testFileSrc, testFile);
		ncFile = NetcdfUtil.parse(path);
		analysisList = TestEntityFactory.analysisList();
		for( AnalysisType analysis : analysisList )
		{
			analysis.setType(AnalysisType.MODEL_OUTPUT_ANALYSIS_TYPE);
			dao.persist(analysis);
		}
    }
    
	public void tearDown() throws IOException
	{
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
		dao.deleteAll("Analysis");
		dao.deleteAll("ModelOutput");
		dao.deleteAll("Model");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("User");
		dao.deleteAll("AnalysisType");
	}
/*
    @Test
    public void testPrepareAnalyses() throws Exception
    {
    	setUp();
    	//dao.persist(entity);
    	as.prepareAnalyses(analysisList,ncFile,entity);
    	tearDown();
    }
*/
    @Test 
    public void testRunAnalysis() throws Exception
    {
    	setUp();
    	as.runAnalysis(analysisList, entity);
    	List<Object> analysisRuns = dao.getAll("Analysis");
    	Assert.assertTrue(analysisRuns.size()>0);
    	for( Object object : analysisRuns )
    	{
    		Analysis ar = (Analysis) object;
    		Assert.assertNotNull(ar.getAnalysable());
    		Assert.assertNotNull(ar.getAnalysisType());
    		Assert.assertEquals(ar.getStatus(), Analysis.STATUS_NEW);
    	}
    	tearDown();
    }
}
