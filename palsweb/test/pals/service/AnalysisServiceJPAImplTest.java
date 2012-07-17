package pals.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Configuration;
import pals.Globals;
import pals.analysis.NetcdfUtil;
import pals.dao.DAO;
import pals.entity.Analysis;
import pals.entity.AnalysisType;
import pals.entity.DataSetVersion;
import pals.entity.ModelOutput;
import pals.entity.TestEntityFactory;
import pals.utils.PalsFileUtils;
import ucar.nc2.NetcdfFile;


public class AnalysisServiceJPAImplTest extends BaseTest
{
    @Autowired
    AnalysisServiceInterface analysisService;
    
    @Autowired
    DAO dao;
    
	String filename ="testdata/flux1.nc";
	ModelOutput entity;
	File testFile;
	String path;
	NetcdfFile ncFile;
	List<AnalysisType> analysisTypeList;
	String executablePath;
	
	DataSetVersion dataSetVersion;
	
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
		analysisTypeList = TestEntityFactory.analysisList();
		for( AnalysisType analysisType : analysisTypeList )
		{
			analysisType.setType(AnalysisType.MODEL_OUTPUT_ANALYSIS_TYPE);
			dao.persist(analysisType);
		}
    }
    
    /*
     * Sets up analysis runs that will actually run
     */
    public void setUpFullAnalysis() throws IOException
    {
        // create an executable	
		String cwd = System.getProperty("user.dir");
		executablePath = "R -f " + cwd + "\\" + "WebContent" +
		    "\\" + "r" + "\\" + "ObsAnnualCycleNEE.R";
		
		// set the path to the application data to come from the test data directory
		Configuration.getInstance().PATH_TO_APP_DATA = cwd + "\\" + "testdata";
		
    	dataSetVersion = TestEntityFactory.dataSetVersion();
    	dao.persist(dataSetVersion.getDataSet().getCountry());
    	dao.persist(dataSetVersion.getDataSet().getVegType());
    	dao.persist(dataSetVersion.getDataSet().getOwner());
    	dao.persist(dataSetVersion.getDataSet());
    	dataSetVersion.setDataSetId(dataSetVersion.getDataSet().getId());
    	dataSetVersion.setOwner(dataSetVersion.getDataSet().getOwner());
    	dao.persist(dataSetVersion);
    	
		File testFileSrc = new File(filename);
		// the following path will be in the testdata/username directory as we have changed the 
		// properties
		path = dataSetVersion.retrieveOutputFilePath();
		testFile = new File(path);
		FileUtils.copyFile(testFileSrc, testFile);
		
		// create the analysis types in the database to be run
		// on  the DataSetVersion
		analysisTypeList = TestEntityFactory.analysisList();
		for( AnalysisType analysisType : analysisTypeList )
		{
			analysisType.setExecutablePath(executablePath);
			analysisType.setType(AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE);
			analysisType.setVariableName("NEE");
			dao.persist(analysisType);
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

    @Test
    public void testGetAnalysisList()
    {
        List<AnalysisType> list = analysisService.getAnalysisList();
        for( AnalysisType analysis : list )
        {
//            System.out.println(analysis.getName());
        }
    }

    @Test
    public void testPollPreparedAnalysable() throws IOException
    {
    	setUp();
    	analysisService.pollPreparedAnalysable();
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
 
    @Test
    /**
     * Runs a full test, creating Analysis objects in the database for
     * all Analysable present, polling these and running the Analysis
     */
    public void pollAnalysisRuns() throws IOException
    {
    	setUpFullAnalysis();
    	
    	// prepare the analysable in the database
    	analysisService.pollPreparedAnalysable();
    	
    	// check they are there
    	List<Object> analysisRuns = dao.getAll("Analysis");
    	Assert.assertTrue(analysisRuns.size()>0);
    	
    	// run the analysis on these runs
    	analysisService.pollAnalysisRuns();
    	
    	// check they have completed
    	analysisRuns = dao.getAll("Analysis");
    	Assert.assertTrue(analysisRuns.size()>0);
    	for( Object object : analysisRuns )
    	{
    		Analysis ar = (Analysis) object;
    		Assert.assertNotNull(ar.getAnalysable());
    		Assert.assertNotNull(ar.getAnalysisType());
    		Assert.assertEquals(ar.getStatus(), Analysis.STATUS_COMPLETE);
    		
    		// check that the files have been created
    		checkFile(ar,Globals.PNG_FILE_SUFFIX);
    		checkFile(ar,Globals.PDF_FILE_SUFFIX);
    		checkFile(ar,Globals.THUMB_FILE_SUFFIX);
    	}
    	tearDown();
    }
    
    public void checkFile(Analysis analysis, String suffix)
    {
		String filename = PalsFileUtils.getAnalysisRunFileLabel(analysis) + suffix;
		File file = new File(filename);
		Assert.assertTrue(file.exists());
		file.deleteOnExit();
    }
}
