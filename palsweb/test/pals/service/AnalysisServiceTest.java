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
import pals.Globals;
import pals.analysis.AnalysisException;
import pals.analysis.NetcdfUtil;
import pals.dao.DAO;
import pals.entity.Analysis;
import pals.entity.AnalysisType;
import pals.entity.DataSetVersion;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.TestEntityFactory;
import pals.utils.PalsFileUtils;
import ucar.nc2.NetcdfFile;

public class AnalysisServiceTest extends BaseTest 
{
    @Autowired
    AnalysisService analysisEntityService;
    
    @Autowired
    AnalysisServiceInterface analysisService;
    
    @Autowired DataSetVersionService dataSetVersionService;
    
    @Autowired
    DAO dao;
    
	String filename ="testdata/flux1.nc";
	DataSetVersion entity;
	File testFile;
	String path;
	
	Analysis analysisO;
	AnalysisType analysisType;
	
	String executablePath;
	NetcdfFile ncFile;
	List<AnalysisType> analysisList;
	List<Object> analysisRuns;
	
	@Autowired ModelOutputService modelOutputService;
	@Autowired UserServiceInterface userService;
	
    public void setUp() throws IOException
    {    	
		String cwd = System.getProperty("user.dir");
		executablePath = cwd + "/WebContent/r/rWrapper -f " + cwd + "/WebContent/r/ObsAnnualCycleNEE.R";
		setUp(executablePath,filename);
    }
    
    public void setUp(String testExecutablePath,String testFilename) throws IOException
    {
    	entity = TestEntityFactory.dataSetVersion();
    	dao.persist(entity.getDataSet().getCountry());
    	dao.persist(entity.getDataSet().getVegType());
    	dao.persist(entity.getDataSet().getOwner());
    	dao.persist(entity.getDataSet());
    	entity.setDataSetId(entity.getDataSet().getId());
    	entity.setOwner(entity.getDataSet().getOwner());
    	dao.persist(entity);
    	
		File testFileSrc = new File(testFilename);
		path = entity.retrieveOutputFilePath();
		testFile = new File(path);
		FileUtils.copyFile(testFileSrc, testFile);
    	
		analysisType = TestEntityFactory.analysisType();
		analysisType.setExecutablePath(testExecutablePath);
		analysisType.setId(1);
		analysisType.setType(AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE);
		analysisType.setVariableName("NEE");
		
		analysisO = TestEntityFactory.analysis();
		analysisO.setAnalysable(entity);
		analysisO.setAnalysisType(analysisType);
		analysisO.setId(66);    	
    }
    
    public void setUpPersistent() throws IOException
    {
    	setUp();
		ncFile = NetcdfUtil.parse(path);
		analysisList = TestEntityFactory.analysisList();
		for( AnalysisType analysis : analysisList )
		{
			analysis.setExecutablePath(executablePath);
			analysis.setType(AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE);
			analysis.setVariableName("NEE");
			dao.persist(analysis);
		}
    	analysisService.pollPreparedAnalysable();
    	analysisRuns = dao.getAll("Analysis");
    }
    
	public void tearDown() throws IOException
	{
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
		dao.deleteAll("Analysis");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("User");
		dao.deleteAll("AnalysisType");
	}
	
    @Test
    public void testStartAnalysisRun() throws IOException
    {
    	setUpPersistent();
    	Assert.assertTrue( analysisRuns.size() > 0 );
    	for( Object analysisObject : analysisRuns )
    	{
    		Analysis analysis = (Analysis) analysisObject;    		
    		analysisEntityService.startAnalysisRun(analysis);
    	}
    	analysisRuns = dao.getAll("Analysis");
    	Assert.assertTrue( analysisRuns.size() > 0 );
    	for( Object analysisObject : analysisRuns )
    	{
    		Analysis analysis = (Analysis) analysisObject;
    		Assert.assertEquals(analysis.getStatus(), Analysis.STATUS_COMPLETE);
    		checkFile(analysis,Globals.PNG_FILE_SUFFIX);
    		checkFile(analysis,Globals.PDF_FILE_SUFFIX);
    		checkFile(analysis,Globals.THUMB_FILE_SUFFIX);
    	}
    	tearDown();
    }
    
    @Test
    public void testGeneratePlotPDF() throws IOException, AnalysisException, InterruptedException
    {
    	setUp();
    	analysisEntityService.generatePlot(analysisO, AnalysisService.OUTPUT_TYPE_PDF);
    	checkFile(analysisO,Globals.PDF_FILE_SUFFIX);
    	tearDown();
    }
    
    @Test
    public void testGeneratePlotPNG() throws IOException, AnalysisException, InterruptedException
    {
    	setUp();
    	analysisEntityService.generatePlot(analysisO, AnalysisService.OUTPUT_TYPE_PNG);
    	checkFile(analysisO,Globals.PNG_FILE_SUFFIX);
    	tearDown();
    }
    
    @Test
    public void testDeleteAllAnalysisForAnalysable() throws IOException
    {
    	setUpPersistent();
    	List<Object> results = dao.getAll(Analysis.class.getName());
    	Assert.assertTrue(results.size()>0);
    	analysisEntityService.deleteAllAnalysisForAnalysable(entity);
    	results = dao.getAll(Analysis.class.getName());
    	Assert.assertEquals(results.size(),0);
    	tearDown();
    }
    
    @Test
    public void testDeleteAllAnalysisForAnalysableDeletesFiles() throws IOException, InterruptedException
    {
    	// if this fails check that testdata/username is empty before
    	// running the test suite
    	
    	setUpPersistent();
    	List<Object> results = dao.getAll(Analysis.class.getName());
    	Assert.assertTrue(results.size()>0);
    	analysisEntityService.deleteAllAnalysisForAnalysable(entity);
    	results = dao.getAll(Analysis.class.getName());
    	Assert.assertEquals(results.size(),0);
        checkFileDeleted(analysisO,Globals.PNG_FILE_SUFFIX);
    	checkFileDeleted(analysisO,Globals.PDF_FILE_SUFFIX);
    	checkFileDeleted(analysisO,Globals.THUMB_FILE_SUFFIX);
    	tearDown();
    }
    
//    @Test
//    public void testGeneratePlotPNGMissingQg() throws IOException, InterruptedException
//    {
//    	String cwd = System.getProperty("user.dir");
//    	
//    	// set the path to run the Qg plot
//		String missingQgExecutablePath = "R -f " + cwd + "\\" + "WebContent" +
//	    "\\" + "r" + "\\" + "ObsAnnualCycleQg.R";
//		
//		// choose and executable without Qg
//		String missingQgfilename = "testdata/DataSetMissingQg.nc";
//		
//    	setUp(missingQgExecutablePath,missingQgfilename);
//    	try
//    	{
//    	    analysisEntityService.generatePlot(analysis, AnalysisService.OUTPUT_TYPE_PNG);
//    	}
//    	catch( AnalysisException e )
//    	{
//    		// expected
//    		System.out.println("MESSAGE:" + e.getMessage());
//    		Assert.assertEquals(e.getMessage(), "Cannot find Qg in " + entity.retrieveOutputFilePath());
//    	}
//    
//    	tearDown();
//    }
    
    public void checkFile(Analysis analysis, String suffix)
    {
		String filename = PalsFileUtils.getAnalysisRunFileLabel(analysis) + suffix;
		File file = new File(filename);
		Assert.assertTrue(file.exists());
		file.deleteOnExit();
    }
    
    public void checkFileDeleted(Analysis analysis, String suffix)
    {
		String filename = PalsFileUtils.getAnalysisRunFileLabel(analysis) + suffix;
		System.out.println(filename);
		File file = new File(filename);
		Assert.assertFalse(file.exists());
    }
    
//    @Test
//    public void testGenerateBenchmark() throws IOException, AnalysisException, InterruptedException, InvalidInputException, CSV2NCDFConversionException
//    {
//    	setUp();
//    	
//		List<String> benchmarks = new ArrayList<String>();
//	    benchmarks.add("testdata/benchmarks/audubonConversion1.0.2.csv");
//	    benchmarks.add("testdata/benchmarks/degeroConversion1.0.2.csv");
//	    benchmarks.add("testdata/benchmarks/espirraConversion1.0.2.csv");
//		
//	    DataSetVersion chosenVersion = null;
//	    
//	    for( String benchmark : benchmarks )
//	    {
//	    	DataSetVersion dsv = TestEntityFactory.dataSetVersion();
//	    	dao.persist(dsv.getDataSet().getCountry());
//	    	dsv.getDataSet().getVegType().setVegetationType(benchmark);
//	    	dao.persist(dsv.getDataSet().getVegType());
//	        dsv.getDataSet().setOwner(entity.getDataSet().getOwner());
//	    	dao.persist(dsv.getDataSet());
//	    	dsv.setDataSetId(dsv.getDataSet().getId());
//	    	dsv.setOwner(dsv.getDataSet().getOwner());
//	    	dao.persist(dsv);
//	    	File storeUploadedFile = new File(dsv.uploadedFilePath());
//	    	File uploadedCSVFile = new File(benchmark);
//	    	FileUtils.copyFile(uploadedCSVFile,storeUploadedFile);
////	        userService.convertDataSetCSV2NCDF(storeUploadedFile,
////			    PalsFileUtils.getDataSetVersionFluxFile(dsv),
////			    PalsFileUtils.getDataSetVersionMetFile(dsv),dsv);
//	        dsv.getDataSet().setLatestVersion(dsv);
//	        dao.update(dsv.getDataSet());
//	        if( chosenVersion == null ) chosenVersion = dsv;
//	    }
//    	
////    	Thread thread = dataSetVersionService.empiricalBenchmarks(chosenVersion,chosenVersion.getDataSet().getOwner());
////    	thread.join();
//    	
//    	File uploadedFile = new File("testdata/benchmarks/mo2714.nc");
//    	
//    	Model model = TestEntityFactory.model();
//    	dao.persist(model);
//    	
//		ModelOutput modelOutput = 
//			modelOutputService.newModelOutput(
//			    chosenVersion.getDataSet().getOwner(), uploadedFile, "Model_Output_Name",
//			    model.getId(), chosenVersion, "SS", "PS", "UC");
//		
//		analysisType.setType(AnalysisType.MODEL_OUTPUT_ANALYSIS_TYPE);
//		analysisType.setName("ModelAnnualCycleNEE");
//		analysisO.setAnalysable(modelOutput);
//    	
//    	analysisEntityService.generateBenchmark(analysisO,AnalysisService.OUTPUT_TYPE_PNG);
//    	
//    	//modelOutputService.removeAllFiles();
//		//modelOutputService.deleteModelOutput(modelOutput);
//    	//File file = new File(PalsFileUtils.getDataSetVersionBenchmarkFilePath(entity));
//    	//file.delete();
//    	//dao.deleteAll("Model");
//    	//tearDown();
//    }
}
