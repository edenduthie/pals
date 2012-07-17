package pals.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Configuration;
import pals.Globals;
import pals.dao.DAO;
import pals.entity.Analysis;
import pals.entity.AnalysisType;
import pals.entity.DataSetVersion;
import pals.entity.ModelOutput;
import pals.entity.TestEntityFactory;
import pals.service.AnalysisServiceInterface;
import pals.utils.PalsFileUtils;
import ucar.nc2.NetcdfFile;

public class SchedulerTest extends BaseTest
{
	private static final int WAIT_TIME = 1000*3;
	
	String preloadDevelFilename = System.getProperty("user.dir") + "\\scripts\\mysql\\preload_devel.sql";
	
	@Autowired
	DataSource dataSource;
	
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
	
    /*
     * Sets up analysis runs that will actually run
     */
    public void setUpFullAnalysis() throws IOException
    {		
		// set the path to the application data to come from the test data directory
		Configuration.getInstance().PATH_TO_APP_DATA = System.getProperty("user.dir") + "\\" + "testdata";
		
    	dataSetVersion = TestEntityFactory.dataSetVersion();
    	dataSetVersion.getDataSet().getCountry().setId(null);
    	dao.persist(dataSetVersion.getDataSet().getCountry());
    	dataSetVersion.getDataSet().getVegType().setVegetationType("THIS CANNOT EXIST");
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
    }
	
    public void setUp() throws IOException
    {
    	System.out.println(dataSource.toString());
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        BufferedReader in = new BufferedReader(new FileReader(new File(preloadDevelFilename)));
        String line;
        String allCommands = "";
        while( (line = in.readLine()) != null )
        {
        	if( line != null && line.trim().length() > 0 )
        	{
        		allCommands += line + " ";
        		//jdbcTemplate.execute(line);
        	}
        }
        jdbcTemplate.execute(allCommands);
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

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Test
	public void testRun() throws IOException, InterruptedException
	{
		setUp();
		setUpFullAnalysis();
		Scheduler scheduler = Scheduler.getInstance();
		scheduler.setAnalysisService(analysisService);
		Thread t = new Thread(scheduler);
		t.start();
		Thread.sleep(WAIT_TIME);
		scheduler.stop();
		
		// check the analysis runs have been completed
		List<Object> analysisRuns = dao.getAll("Analysis");
    	Assert.assertTrue(analysisRuns.size()>0);
    	for( Object object : analysisRuns )
    	{
    		Analysis ar = (Analysis) object;
    		Assert.assertNotNull(ar.getAnalysable());
    		Assert.assertNotNull(ar.getAnalysisType());
    		System.out.println("ERROR MESSAGE:"+ar.getErrorMessage());
    		Assert.assertEquals(ar.getStatus(), Analysis.STATUS_COMPLETE);
    		
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
