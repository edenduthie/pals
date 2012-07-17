package pals.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Globals;
import pals.analysis.AnalysisException;
import pals.dao.DAO;
import pals.entity.Analysis;
import pals.entity.AnalysisType;
import pals.entity.DataSetVersion;
import pals.entity.ModelOutput;
import pals.entity.TestEntityFactory;
import pals.utils.PalsFileUtils;
import ucar.nc2.NetcdfFile;

public class AnalysisServiceTestModelOutput extends BaseTest
{
	@Autowired
    AnalysisService analysisEntityService;
    
    @Autowired
    AnalysisServiceInterface analysisService;
    
    @Autowired
    DAO dao;
    
	String filename ="testdata/flux1.nc";
	String filenameModelOutput ="testdata/CABLE_Tumbarumba.nc";
	DataSetVersion dataSetVersion;
	ModelOutput modelOutput;
	File testFile;
	File testFileModelOutput;
	String path;
	
	Analysis analysis;
	AnalysisType analysisType;
	
	String executablePath;
	NetcdfFile ncFile;
	List<AnalysisType> analysisList;
	List<Object> analysisRuns;
	
    public void setUp() throws IOException
    {    	
    	dataSetVersion = TestEntityFactory.dataSetVersion();
    	dao.persist(dataSetVersion.getDataSet().getCountry());
    	dao.persist(dataSetVersion.getDataSet().getVegType());
    	dao.persist(dataSetVersion.getDataSet().getOwner());
    	dao.persist(dataSetVersion.getDataSet());
    	dataSetVersion.setDataSetId(dataSetVersion.getDataSet().getId());
    	dataSetVersion.setOwner(dataSetVersion.getDataSet().getOwner());
    	dao.persist(dataSetVersion);
    	
    	modelOutput = TestEntityFactory.modelOutput();
    	modelOutput.setDataSetVersion(dataSetVersion);
    	modelOutput.setOwner(dataSetVersion.getOwner());
    	dao.persist(modelOutput.getModel());
    	dao.persist(modelOutput);
    	
		File testFileSrc = new File(filename);
		path = dataSetVersion.retrieveOutputFilePath();
		testFile = new File(path);
		FileUtils.copyFile(testFileSrc, testFile);
		
		File testFileModelOutputSrc = new File(filenameModelOutput);
		path = modelOutput.retrieveOutputFilePath();
		testFileModelOutput = new File(path);
		FileUtils.copyFile(testFileModelOutputSrc, testFileModelOutput);
    	
		analysisType = TestEntityFactory.analysisType();
		String cwd = System.getProperty("user.dir");
		executablePath = "R -f " + cwd + "\\" + "WebContent" +
		    "\\" + "r" + "\\" + "ModelAnnualCycleNEE.R";
		analysisType.setExecutablePath(executablePath);
		analysisType.setId(1);
		analysisType.setType(AnalysisType.MODEL_OUTPUT_ANALYSIS_TYPE);
		analysisType.setVariableName("NEE");
		
		analysis = TestEntityFactory.analysis();
		analysis.setAnalysable(modelOutput);
		analysis.setAnalysisType(analysisType);
		analysis.setId(66);
    }
    
	public void tearDown() throws IOException
	{
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
		if( testFileModelOutput != null && testFileModelOutput.exists() )
		{
			testFileModelOutput.deleteOnExit();
		}
		dao.deleteAll("Analysis");
		dao.deleteAll("ModelOutput");
		dao.deleteAll("DataSetVersion");
		dao.deleteAll("DataSet");
		dao.deleteAll("Country");
		dao.deleteAll("VegetationType");
		dao.deleteAll("Model");
		dao.deleteAll("User");
		dao.deleteAll("AnalysisType");
	}
	
	@Test
	public void testGeneratePlotPNGModelOutput() throws IOException, AnalysisException, InterruptedException
	{
    	setUp();
    	analysisEntityService.generatePlot(analysis, AnalysisService.OUTPUT_TYPE_PNG);
    	checkFile(analysis,Globals.PNG_FILE_SUFFIX);
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